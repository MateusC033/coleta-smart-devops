package com.fiap.residuos.controller;

import com.fiap.residuos.dto.request.MotoristaRequestDTO;
import com.fiap.residuos.dto.response.MotoristaResponseDTO;
import com.fiap.residuos.service.MotoristaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motoristas")
@RequiredArgsConstructor
public class MotoristaController {

    private final MotoristaService motoristaService;

    @PostMapping
    public ResponseEntity<MotoristaResponseDTO> cadastrar(@Valid @RequestBody MotoristaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(motoristaService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<MotoristaResponseDTO>> listar() {
        return ResponseEntity.ok(motoristaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoristaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(motoristaService.buscarPorId(id));
    }
}
