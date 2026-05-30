package com.argos.service;

import com.argos.dto.LeituraSensorResponse;
import com.argos.dto.RegistrarLeituraRequest;
import com.argos.exception.MissaoNaoAtivaException;
import com.argos.model.*;
import com.argos.repository.AlertaRepository;
import com.argos.repository.LeituraSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeituraSensorService {

    private final LeituraSensorRepository leituraRepository;
    private final AlertaRepository alertaRepository;
    private final MissaoService missaoService;

    public LeituraSensorResponse registrar(RegistrarLeituraRequest request) {
        Missao missao = missaoService.buscarEntidade(request.getMissaoId());

        if (missao.getStatus() != StatusMissao.ATIVA) {
            throw new MissaoNaoAtivaException(
                    "Só é possível registrar leituras em missões com status ATIVA. " +
                            "Status atual: " + missao.getStatus());
        }

        boolean anomalia = avaliarAnomalia(request.getTipoSensor(), request.getValorLido());

        LeituraSensor leitura = LeituraSensor.builder()
                .missao(missao)
                .tipoSensor(request.getTipoSensor())
                .valorLido(request.getValorLido())
                .unidade(request.getUnidade())
                .anomalia(anomalia)
                .build();

        leituraRepository.save(leitura);

        if (anomalia) {
            gerarAlerta(missao, request.getTipoSensor(), request.getValorLido());
            recalcularRisco(missao);
        }

        return toResponse(leitura);
    }

    public List<LeituraSensorResponse> listarPorMissao(Long missaoId) {
        missaoService.buscarEntidade(missaoId);
        return leituraRepository.findByMissaoIdOrderByDataLeituraDesc(missaoId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LeituraSensorResponse> listarAnomaliasPorMissao(Long missaoId) {
        missaoService.buscarEntidade(missaoId);
        return leituraRepository.findByMissaoIdAndAnomaliaOrderByDataLeituraDesc(missaoId, true)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LeituraSensorResponse> listarPorMissaoETipoSensor(Long missaoId, TipoSensor tipoSensor) {
        missaoService.buscarEntidade(missaoId);
        return leituraRepository.findByMissaoIdAndTipoSensorOrderByDataLeituraDesc(missaoId, tipoSensor)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }


    private boolean avaliarAnomalia(TipoSensor tipo, Double valor) {
        return switch (tipo) {
            case TEMPERATURA  -> valor > 150.0;
            case PRESSAO      -> valor < 0.5 || valor > 300.0;
            case VIBRACAO     -> valor > 8.5;
            case COMBUSTIVEL  -> valor < 10.0;
            case SINAL        -> valor < -90.0;
        };
    }

    private void gerarAlerta(Missao missao, TipoSensor tipo, Double valor) {
        String mensagem = switch (tipo) {
            case TEMPERATURA -> "Temperatura crítica detectada: " + valor + "°C (limite: 150°C)";
            case PRESSAO     -> "Pressão fora do intervalo seguro: " + valor + " Bar (limite: 0.5–300 Bar)";
            case VIBRACAO    -> "Vibração excessiva detectada: " + valor + " g (limite: 8.5 g)";
            case COMBUSTIVEL -> "Nível de combustível crítico: " + valor + "% (mínimo: 10%)";
            case SINAL       -> "Sinal de telecomunicação fraco: " + valor + " dBm (mínimo: -90 dBm)";
        };

        Severidade severidade = switch (tipo) {
            case TEMPERATURA, PRESSAO, COMBUSTIVEL -> Severidade.CRITICO;
            case VIBRACAO                          -> Severidade.AVISO;
            case SINAL                             -> Severidade.AVISO;
        };

        Alerta alerta = Alerta.builder()
                .missao(missao)
                .mensagem(mensagem)
                .severidade(severidade)
                .resolvido(false)
                .build();

        alertaRepository.save(alerta);
    }

    public void recalcularRisco(Missao missao) {
        long anomaliasAbertas = alertaRepository.countByMissaoIdAndResolvido(missao.getId(), false);

        NivelRisco novoRisco;
        if (anomaliasAbertas == 0) {
            novoRisco = NivelRisco.BAIXO;
        } else if (anomaliasAbertas <= 2) {
            novoRisco = NivelRisco.MEDIO;
        } else if (anomaliasAbertas <= 5) {
            novoRisco = NivelRisco.ALTO;
        } else {
            novoRisco = NivelRisco.CRITICO;
        }

        missao.setNivelRisco(novoRisco);
        missaoService.salvar(missao);
    }

    private LeituraSensorResponse toResponse(LeituraSensor leitura) {
        return LeituraSensorResponse.builder()
                .id(leitura.getId())
                .missaoId(leitura.getMissao().getId())
                .tipoSensor(leitura.getTipoSensor())
                .valorLido(leitura.getValorLido())
                .unidade(leitura.getUnidade())
                .dataLeitura(leitura.getDataLeitura())
                .anomalia(leitura.getAnomalia())
                .build();
    }
}