package com.argos.service;

import com.argos.dto.*;
import com.argos.exception.OperacaoInvalidaException;
import com.argos.exception.RecursoNaoEncontradoException;
import com.argos.exception.TransicaoStatusInvalidaException;
import com.argos.model.Missao;
import com.argos.model.NivelRisco;
import com.argos.model.StatusMissao;
import com.argos.repository.MissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissaoService {

    private final MissaoRepository missaoRepository;

    public MissaoResponse criar(CriarMissaoRequest request) {
        Missao missao = Missao.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .areaOperacao(request.getAreaOperacao())
                .status(StatusMissao.PLANEJADA)
                .nivelRisco(NivelRisco.BAIXO)
                .build();

        return toResponse(missaoRepository.save(missao));
    }

    public List<MissaoResponse> listarTodos(StatusMissao status) {
        List<Missao> missoes = (status != null)
                ? missaoRepository.findByStatus(status)
                : missaoRepository.findAll();

        return missoes.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public MissaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public List<MissaoResponse> buscarPorArea(String area) {
        return missaoRepository.findByAreaOperacaoContainingIgnoreCase(area)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public MissaoResponse atualizar(Long id, AtualizarMissaoRequest request) {
        Missao missao = buscarEntidade(id);

        if (missao.getStatus() == StatusMissao.CANCELADA) {
            throw new OperacaoInvalidaException(
                "Não é possível atualizar uma missão com status CANCELADA");
        }

        missao.setNome(request.getNome());
        missao.setDescricao(request.getDescricao());
        missao.setAreaOperacao(request.getAreaOperacao());

        return toResponse(missaoRepository.save(missao));
    }

    public MissaoResponse atualizarStatus(Long id, AtualizarStatusRequest request) {
        Missao missao = buscarEntidade(id);
        validarTransicao(missao.getStatus(), request.getStatus());
        missao.setStatus(request.getStatus());
        return toResponse(missaoRepository.save(missao));
    }

    public void excluir(Long id) {
        Missao missao = buscarEntidade(id);

        if (missao.getStatus() == StatusMissao.ATIVA) {
            throw new OperacaoInvalidaException(
                "Não é possível excluir uma missão com status ATIVA");
        }

        missaoRepository.delete(missao);
    }

    public ResumoDashboardResponse gerarResumo() {
        List<Missao> todas = missaoRepository.findAll();

        Map<String, Long> porStatus = todas.stream()
                .collect(Collectors.groupingBy(
                    m -> m.getStatus().name(), Collectors.counting()));

        Map<String, Long> porArea = todas.stream()
                .collect(Collectors.groupingBy(
                    Missao::getAreaOperacao, Collectors.counting()));

        List<MissaoResponse> criticas = todas.stream()
                .filter(m -> m.getNivelRisco() == NivelRisco.ALTO
                          || m.getNivelRisco() == NivelRisco.CRITICO)
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResumoDashboardResponse.builder()
                .totalMissoes(todas.size())
                .porStatus(porStatus)
                .porArea(porArea)
                .missoesCriticas(criticas)
                .build();
    }

    public Missao buscarEntidade(Long id) {
        return missaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                    "Missão com id " + id + " não encontrada"));
    }

    public void salvar(Missao missao) {
        missaoRepository.save(missao);
    }

    private void validarTransicao(StatusMissao atual, StatusMissao novo) {
        boolean valida = switch (atual) {
            case PLANEJADA -> novo == StatusMissao.ATIVA || novo == StatusMissao.CANCELADA;
            case ATIVA     -> novo == StatusMissao.CONCLUIDA || novo == StatusMissao.CANCELADA;
            case CONCLUIDA, CANCELADA -> false;
        };

        if (!valida) {
            throw new TransicaoStatusInvalidaException(
                "Transição de status inválida: " + atual + " → " + novo);
        }
    }

    public MissaoResponse toResponse(Missao missao) {
        return MissaoResponse.builder()
                .id(missao.getId())
                .nome(missao.getNome())
                .descricao(missao.getDescricao())
                .status(missao.getStatus())
                .nivelRisco(missao.getNivelRisco())
                .areaOperacao(missao.getAreaOperacao())
                .dataCriacao(missao.getDataCriacao())
                .dataAtualizacao(missao.getDataAtualizacao())
                .build();
    }
}