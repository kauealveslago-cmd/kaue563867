package br.com.fiap.kaue563867.service;

import br.com.fiap.kaue563867.domain.Bairro;
import br.com.fiap.kaue563867.dto.BairroDTO;
import br.com.fiap.kaue563867.mapper.BairroMapper;
import br.com.fiap.kaue563867.repository.BairroRepository;
import br.com.fiap.kaue563867.repository.TrechoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BairroService {
    private static final List<String> ZONAS = List.of("Centro", "Norte", "Sul", "Leste", "Oeste");

    private final BairroRepository repo;
    private final TrechoRepository trechoRepository;
    private final BairroMapper mapper;

    public BairroService(BairroRepository repo, TrechoRepository trechoRepository, BairroMapper mapper) {
        this.repo = repo;
        this.trechoRepository = trechoRepository;
        this.mapper = mapper;
    }

    public List<BairroDTO> listar() {
        return repo.findAllByOrderByNomeAsc().stream().map(bairro -> mapper.toDTO(bairro)).toList();
    }

    public BairroDTO buscarPorId(Long id) {
        return mapper.toDTO(buscarEntidade(id));
    }

    public Bairro buscarEntidade(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Bairro não encontrado");
        }
        return repo.findById(id).orElseThrow();
    }

    @Transactional
    public BairroDTO salvar(BairroDTO dto) {
        validar(dto);
        if (dto.id() != null) {
            buscarEntidade(dto.id());
        }
        Bairro bairro = mapper.toEntity(dto);
        if (bairro.getPossuiPiscinao() == null) {
            bairro.setPossuiPiscinao(false);
        }
        Bairro aux = repo.save(bairro);
        return mapper.toDTO(aux);
    }

    @Transactional
    public void excluir(Long id) {
        buscarEntidade(id);
        if (trechoRepository.existsByBairroId(id)) {
            throw new IllegalArgumentException(
                    "O bairro não pode ser excluído porque possui trechos cadastrados");
        }
        repo.deleteById(id);
    }

    private void validar(BairroDTO dto) {
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome do bairro");
        }
        if (dto.nome().length() > 80) {
            throw new IllegalArgumentException("O nome do bairro deve ter no máximo 80 caracteres");
        }
        if (dto.zona() == null || !ZONAS.contains(dto.zona())) {
            throw new IllegalArgumentException("A zona deve ser Centro, Norte, Sul, Leste ou Oeste");
        }
    }
}
