package com.fiap.residuos.config;

import com.fiap.residuos.entity.Usuario;
import com.fiap.residuos.enums.Role;
import com.fiap.residuos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // cria o gestor padrão na primeira execução
        if (!usuarioRepository.existsByEmail("admin@coletasmart.com")) {
            var admin = new Usuario();
            admin.setNome("Gestor Admin");
            admin.setEmail("admin@coletasmart.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setTelefone("11900000000");
            admin.setRole(Role.ROLE_ADMIN);
            usuarioRepository.save(admin);
            log.info("Admin padrão criado — email: admin@coletasmart.com");
        }
    }
}
