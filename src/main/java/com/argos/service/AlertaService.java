package com.argos.service;

import com.argos.dto.AlertaResponse;
import com.argos.exception.AlertaJaResolvidoException;
import com.argos.exception.RecursoNaoEncontradoException;
import com.argos.model.Alerta;
import com.argos.model.Severidade;
import com.argos.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final MissaoService missaoService;
    private final LeituraSensorService leituraService;

    public List<AlertaResponse> listarPorMissao(Long missaoId) {
        missaoService.buscarEntidade(missaoId);
        return alertaRepository.findByMissaoIdOrderByDataAlertaDesc(missaoId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AlertaResponse> listarPendentesPorMissao(Long missaoId) {
        missaoService.buscarEntidade(missaoId);
        return alertaRepository.findByMissaoIdAndResolvidoOrderByDataAlertaDesc(missaoId, false)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public AlertaResponse resolver(Long alertaId) {
        Alerta alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Alerta com id " + alertaId + " não encontrado"));

        if (alerta.getResolvido()) {
            throw new AlertaJaResolvidoException(
                    "Alerta já foi resolvido anteriormente");
        }

        alerta.setResolvido(true);
        alertaRepository.save(alerta);

        if (alerta.getSeveridade() == Severidade.CRITICO) {
            leituraService.recalcularRisco(alerta.getMissao());
        }

        return toResponse(alerta);
    }

    public List<AlertaResponse> listarCriticos() {
        return alertaRepository.findBySeveridadeAndResolvido(Severidade.CRITICO, false)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private AlertaResponse toResponse(Alerta alerta) {
        return AlertaResponse.builder()
                .id(alerta.getId())
                .missaoId(alerta.getMissao().getId())
                .mensagem(alerta.getMensagem())
                .severidade(alerta.getSeveridade())
                .resolvido(alerta.getResolvido())
                .dataAlerta(alerta.getDataAlerta())
                .build();
    }
}