package com.fiap.residuos.service;

import com.fiap.residuos.dto.request.LoginRequestDTO;
import com.fiap.residuos.dto.request.RegistroRequestDTO;
import com.fiap.residuos.dto.response.TokenResponseDTO;
import com.fiap.residuos.entity.Endereco;
import com.fiap.residuos.entity.Usuario;
import com.fiap.residuos.enums.Role;
import com.fiap.residuos.repository.UsuarioRepository;
import com.fiap.residuos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public TokenResponseDTO registrar(RegistroRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setTelefone(dto.getTelefone());
        usuario.setRole(Role.ROLE_USER);

        Endereco endereco = new Endereco();
        endereco.setCep(dto.getEndereco().getCep());
        endereco.setRua(dto.getEndereco().getRua());
        endereco.setBairro(dto.getEndereco().getBairro());
        endereco.setNumero(dto.getEndereco().getNumero());
        endereco.setUsuario(usuario);

        usuario.setEndereco(endereco);
        usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtService.generateToken(userDetails);
        return new TokenResponseDTO(token, "Bearer", usuario.getNome(), usuario.getRole().name());
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtService.generateToken(userDetails);
        return new TokenResponseDTO(token, "Bearer", usuario.getNome(), usuario.getRole().name());
    }
}
