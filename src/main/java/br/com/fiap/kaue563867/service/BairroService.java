package br.com.fiap.kaue563867.service;

import br.com.fiap.kaue563867.domain.Bairro;
import br.com.fiap.kaue563867.dto.BairroDTO;
import br.com.fiap.kaue563867.exception.RecursoNaoEncontradoException;
import br.com.fiap.kaue563867.exception.RegraNegocioException;
import br.com.fiap.kaue563867.mapper.BairroMapper;
import br.com.fiap.kaue563867.repository.BairroRepository;
import br.com.fiap.kaue563867.repository.TrechoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;
    private final TrechoRepository trechoRepository;
    private final BairroMapper bairroMapper;

    public BairroService(BairroRepository bairroRepository, TrechoRepository trechoRepository,
                         BairroMapper bairroMapper) {
        this.bairroRepository = bairroRepository;
        this.trechoRepository = trechoRepository;
        this.bairroMapper = bairroMapper;
    }

    @Transactional(readOnly = true)
    public List<BairroDTO> listar() {
        return bairroRepository.findAllByOrderByNomeAsc().stream()
                .map(bairroMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public BairroDTO buscarPorId(Long id) {
        return bairroMapper.toDTO(buscarEntidade(id));
    }

    public Bairro buscarEntidade(Long id) {
        return bairroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bairro de id " + id + " não encontrado."));
    }

    @Transactional
    public BairroDTO salvar(BairroDTO dto) {
        if (dto.id() != null) {
            buscarEntidade(dto.id()); // alteração: o bairro precisa existir
        }
        Bairro salvo = bairroRepository.save(bairroMapper.toEntity(dto));
        return bairroMapper.toDTO(salvo);
    }

    // R2: um bairro que tem trechos não pode ser excluído
    @Transactional
    public void excluir(Long id) {
        Bairro bairro = buscarEntidade(id);
        if (trechoRepository.existsByBairroId(id)) {
            throw new RegraNegocioException("O bairro " + bairro.getNome()
                    + " não pode ser excluído porque possui trechos cadastrados.");
        }
        bairroRepository.delete(bairro);
    }
}
