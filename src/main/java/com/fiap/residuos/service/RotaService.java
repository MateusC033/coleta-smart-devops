package com.fiap.residuos.service;

import com.fiap.residuos.dto.request.RotaRequestDTO;
import com.fiap.residuos.dto.response.RotaResponseDTO;
import com.fiap.residuos.entity.Caminhao;
import com.fiap.residuos.entity.Rota;
import com.fiap.residuos.entity.Usuario;
import com.fiap.residuos.exception.ResourceNotFoundException;
import com.fiap.residuos.repository.CaminhaoRepository;
import com.fiap.residuos.repository.RotaRepository;
import com.fiap.residuos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RotaService {

    private final RotaRepository rotaRepository;
    private final CaminhaoRepository caminhaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public RotaResponseDTO criar(RotaRequestDTO dto) {
        var caminhao = caminhaoRepository.findById(dto.getCaminhaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado: " + dto.getCaminhaoId()));

        var rota = new Rota();
        rota.setNome(dto.getNome());
        rota.setBairrosAtendidos(dto.getBairrosAtendidos());
        rota.setDiasSemana(dto.getDiasSemana());
        rota.setTurno(dto.getTurno());
        rota.setCaminhao(caminhao);

        rotaRepository.save(rota);
        return toResponse(rota);
    }

    @Transactional(readOnly = true)
    public List<RotaResponseDTO> listar() {
        return rotaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<RotaResponseDTO> buscarPorMeuBairro() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (usuario.getEndereco() == null) {
            throw new ResourceNotFoundException("Endereço não cadastrado para este usuário");
        }

        String bairro = usuario.getEndereco().getBairro();
        return rotaRepository.findByBairro(bairro).stream().map(this::toResponse).toList();
    }

    @Transactional
    public RotaResponseDTO atualizar(Long id, RotaRequestDTO dto) {
        Rota rota = rotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada: " + id));
        Caminhao caminhao = caminhaoRepository.findById(dto.getCaminhaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado: " + dto.getCaminhaoId()));

        rota.setNome(dto.getNome());
        rota.setBairrosAtendidos(dto.getBairrosAtendidos());
        rota.setDiasSemana(dto.getDiasSemana());
        rota.setTurno(dto.getTurno());
        rota.setCaminhao(caminhao);

        return toResponse(rota);
    }

    @Transactional
    public void deletar(Long id) {
        if (!rotaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rota não encontrada: " + id);
        }
        rotaRepository.deleteById(id);
    }

    private RotaResponseDTO toResponse(Rota r) {
        String motoristaNome = r.getCaminhao().getMotorista() != null
                ? r.getCaminhao().getMotorista().getNome() : null;
        return new RotaResponseDTO(
                r.getId(), r.getNome(), r.getBairrosAtendidos(), r.getDiasSemana(),
                r.getTurno(), r.getCaminhao().getId(), r.getCaminhao().getPlaca(), motoristaNome
        );
    }
}
