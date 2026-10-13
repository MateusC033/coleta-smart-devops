package com.fiap.residuos.controller;

import com.fiap.residuos.dto.request.MensagemRequestDTO;
import com.fiap.residuos.dto.response.MensagemResponseDTO;
import com.fiap.residuos.service.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService mensagemService;

    @PostMapping("/alerta")
    public ResponseEntity<MensagemResponseDTO> enviarAlerta(@Valid @RequestBody MensagemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemService.enviarAlerta(dto));
    }

    @PostMapping("/contato")
    public ResponseEntity<MensagemResponseDTO> enviarContato(@Valid @RequestBody MensagemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemService.enviarContato(dto));
    }

    @GetMapping
    public ResponseEntity<List<MensagemResponseDTO>> listar() {
        return ResponseEntity.ok(mensagemService.listar());
    }
}
