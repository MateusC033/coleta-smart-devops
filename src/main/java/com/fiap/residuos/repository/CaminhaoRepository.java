package com.fiap.residuos.repository;

import com.fiap.residuos.entity.Caminhao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaminhaoRepository extends JpaRepository<Caminhao, Long> {
}
