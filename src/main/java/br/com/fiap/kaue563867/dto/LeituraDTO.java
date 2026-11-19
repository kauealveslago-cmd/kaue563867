package br.com.fiap.kaue563867.dto;

import java.time.LocalDateTime;

public record LeituraDTO(
        Long id,
        Long trechoId,
        String trechoDescricao,
        LocalDateTime dataHora,
        Integer laminaCm,
        String status
) {
}
