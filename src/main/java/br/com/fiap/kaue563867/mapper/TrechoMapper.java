package br.com.fiap.kaue563867.mapper;

import br.com.fiap.kaue563867.domain.Bairro;
import br.com.fiap.kaue563867.domain.Trecho;
import br.com.fiap.kaue563867.dto.TrechoDTO;
import org.springframework.stereotype.Component;

@Component
public class TrechoMapper {

    public Trecho toEntity(TrechoDTO dto, Bairro bairro) {
        Trecho trecho = new Trecho();
        trecho.setId(dto.id());
        trecho.setLogradouro(dto.logradouro());
        trecho.setTipoCalcada(dto.tipoCalcada());
        trecho.setAltitude(dto.altitude());
        trecho.setBairro(bairro);
        return trecho;
    }

    public TrechoDTO toDTO(Trecho trecho) {
        return new TrechoDTO(
                trecho.getId(),
                trecho.getLogradouro(),
                trecho.getTipoCalcada(),
                trecho.getAltitude(),
                trecho.getBairro().getId(),
                trecho.getBairro().getNome()
        );
    }
}
