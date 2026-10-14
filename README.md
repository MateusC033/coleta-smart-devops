# Coleta Smart DevOps

API de gestão de coleta seletiva com tema ESG, adaptada para a Fase 6 (DevOps) do curso de ADS da FIAP. Esta entrega é a Coleta Smart original, feita na Fase 4 (Spring), levada agora para um ciclo completo de DevOps: containerização, pipeline de CI/CD com build, testes e deploy automatizados, e orquestração local.

## Projeto Cidades ESGInteligentes

A ideia surgiu de um problema simples: moradores muitas vezes não sabem quando o caminhão de coleta vai passar pelo bairro deles (principalmente recem chegados na visinhança), e quando há algum imprevisto (como atraso ou mudança de rota), não existe nenhum canal direto de comunicação. Do outro lado, gestores não têm uma forma centralizada de acompanhar as rotas e os motoristas.

O Coleta Smart é uma API que tenta resolver isso. Ela conecta três perfis de usuário, o gestor, o motorista e o morador, numa única plataforma de gestão de coleta seletiva.

O gestor pode cadastrar motoristas, registrar os caminhões da frota e definir as rotas de coleta (quais bairros são atendidos, em quais dias e qual o turno). Quando acontece algum imprevisto numa rota, ele ou o motorista podem enviar um alerta para o sistema.

O morador, depois de se cadastrar informando seu endereço, consegue consultar quais rotas passam pelo seu bairro. Ele também pode enviar uma mensagem de contato ou denúncia, por exemplo reportar lixo acumulado numa rua.

## Como executar localmente com Docker

Pré-requisitos: Docker Desktop (Windows ou macOS) ou Docker Engine + Compose (Linux).

1. Copie o arquivo de exemplo de variáveis de ambiente:

   ```powershell
   Copy-Item .env.example .env
   ```

   No macOS/Linux: `cp .env.example .env`. O `.env` fica fora do git.

2. Opcionalmente, edite o `.env` e troque as senhas e a chave JWT por valores de sua preferência. Os valores padrão já funcionam para desenvolvimento local.

3. Suba a aplicação e o banco:

   ```powershell
   docker compose up --build
   ```

   O comando sobe dois containers: `coleta-db` (Postgres 16) e `coleta-app` (a API Spring Boot). O compose aguarda o banco ficar `healthy` antes de iniciar a aplicação.

4. Verifique que a API está no ar:

   ```powershell
   curl http://localhost:8080/actuator/health
   ```

   A resposta esperada é `{"status":"UP"}`.

5. Para derrubar o stack: `docker compose down`. Para remover também o volume do Postgres (apaga os dados): `docker compose down -v`.

## Pipeline CI/CD

A ferramenta escolhida foi o **GitHub Actions**. O workflow está em `.github/workflows/ci.yml` e dispara em dois eventos: `push` nas branches `main` e `develop` e `pull_request` apontando para essas branches.

### Branches e ambientes

A convenção é a clássica de dois ambientes:

| Branch | Ambiente no Railway | URL |
|---|---|---|
| `develop` | staging | https://app-staging-36d2.up.railway.app |
| `main` | production | https://app-production-90364.up.railway.app |

O fluxo é: desenvolvo em `develop`, push → o pipeline builda, testa e, se verde, o Railway deploya em staging. Quando a versão passa no staging, faço merge em `main` → o mesmo pipeline roda e o Railway deploya em production.

### Jobs do workflow

1. **`build-test`** (roda sempre): checkout, setup do JDK 17 (Temurin, com cache do Maven), `mvn clean package -DskipTests` e `mvn test`. O artefato `surefire-reports` é publicado para debug em caso de falha.
2. **`deploy-staging`** (só em push na `develop`): depende do `build-test` verde. Usa `environment: staging` do GitHub Actions, o que registra o deploy na UI do commit com o link do ambiente.
3. **`deploy-production`** (só em push na `main`): idêntico ao anterior, mas para o ambiente de produção.

### Como o deploy funciona na prática

O deploy real é executado pelo **Railway**, que observa diretamente as branches `main` e `develop` do repositório. Nos dois ambientes está ativada a opção **"Wait for CI"**: o Railway só inicia o deploy depois que o workflow do GitHub Actions ficar verde para aquele commit. Isso impede que código com testes quebrados vá para o ar.

A conexão entre a aplicação e o Postgres é feita por rede privada do Railway, usando **reference variables** (`${{Postgres.PGHOST}}`, `${{Postgres.PGPORT}}`, etc.) que resolvem para o Postgres do mesmo environment. Credenciais e `JWT_SECRET` são distintos entre staging e production. Nenhuma senha está no repositório.

## Containerização

A containerização usa duas peças: um `Dockerfile` multi-stage para a aplicação e um `docker-compose.yml` para orquestrar aplicação + banco em desenvolvimento local.

### Dockerfile

Está dividido em dois estágios:

1. **Build** (`maven:3.9.9-eclipse-temurin-17`): compila o JAR com Maven dentro do próprio container. O host não precisa ter Maven instalado. A camada de download de dependências (`mvn dependency:go-offline`) vem antes do `COPY src`, para aproveitar o cache enquanto o `pom.xml` não muda.

2. **Runtime** (`eclipse-temurin:17-jre-alpine`): imagem enxuta só com a JRE e o JAR final. A aplicação roda como usuário não-root `app` para reduzir superfície de ataque. O JAR tem nome fixo (`app.jar`) via `<finalName>` no `pom.xml`.

Um `.dockerignore` reduz o contexto enviado ao daemon e evita que arquivos locais como `.env`, `target/`, `.git/` e material de apoio da sessão entrem na imagem.

### docker-compose.yml

Orquestra dois serviços:

- `db` (postgres:16-alpine): com volume nomeado `postgres-data` para persistir os dados entre execuções, healthcheck via `pg_isready` e credenciais vindas do `.env`.
- `app`: build local a partir do Dockerfile, variáveis de ambiente vindas do `.env`, healthcheck via `/actuator/health` e `depends_on: service_healthy` do `db` (a aplicação só inicia depois do banco responder).

Os dois serviços conversam pela rede dedicada `coleta-net` (bridge). A porta `8080` é publicada no host.

Variáveis de ambiente, volume e rede são as três peças que o enunciado pede para a orquestração, e estão todas configuradas no compose.

## Prints do funcionamento

Todas as evidências estão em [`docs/prints/`](docs/prints/).

| # | Evidência | Arquivo |
|---|---|---|
| 1 | GitHub Actions com os workflows das branches `main` e `develop` verdes | [`01-pipeline-list.png`](docs/prints/01-pipeline-list.png) |
| 2 | Detalhe de um workflow mostrando os três jobs (`build-test`, `deploy-staging`, `deploy-production`) e o link do ambiente | [`03-pipeline-jobs.png`](docs/prints/03-pipeline-jobs.png) |
| 3 | Canvas do Railway em **production** com Postgres e `app` online | [`04-railway-production.png`](docs/prints/04-railway-production.png) |
| 4 | Canvas do Railway em **staging** com Postgres e `app` online | [`05-railway-staging.png`](docs/prints/05-railway-staging.png) |
| 5 | `GET /actuator/health` de **staging** respondendo `{"status":"UP"}` | [`06-staging-health.png`](docs/prints/06-staging-health.png) |
| 6 | `GET /actuator/health` de **production** respondendo `{"status":"UP"}` | [`07-production-health.png`](docs/prints/07-production-health.png) |

## Tecnologias utilizadas

- **Java 17** + **Spring Boot 3.3.4** (web, data-jpa, security, validation, actuator)
- **PostgreSQL 16** como banco de dados
- **JWT** (`io.jsonwebtoken` 0.12.3) para autenticação com papéis (ADMIN, DRIVER, USER)
- **Lombok** para reduzir boilerplate
- **H2** (apenas em testes) para rodar a suíte em memória, sem dependência de Postgres no runner de CI
- **Maven 3.9** como build tool
- **Docker** + **Docker Compose** para containerização e orquestração local
- **GitHub Actions** para CI/CD
- **Railway** como plataforma de deploy (ambientes de staging e produção)

## Checklist de entrega

| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☐ |
| Dockerfile funcional | ☑ |
| docker-compose.yml ou arquivos Kubernetes | ☑ |
| Pipeline com etapas de build, teste e deploy | ☑ |
| README.md com instruções e prints | ☑ |
| Documentação técnica com evidências (PDF ou PPT) | ☐ |
| Deploy realizado nos ambientes staging e produção | ☑ |

---

Projeto feito individualmente por **Mateus** (Grupo 28) para a atividade "Desafio DevOps" da Fase 6 do curso de ADS da FIAP, ano 2026.
