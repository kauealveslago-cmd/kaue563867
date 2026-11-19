package br.com.fiap.kaue563867.controller;

import br.com.fiap.kaue563867.dto.TrechoDTO;
import br.com.fiap.kaue563867.service.BairroService;
import br.com.fiap.kaue563867.service.TrechoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trechos")
public class TrechoController {
    private static final List<String> TIPOS_CALCADA = List.of("Regular", "Irregular", "Escadaria", "Sem iluminação");

    private final TrechoService service;
    private final BairroService bairroService;

    public TrechoController(TrechoService service, BairroService bairroService) {
        this.service = service;
        this.bairroService = bairroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("trechos", service.listar());
        return "trechos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("trecho", new TrechoDTO(null, null, null, null, null, null));
        carregarListas(model);
        return "trechos/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("trecho", service.buscarPorId(id));
            carregarListas(model);
            return "trechos/form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("trechos", service.listar());
            model.addAttribute("erro", e.getMessage());
            return "trechos/lista";
        }
    }

    @PostMapping("/salvar")
    public String salvar(TrechoDTO trecho, Model model) {
        try {
            service.salvar(trecho);
            return "redirect:/trechos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("trecho", trecho);
            carregarListas(model);
            model.addAttribute("erro", e.getMessage());
            return "trechos/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, Model model) {
        try {
            service.excluir(id);
            return "redirect:/trechos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("trechos", service.listar());
            model.addAttribute("erro", e.getMessage());
            return "trechos/lista";
        }
    }

    private void carregarListas(Model model) {
        model.addAttribute("bairros", bairroService.listar());
        model.addAttribute("tiposCalcada", TIPOS_CALCADA);
    }
}
