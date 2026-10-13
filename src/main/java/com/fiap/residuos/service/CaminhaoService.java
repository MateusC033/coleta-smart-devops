package com.fiap.residuos.service;

import com.fiap.residuos.dto.request.CaminhaoRequestDTO;
import com.fiap.residuos.dto.response.CaminhaoResponseDTO;
import com.fiap.residuos.entity.Caminhao;
import com.fiap.residuos.entity.Usuario;
import com.fiap.residuos.enums.Role;
import com.fiap.residuos.exception.ResourceNotFoundException;
import com.fiap.residuos.repository.CaminhaoRepository;
import com.fiap.residuos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaminhaoService {

    private final CaminhaoRepository caminhaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CaminhaoResponseDTO cadastrar(CaminhaoRequestDTO dto) {
        Caminhao caminhao = new Caminhao();
        caminhao.setPlaca(dto.getPlaca());
        caminhao.setCapacidade(dto.getCapacidade());
        vincularMotorista(caminhao, dto.getMotoristaId());
        caminhaoRepository.save(caminhao);
        return toResponse(caminhao);
    }

    @Transactional(readOnly = true)
    public List<CaminhaoResponseDTO> listar() {
        return caminhaoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CaminhaoResponseDTO buscarPorId(Long id) {
        return caminhaoRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado: " + id));
    }

    @Transactional
    public CaminhaoResponseDTO atualizar(Long id, CaminhaoRequestDTO dto) {
        Caminhao caminhao = caminhaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado: " + id));
        caminhao.setPlaca(dto.getPlaca());
        caminhao.setCapacidade(dto.getCapacidade());
        vincularMotorista(caminhao, dto.getMotoristaId());
        return toResponse(caminhao);
    }

    @Transactional
    public void deletar(Long id) {
        if (!caminhaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Caminhão não encontrado: " + id);
        }
        caminhaoRepository.deleteById(id);
    }

    private void vincularMotorista(Caminhao caminhao, Long motoristaId) {
        if (motoristaId == null) {
            caminhao.setMotorista(null);
            return;
        }
        // garante que o id informado é de um motorista, não de outro tipo de usuário
        var motorista = usuarioRepository.findById(motoristaId)
                .filter(u -> u.getRole() == Role.ROLE_DRIVER)
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado: " + motoristaId));
        caminhao.setMotorista(motorista);
    }

    private CaminhaoResponseDTO toResponse(Caminhao c) {
        Long motoristaId = c.getMotorista() != null ? c.getMotorista().getId() : null;
        String motoristaNome = c.getMotorista() != null ? c.getMotorista().getNome() : null;
        return new CaminhaoResponseDTO(c.getId(), c.getPlaca(), c.getCapacidade(), motoristaId, motoristaNome);
    }
}
