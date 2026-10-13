package com.fiap.residuos.controller;

import com.fiap.residuos.dto.request.CaminhaoRequestDTO;
import com.fiap.residuos.dto.response.CaminhaoResponseDTO;
import com.fiap.residuos.service.CaminhaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caminhoes")
@RequiredArgsConstructor
public class CaminhaoController {

    private final CaminhaoService caminhaoService;

    @PostMapping
    public ResponseEntity<CaminhaoResponseDTO> cadastrar(@Valid @RequestBody CaminhaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(caminhaoService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<CaminhaoResponseDTO>> listar() {
        return ResponseEntity.ok(caminhaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaminhaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caminhaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaminhaoResponseDTO> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody CaminhaoRequestDTO dto) {
        return ResponseEntity.ok(caminhaoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        caminhaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
