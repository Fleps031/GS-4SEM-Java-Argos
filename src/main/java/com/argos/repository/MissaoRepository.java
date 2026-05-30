package com.argos.repository;

import com.argos.model.Missao;
import com.argos.model.StatusMissao;
import com.argos.model.NivelRisco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {

    List<Missao> findByStatus(StatusMissao status);

    List<Missao> findByAreaOperacaoContainingIgnoreCase(String areaOperacao);

    List<Missao> findByNivelRisco(NivelRisco nivelRisco);

}
