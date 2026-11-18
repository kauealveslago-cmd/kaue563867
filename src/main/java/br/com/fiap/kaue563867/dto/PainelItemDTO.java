package br.com.fiap.kaue563867.dto;

public record PainelItemDTO(
        String logradouro,
        String tipoCalcada,
        Integer altitude,
        Integer ultimaLamina,   // null quando o trecho ainda não tem leitura
        String ultimoStatus     // null quando o trecho ainda não tem leitura
) {
}
