package br.com.fiap.kaue563867.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BairroDTO(
        Long id,

        @NotBlank(message = "Informe o nome do bairro.")
        @Size(max = 80, message = "O nome do bairro deve ter no máximo 80 caracteres.")
        String nome,

        @NotBlank(message = "Informe a zona do bairro.")
        @Pattern(regexp = "Centro|Norte|Sul|Leste|Oeste",
                message = "A zona deve ser Centro, Norte, Sul, Leste ou Oeste.")
        String zona,

        Boolean possuiPiscinao
) {
    public BairroDTO comId(Long novoId) {
        return new BairroDTO(novoId, nome, zona, possuiPiscinao);
    }
}
