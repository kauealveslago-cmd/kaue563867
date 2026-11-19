package br.com.fiap.kaue563867.dto;

public record TrechoDTO(
        Long id,
        String logradouro,
        String tipoCalcada,
        Integer altitude,
        Long bairroId,
        String bairroNome
) {
}
