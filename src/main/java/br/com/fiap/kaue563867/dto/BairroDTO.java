package br.com.fiap.kaue563867.dto;

public record BairroDTO(
        Long id,
        String nome,
        String zona,
        Boolean possuiPiscinao
) {
}
