package com.fiap.residuos.repository;

import com.fiap.residuos.entity.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByRotaId(Long rotaId);
}
