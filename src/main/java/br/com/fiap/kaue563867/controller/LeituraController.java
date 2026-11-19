package br.com.fiap.kaue563867.controller;

import br.com.fiap.kaue563867.dto.LeituraDTO;
import br.com.fiap.kaue563867.service.LeituraService;
import br.com.fiap.kaue563867.service.TrechoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/leituras")
public class LeituraController {
    private final LeituraService service;
    private final TrechoService trechoService;

    public LeituraController(LeituraService service, TrechoService trechoService) {
        this.service = service;
        this.trechoService = trechoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("leituras", service.listar());
        return "leituras/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("leitura", new LeituraDTO(null, null, null, null, null, null));
        model.addAttribute("trechos", trechoService.listar());
        return "leituras/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("leitura", service.buscarPorId(id));
            model.addAttribute("trechos", trechoService.listar());
            return "leituras/form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("leituras", service.listar());
            model.addAttribute("erro", e.getMessage());
            return "leituras/lista";
        }
    }

    @PostMapping("/salvar")
    public String salvar(LeituraDTO leitura, Model model) {
        try {
            service.salvar(leitura);
            return "redirect:/leituras";
        } catch (IllegalArgumentException e) {
            model.addAttribute("leitura", leitura);
            model.addAttribute("trechos", trechoService.listar());
            model.addAttribute("erro", e.getMessage());
            return "leituras/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, Model model) {
        try {
            service.excluir(id);
            return "redirect:/leituras";
        } catch (IllegalArgumentException e) {
            model.addAttribute("leituras", service.listar());
            model.addAttribute("erro", e.getMessage());
            return "leituras/lista";
        }
    }
}
