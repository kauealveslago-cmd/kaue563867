package br.com.fiap.kaue563867.dto;

import java.util.List;

public record PainelDTO(
        String bairroNome,
        long qtdNormal,
        long qtdAtencao,
        long qtdAlerta,
        List<PainelItemDTO> itens
) {
}
