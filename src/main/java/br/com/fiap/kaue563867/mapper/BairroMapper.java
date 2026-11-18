package br.com.fiap.kaue563867.mapper;

import br.com.fiap.kaue563867.domain.Bairro;
import br.com.fiap.kaue563867.dto.BairroDTO;
import org.springframework.stereotype.Component;

@Component
public class BairroMapper {

    public Bairro toEntity(BairroDTO dto) {
        Bairro bairro = new Bairro();
        bairro.setId(dto.id());
        bairro.setNome(dto.nome());
        bairro.setZona(dto.zona());
        bairro.setPossuiPiscinao(Boolean.TRUE.equals(dto.possuiPiscinao()));
        return bairro;
    }

    public BairroDTO toDTO(Bairro bairro) {
        return new BairroDTO(
                bairro.getId(),
                bairro.getNome(),
                bairro.getZona(),
                bairro.getPossuiPiscinao()
        );
    }
}
