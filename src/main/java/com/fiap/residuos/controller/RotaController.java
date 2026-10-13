package com.fiap.residuos.controller;

import com.fiap.residuos.dto.request.RotaRequestDTO;
import com.fiap.residuos.dto.response.RotaResponseDTO;
import com.fiap.residuos.service.RotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rotas")
@RequiredArgsConstructor
public class RotaController {

    private final RotaService rotaService;

    @PostMapping
    public ResponseEntity<RotaResponseDTO> criar(@Valid @RequestBody RotaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rotaService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<RotaResponseDTO>> listar() {
        return ResponseEntity.ok(rotaService.listar());
    }

    @GetMapping("/meu-bairro")
    public ResponseEntity<List<RotaResponseDTO>> buscarPorMeuBairro() {
        return ResponseEntity.ok(rotaService.buscarPorMeuBairro());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RotaResponseDTO> atualizar(@PathVariable Long id,
                                                      @Valid @RequestBody RotaRequestDTO dto) {
        return ResponseEntity.ok(rotaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        rotaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
