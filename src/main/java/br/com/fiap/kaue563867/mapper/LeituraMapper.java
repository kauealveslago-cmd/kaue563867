package br.com.fiap.kaue563867.mapper;

import br.com.fiap.kaue563867.domain.Leitura;
import br.com.fiap.kaue563867.domain.Trecho;
import br.com.fiap.kaue563867.dto.LeituraDTO;
import org.springframework.stereotype.Component;

@Component
public class LeituraMapper {

    public Leitura toEntity(LeituraDTO dto, Trecho trecho, String status) {
        Leitura leitura = new Leitura();
        leitura.setId(dto.id());
        leitura.setDataHora(dto.dataHora());
        leitura.setLaminaCm(dto.laminaCm());
        leitura.setStatus(status);
        leitura.setTrecho(trecho);
        return leitura;
    }

    public LeituraDTO toDTO(Leitura leitura) {
        Trecho trecho = leitura.getTrecho();
        return new LeituraDTO(
                leitura.getId(),
                trecho.getId(),
                trecho.getLogradouro() + " (" + trecho.getBairro().getNome() + ")",
                leitura.getDataHora(),
                leitura.getLaminaCm(),
                leitura.getStatus()
        );
    }
}
