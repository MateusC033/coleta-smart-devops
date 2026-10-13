package com.fiap.residuos.service;

import com.fiap.residuos.dto.request.MotoristaRequestDTO;
import com.fiap.residuos.dto.response.MotoristaResponseDTO;
import com.fiap.residuos.entity.Usuario;
import com.fiap.residuos.enums.Role;
import com.fiap.residuos.exception.ResourceNotFoundException;
import com.fiap.residuos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotoristaService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MotoristaResponseDTO cadastrar(MotoristaRequestDTO dto) {
        var novoMotorista = new Usuario();
        novoMotorista.setNome(dto.getNome());
        novoMotorista.setEmail(dto.getEmail());
        novoMotorista.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoMotorista.setTelefone(dto.getTelefone());
        novoMotorista.setRole(Role.ROLE_DRIVER);
        usuarioRepository.save(novoMotorista);
        return toResponse(novoMotorista);
    }

    @Transactional(readOnly = true)
    public List<MotoristaResponseDTO> listar() {
        return usuarioRepository.findByRole(Role.ROLE_DRIVER).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MotoristaResponseDTO buscarPorId(Long id) {
        Usuario motorista = usuarioRepository.findById(id)
                .filter(u -> u.getRole() == Role.ROLE_DRIVER)
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado: " + id));
        return toResponse(motorista);
    }

    private MotoristaResponseDTO toResponse(Usuario u) {
        return new MotoristaResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getTelefone());
    }
}
