package br.com.fiap.kaue563867.service;

import br.com.fiap.kaue563867.domain.Leitura;
import br.com.fiap.kaue563867.domain.Trecho;
import br.com.fiap.kaue563867.dto.LeituraDTO;
import br.com.fiap.kaue563867.exception.RecursoNaoEncontradoException;
import br.com.fiap.kaue563867.mapper.LeituraMapper;
import br.com.fiap.kaue563867.repository.LeituraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeituraService {

    // P2: L = 10 + 2 * (1688668 % 4) = 10 cm
    public static final int LIMIAR_ALERTA = 10;

    public static final String NORMAL = "NORMAL";
    public static final String ATENCAO = "ATENCAO";
    public static final String ALERTA = "ALERTA";

    private final LeituraRepository leituraRepository;
    private final TrechoService trechoService;
    private final LeituraMapper leituraMapper;

    public LeituraService(LeituraRepository leituraRepository, TrechoService trechoService,
                          LeituraMapper leituraMapper) {
        this.leituraRepository = leituraRepository;
        this.trechoService = trechoService;
        this.leituraMapper = leituraMapper;
    }

    @Transactional(readOnly = true)
    public List<LeituraDTO> listar() {
        return leituraRepository.findAllByOrderByDataHoraDesc().stream()
                .map(leituraMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public LeituraDTO buscarPorId(Long id) {
        return leituraMapper.toDTO(buscarEntidade(id));
    }

    public Leitura buscarEntidade(Long id) {
        return leituraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Leitura de id " + id + " não encontrada."));
    }

    // R1: o status nunca vem da requisição; é sempre (re)calculado aqui
    @Transactional
    public LeituraDTO salvar(LeituraDTO dto) {
        if (dto.id() != null) {
            buscarEntidade(dto.id()); // alteração: a leitura precisa existir
        }
        Trecho trecho = trechoService.buscarEntidade(dto.trechoId());
        String status = calcularStatus(dto.laminaCm());
        Leitura salva = leituraRepository.save(leituraMapper.toEntity(dto, trecho, status));
        return leituraMapper.toDTO(salva);
    }

    @Transactional
    public void excluir(Long id) {
        leituraRepository.delete(buscarEntidade(id));
    }

    // R1: ALERTA se lâmina >= L; ATENCAO se L/2 <= lâmina < L; NORMAL se lâmina < L/2
    public String calcularStatus(int laminaCm) {
        if (laminaCm >= LIMIAR_ALERTA) {
            return ALERTA;
        }
        if (laminaCm * 2 >= LIMIAR_ALERTA) {
            return ATENCAO;
        }
        return NORMAL;
    }
}
