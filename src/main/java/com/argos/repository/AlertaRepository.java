package com.argos.repository;

import com.argos.model.Alerta;
import com.argos.model.Severidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByMissaoIdOrderByDataAlertaDesc(Long missaoId);

    List<Alerta> findByMissaoIdAndResolvidoOrderByDataAlertaDesc(Long missaoId, Boolean resolvido);

    List<Alerta> findBySeveridadeAndResolvido(Severidade severidade, Boolean resolvido);

    long countByMissaoIdAndResolvido(Long missaoId, Boolean resolvido);
}