package com.argos.repository;

import com.argos.model.LeituraSensor;
import com.argos.model.TipoSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {

    List<LeituraSensor> findByMissaoIdOrderByDataLeituraDesc(Long missaoId);

    List<LeituraSensor> findByMissaoIdAndAnomaliaOrderByDataLeituraDesc(Long missaoId, Boolean anomalia);

    List<LeituraSensor> findByMissaoIdAndTipoSensorOrderByDataLeituraDesc(Long missaoId, TipoSensor tipoSensor);
}