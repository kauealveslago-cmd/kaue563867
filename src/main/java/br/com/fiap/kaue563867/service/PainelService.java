package br.com.fiap.kaue563867.service;

import br.com.fiap.kaue563867.domain.Bairro;
import br.com.fiap.kaue563867.domain.Leitura;
import br.com.fiap.kaue563867.domain.Trecho;
import br.com.fiap.kaue563867.dto.PainelDTO;
import br.com.fiap.kaue563867.dto.PainelItemDTO;
import br.com.fiap.kaue563867.repository.LeituraRepository;
import br.com.fiap.kaue563867.repository.TrechoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    // trechos do bairro em ordem de logradouro, cada um com a leitura mais recente
    @Transactional(readOnly = true)
    public List<PainelItemDTO> montarPainel(Long bairroId) {
        bairroService.buscarEntidade(bairroId);
        return trechoRepository.findByBairroIdOrderByLogradouroAsc(bairroId).stream()
                .map(this::montarItem)
                .toList();
    }

    // painel completo da API: nome do bairro, quantidade de trechos em cada status e itens
    @Transactional(readOnly = true)
    public PainelDTO gerarPainel(Long bairroId) {
        Bairro bairro = bairroService.buscarEntidade(bairroId);
        List<PainelItemDTO> itens = montarPainel(bairroId);
        return new PainelDTO(
                bairro.getNome(),
                contarPorStatus(itens, LeituraService.NORMAL),
                contarPorStatus(itens, LeituraService.ATENCAO),
                contarPorStatus(itens, LeituraService.ALERTA),
                itens
        );
    }

    private PainelItemDTO montarItem(Trecho trecho) {
        Optional<Leitura> ultima = leituraRepository.findFirstByTrechoIdOrderByDataHoraDesc(trecho.getId());
        return new PainelItemDTO(
                trecho.getLogradouro(),
                trecho.getTipoCalcada(),
                trecho.getAltitude(),
                ultima.map(Leitura::getLaminaCm).orElse(null),
                ultima.map(Leitura::getStatus).orElse(null)
        );
    }

    private long contarPorStatus(List<PainelItemDTO> itens, String status) {
        return itens.stream()
                .filter(item -> status.equals(item.ultimoStatus()))
                .count();
    }
}
