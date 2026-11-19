package br.com.fiap.kaue563867.service;

import br.com.fiap.kaue563867.domain.Leitura;
import br.com.fiap.kaue563867.domain.Trecho;
import br.com.fiap.kaue563867.dto.LeituraDTO;
import br.com.fiap.kaue563867.mapper.LeituraMapper;
import br.com.fiap.kaue563867.repository.LeituraRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeituraService {
    private static final int LIMIAR = 10;

    private final LeituraRepository repo;
    private final TrechoService trechoService;
    private final LeituraMapper mapper;

    public LeituraService(LeituraRepository repo, TrechoService trechoService, LeituraMapper mapper) {
        this.repo = repo;
        this.trechoService = trechoService;
        this.mapper = mapper;
    }

    @Transactional
    public List<LeituraDTO> listar() {
        return repo.findAllByOrderByDataHoraDesc().stream().map(leitura -> mapper.toDTO(leitura)).toList();
    }

    @Transactional
    public LeituraDTO buscarPorId(Long id) {
        return mapper.toDTO(buscarEntidade(id));
    }

    public Leitura buscarEntidade(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Leitura não encontrada");
        }
        return repo.findById(id).orElseThrow();
    }

    @Transactional
    public LeituraDTO salvar(LeituraDTO dto) {
        validar(dto);
        if (dto.id() != null) {
            buscarEntidade(dto.id());
        }
        Trecho trecho = trechoService.buscarEntidade(dto.trechoId());
        String status = calcularStatus(dto.laminaCm());
        Leitura leitura = mapper.toEntity(dto, trecho, status);
        Leitura aux = repo.save(leitura);
        return mapper.toDTO(aux);
    }

    @Transactional
    public void excluir(Long id) {
        buscarEntidade(id);
        repo.deleteById(id);
    }

    public String calcularStatus(int laminaCm) {
        if (laminaCm >= LIMIAR) {
            return "ALERTA";
        }
        if (laminaCm >= LIMIAR / 2) {
            return "ATENCAO";
        }
        return "NORMAL";
    }

    private void validar(LeituraDTO dto) {
        if (dto.trechoId() == null) {
            throw new IllegalArgumentException("Informe o trecho");
        }
        if (dto.dataHora() == null) {
            throw new IllegalArgumentException("Informe a data e a hora da leitura");
        }
        if (dto.dataHora().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data e a hora da leitura não podem estar no futuro");
        }
        if (dto.laminaCm() == null || dto.laminaCm() < 0 || dto.laminaCm() > 300) {
            throw new IllegalArgumentException("A lâmina deve estar entre 0 e 300 cm");
        }
    }
}
