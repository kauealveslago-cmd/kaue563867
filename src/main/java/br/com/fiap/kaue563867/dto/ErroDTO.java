package br.com.fiap.kaue563867.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroDTO(
        int status,
        String mensagem,
        Map<String, String> campos   // preenchido apenas nos erros de validação (400)
) {
    public ErroDTO(int status, String mensagem) {
        this(status, mensagem, null);
    }
}
