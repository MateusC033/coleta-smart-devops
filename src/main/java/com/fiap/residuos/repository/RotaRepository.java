package com.fiap.residuos.repository;

import com.fiap.residuos.entity.Rota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RotaRepository extends JpaRepository<Rota, Long> {

    @Query("SELECT r FROM Rota r WHERE LOWER(r.bairrosAtendidos) LIKE LOWER(CONCAT('%', :bairro, '%'))")
    List<Rota> findByBairro(@Param("bairro") String bairro);
}
