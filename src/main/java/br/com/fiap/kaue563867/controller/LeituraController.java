package br.com.fiap.kaue563867.controller;

import br.com.fiap.kaue563867.dto.LeituraDTO;
import br.com.fiap.kaue563867.service.LeituraService;
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
@RequestMapping("/api/leituras")
public class LeituraController {

    private final LeituraService leituraService;

    public LeituraController(LeituraService leituraService) {
        this.leituraService = leituraService;
    }

    @GetMapping
    public ResponseEntity<List<LeituraDTO>> listar() {
        return ResponseEntity.ok(leituraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeituraDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(leituraService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<LeituraDTO> incluir(@Valid @RequestBody LeituraDTO dto) {
        LeituraDTO salva = leituraService.salvar(dto.comId(null));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(salva.id()).toUri();
        return ResponseEntity.created(uri).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeituraDTO> alterar(@PathVariable Long id, @Valid @RequestBody LeituraDTO dto) {
        return ResponseEntity.ok(leituraService.salvar(dto.comId(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        leituraService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
