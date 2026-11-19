package br.com.fiap.kaue563867.service;

import br.com.fiap.kaue563867.domain.Bairro;
import br.com.fiap.kaue563867.domain.Trecho;
import br.com.fiap.kaue563867.dto.TrechoDTO;
import br.com.fiap.kaue563867.mapper.TrechoMapper;
import br.com.fiap.kaue563867.repository.LeituraRepository;
import br.com.fiap.kaue563867.repository.TrechoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrechoService {
    private static final List<String> TIPOS_CALCADA = List.of("Regular", "Irregular", "Escadaria", "Sem iluminação");

    private final TrechoRepository repo;
    private final LeituraRepository leituraRepository;
    private final BairroService bairroService;
    private final TrechoMapper mapper;

    public TrechoService(TrechoRepository repo, LeituraRepository leituraRepository,
                         BairroService bairroService, TrechoMapper mapper) {
        this.repo = repo;
        this.leituraRepository = leituraRepository;
        this.bairroService = bairroService;
        this.mapper = mapper;
    }

    @Transactional
    public List<TrechoDTO> listar() {
        return repo.findAllByOrderByBairroNomeAscLogradouroAsc().stream().map(trecho -> mapper.toDTO(trecho)).toList();
    }

    @Transactional
    public TrechoDTO buscarPorId(Long id) {
        return mapper.toDTO(buscarEntidade(id));
    }

    public Trecho buscarEntidade(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Trecho não encontrado");
        }
        return repo.findById(id).orElseThrow();
    }

    @Transactional
    public TrechoDTO salvar(TrechoDTO dto) {
        validar(dto);
        if (dto.id() != null) {
            buscarEntidade(dto.id());
        }
        Bairro bairro = bairroService.buscarEntidade(dto.bairroId());
        Trecho trecho = mapper.toEntity(dto, bairro);
        Trecho aux = repo.save(trecho);
        return mapper.toDTO(aux);
    }

    @Transactional
    public void excluir(Long id) {
        buscarEntidade(id);
        if (leituraRepository.existsByTrechoId(id)) {
            throw new IllegalArgumentException(
                    "O trecho não pode ser excluído porque possui leituras registradas");
        }
        repo.deleteById(id);
    }

    private void validar(TrechoDTO dto) {
        if (dto.logradouro() == null || dto.logradouro().isBlank()) {
            throw new IllegalArgumentException("Informe o logradouro");
        }
        if (dto.logradouro().length() > 120) {
            throw new IllegalArgumentException("O logradouro deve ter no máximo 120 caracteres");
        }
        if (dto.tipoCalcada() == null || !TIPOS_CALCADA.contains(dto.tipoCalcada())) {
            throw new IllegalArgumentException(
                    "O tipo de calçada deve ser Regular, Irregular, Escadaria ou Sem iluminação");
        }
        if (dto.altitude() == null || dto.altitude() < 0 || dto.altitude() > 2000) {
            throw new IllegalArgumentException("A altitude deve estar entre 0 e 2000 m");
        }
        if (dto.bairroId() == null) {
            throw new IllegalArgumentException("Informe o bairro");
        }
    }
}
