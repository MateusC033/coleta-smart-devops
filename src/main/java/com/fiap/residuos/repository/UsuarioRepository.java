package com.fiap.residuos.repository;

import com.fiap.residuos.entity.Usuario;
import com.fiap.residuos.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRole(Role role);
    boolean existsByEmail(String email);
}
