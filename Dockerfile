# syntax=docker/dockerfile:1.7

# ================================================
# Stage 1 — build
# Compila o JAR com Maven dentro do próprio container,
# para que o host não precise ter Maven instalado.
# ================================================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Baixa as dependências primeiro para aproveitar o cache de camadas:
# enquanto o pom.xml não mudar, esta etapa não é refeita.
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ================================================
# Stage 2 — runtime
# Imagem enxuta só com a JRE e o JAR final.
# ================================================
FROM eclipse-temurin:17-jre-alpine

# Usuário não-root para rodar a aplicação
RUN addgroup -S app && adduser -S -G app app

WORKDIR /app

# Nome do JAR vem de <finalName>app</finalName> no pom.xml
COPY --from=build --chown=app:app /app/target/app.jar ./app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
