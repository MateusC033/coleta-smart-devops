package com.fiap.residuos.service;

import com.fiap.residuos.dto.request.MensagemRequestDTO;
import com.fiap.residuos.dto.response.MensagemResponseDTO;
import com.fiap.residuos.entity.Mensagem;
import com.fiap.residuos.entity.Rota;
import com.fiap.residuos.entity.Usuario;
import com.fiap.residuos.enums.TipoMensagem;
import com.fiap.residuos.exception.ResourceNotFoundException;
import com.fiap.residuos.repository.MensagemRepository;
import com.fiap.residuos.repository.RotaRepository;
import com.fiap.residuos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final RotaRepository rotaRepository;

    @Transactional
    public MensagemResponseDTO enviarAlerta(MensagemRequestDTO dto) {
        var remetente = obterRemetente();

        Rota rota = null;
        if (dto.getRotaId() != null) {
            rota = rotaRepository.findById(dto.getRotaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada: " + dto.getRotaId()));
        }

        var mensagem = new Mensagem();
        mensagem.setRemetente(remetente);
        mensagem.setRota(rota);
        mensagem.setConteudo(dto.getConteudo());
        mensagem.setDataHora(LocalDateTime.now());
        mensagem.setTipo(TipoMensagem.ALERTA);

        mensagemRepository.save(mensagem);
        return toResponse(mensagem);
    }

    @Transactional
    public MensagemResponseDTO enviarContato(MensagemRequestDTO dto) {
        var remetente = obterRemetente();

        Mensagem mensagem = new Mensagem();
        mensagem.setRemetente(remetente);
        mensagem.setConteudo(dto.getConteudo());
        mensagem.setDataHora(LocalDateTime.now());
        mensagem.setTipo(TipoMensagem.CONTATO);

        mensagemRepository.save(mensagem);
        return toResponse(mensagem);
    }

    @Transactional(readOnly = true)
    public List<MensagemResponseDTO> listar() {
        return mensagemRepository.findAll().stream().map(this::toResponse).toList();
    }

    private Usuario obterRemetente() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private MensagemResponseDTO toResponse(Mensagem m) {
        String nomeDaRota = m.getRota() != null ? m.getRota().getNome() : null;
        return new MensagemResponseDTO(
                m.getId(), m.getRemetente().getNome(), nomeDaRota,
                m.getConteudo(), m.getDataHora(), m.getTipo().name()
        );
    }
}
