package br.com.fiap.kaue563867.service;

import br.com.fiap.kaue563867.domain.Bairro;
import br.com.fiap.kaue563867.domain.Trecho;
import br.com.fiap.kaue563867.dto.TrechoDTO;
import br.com.fiap.kaue563867.exception.RecursoNaoEncontradoException;
import br.com.fiap.kaue563867.exception.RegraNegocioException;
import br.com.fiap.kaue563867.mapper.TrechoMapper;
import br.com.fiap.kaue563867.repository.LeituraRepository;
import br.com.fiap.kaue563867.repository.TrechoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrechoService {

    private final TrechoRepository trechoRepository;
    private final LeituraRepository leituraRepository;
    private final BairroService bairroService;
    private final TrechoMapper trechoMapper;

    public TrechoService(TrechoRepository trechoRepository, LeituraRepository leituraRepository,
                         BairroService bairroService, TrechoMapper trechoMapper) {
        this.trechoRepository = trechoRepository;
        this.leituraRepository = leituraRepository;
        this.bairroService = bairroService;
        this.trechoMapper = trechoMapper;
    }

    // P4 = 2: ordenado pelo nome do bairro e, no empate, pelo logradouro
    @Transactional(readOnly = true)
    public List<TrechoDTO> listar() {
        return trechoRepository.findAllByOrderByBairroNomeAscLogradouroAsc().stream()
                .map(trechoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TrechoDTO buscarPorId(Long id) {
        return trechoMapper.toDTO(buscarEntidade(id));
    }

    public Trecho buscarEntidade(Long id) {
        return trechoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Trecho de id " + id + " não encontrado."));
    }

    @Transactional
    public TrechoDTO salvar(TrechoDTO dto) {
        if (dto.id() != null) {
            buscarEntidade(dto.id()); // alteração: o trecho precisa existir
        }
        Bairro bairro = bairroService.buscarEntidade(dto.bairroId());
        Trecho salvo = trechoRepository.save(trechoMapper.toEntity(dto, bairro));
        return trechoMapper.toDTO(salvo);
    }

    // R3 com P5 = 0: trecho com leituras não pode ser excluído
    @Transactional
    public void excluir(Long id) {
        Trecho trecho = buscarEntidade(id);
        if (leituraRepository.existsByTrechoId(id)) {
            throw new RegraNegocioException("O trecho " + trecho.getLogradouro()
                    + " não pode ser excluído porque possui leituras registradas.");
        }
        trechoRepository.delete(trecho);
    }
}
