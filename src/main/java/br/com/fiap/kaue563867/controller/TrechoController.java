package br.com.fiap.kaue563867.controller;

import br.com.fiap.kaue563867.dto.TrechoDTO;
import br.com.fiap.kaue563867.service.TrechoService;
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
@RequestMapping("/api/trechos")
public class TrechoController {

    private final TrechoService trechoService;

    public TrechoController(TrechoService trechoService) {
        this.trechoService = trechoService;
    }

    @GetMapping
    public ResponseEntity<List<TrechoDTO>> listar() {
        return ResponseEntity.ok(trechoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrechoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(trechoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TrechoDTO> incluir(@Valid @RequestBody TrechoDTO dto) {
        TrechoDTO salvo = trechoService.salvar(dto.comId(null));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(salvo.id()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrechoDTO> alterar(@PathVariable Long id, @Valid @RequestBody TrechoDTO dto) {
        return ResponseEntity.ok(trechoService.salvar(dto.comId(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        trechoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
