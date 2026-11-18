package br.com.fiap.kaue563867.controller;

import br.com.fiap.kaue563867.dto.BairroDTO;
import br.com.fiap.kaue563867.dto.PainelDTO;
import br.com.fiap.kaue563867.service.BairroService;
import br.com.fiap.kaue563867.service.PainelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bairros")
public class BairroController {

    private final BairroService bairroService;
    private final PainelService painelService;

    public BairroController(BairroService bairroService, PainelService painelService) {
        this.bairroService = bairroService;
        this.painelService = painelService;
    }

    @GetMapping
    public ResponseEntity<List<BairroDTO>> listar() {
        return ResponseEntity.ok(bairroService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BairroDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bairroService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<BairroDTO> incluir(@Valid @RequestBody BairroDTO dto) {
        BairroDTO salvo = bairroService.salvar(dto.comId(null));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(salvo.id()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BairroDTO> alterar(@PathVariable Long id, @Valid @RequestBody BairroDTO dto) {
        return ResponseEntity.ok(bairroService.salvar(dto.comId(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bairroService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/painel")
    public ResponseEntity<PainelDTO> painel(@PathVariable Long id) {
        return ResponseEntity.ok(painelService.gerarPainel(id));
    }
}
