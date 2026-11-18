package br.com.fiap.kaue563867.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TrechoDTO(
        Long id,

        @NotBlank(message = "Informe o logradouro.")
        @Size(max = 120, message = "O logradouro deve ter no máximo 120 caracteres.")
        String logradouro,

        @NotBlank(message = "Informe o tipo de calçada.")
        @Pattern(regexp = "Regular|Irregular|Escadaria|Sem iluminação",
                message = "O tipo de calçada deve ser Regular, Irregular, Escadaria ou Sem iluminação.")
        String tipoCalcada,

        @NotNull(message = "Informe a altitude.")
        @Min(value = 0, message = "A altitude deve estar entre 0 e 2000 m.")
        @Max(value = 2000, message = "A altitude deve estar entre 0 e 2000 m.")
        Integer altitude,

        @NotNull(message = "Informe o bairro.")
        Long bairroId,

        String bairroNome
) {
    public TrechoDTO comId(Long novoId) {
        return new TrechoDTO(novoId, logradouro, tipoCalcada, altitude, bairroId, bairroNome);
    }
}
