package com.argos.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter @Builder
public class ResumoDashboardResponse {
    private long totalMissoes;
    private Map<String, Long> porStatus;
    private Map<String, Long> porArea;
    private List<MissaoResponse> missoesCriticas;
}