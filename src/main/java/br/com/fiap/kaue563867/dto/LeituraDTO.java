package br.com.fiap.kaue563867.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record LeituraDTO(
        Long id,

        @NotNull(message = "Informe o trecho.")
        Long trechoId,

        String trechoDescricao,

        @NotNull(message = "Informe a data e a hora da leitura.")
        @PastOrPresent(message = "A data e a hora da leitura não podem estar no futuro.")
        LocalDateTime dataHora,

        @NotNull(message = "Informe a lâmina de água.")
        @Min(value = 0, message = "A lâmina deve estar entre 0 e 300 cm.")
        @Max(value = 300, message = "A lâmina deve estar entre 0 e 300 cm.")
        Integer laminaCm,

        // calculado pelo LeituraService (regra R1); ignorado na entrada
        String status
) {
    public LeituraDTO comId(Long novoId) {
        return new LeituraDTO(novoId, trechoId, trechoDescricao, dataHora, laminaCm, status);
    }
}
