package br.com.fiap.kaue563867.service;

import br.com.fiap.kaue563867.domain.Leitura;
import br.com.fiap.kaue563867.domain.Trecho;
import br.com.fiap.kaue563867.dto.PainelItemDTO;
import br.com.fiap.kaue563867.repository.LeituraRepository;
import br.com.fiap.kaue563867.repository.TrechoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PainelService {
    private final BairroService bairroService;
    private final TrechoRepository trechoRepository;
    private final LeituraRepository leituraRepository;

    public PainelService(BairroService bairroService, TrechoRepository trechoRepository,
                         LeituraRepository leituraRepository) {
        this.bairroService = bairroService;
        this.trechoRepository = trechoRepository;
        this.leituraRepository = leituraRepository;
    }

    public List<PainelItemDTO> montarPainel(Long bairroId) {
        bairroService.buscarEntidade(bairroId);
        return trechoRepository.findByBairroIdOrderByLogradouroAsc(bairroId).stream()
                .map(trecho -> montarItem(trecho))
                .toList();
    }

    public long contarPorStatus(List<PainelItemDTO> itens, String status) {
        return itens.stream().filter(item -> status.equals(item.ultimoStatus())).count();
    }

    private PainelItemDTO montarItem(Trecho trecho) {
        Integer ultimaLamina = null;
        String ultimoStatus = null;
        if (leituraRepository.existsByTrechoId(trecho.getId())) {
            Leitura ultima = leituraRepository.findFirstByTrechoIdOrderByDataHoraDesc(trecho.getId()).orElseThrow();
            ultimaLamina = ultima.getLaminaCm();
            ultimoStatus = ultima.getStatus();
        }
        return new PainelItemDTO(trecho.getLogradouro(), trecho.getTipoCalcada(), trecho.getAltitude(),
                ultimaLamina, ultimoStatus);
    }
}
