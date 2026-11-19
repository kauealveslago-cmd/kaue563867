package br.com.fiap.kaue563867.controller;

import br.com.fiap.kaue563867.dto.BairroDTO;
import br.com.fiap.kaue563867.dto.PainelItemDTO;
import br.com.fiap.kaue563867.service.BairroService;
import br.com.fiap.kaue563867.service.PainelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bairros")
public class BairroController {
    private static final List<String> ZONAS = List.of("Centro", "Norte", "Sul", "Leste", "Oeste");

    private final BairroService service;
    private final PainelService painelService;

    public BairroController(BairroService service, PainelService painelService) {
        this.service = service;
        this.painelService = painelService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("bairros", service.listar());
        return "bairros/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("bairro", new BairroDTO(null, null, null, false));
        model.addAttribute("zonas", ZONAS);
        return "bairros/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("bairro", service.buscarPorId(id));
            model.addAttribute("zonas", ZONAS);
            return "bairros/form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("bairros", service.listar());
            model.addAttribute("erro", e.getMessage());
            return "bairros/lista";
        }
    }

    @PostMapping("/salvar")
    public String salvar(BairroDTO bairro, Model model) {
        try {
            service.salvar(bairro);
            return "redirect:/bairros";
        } catch (IllegalArgumentException e) {
            model.addAttribute("bairro", bairro);
            model.addAttribute("zonas", ZONAS);
            model.addAttribute("erro", e.getMessage());
            return "bairros/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, Model model) {
        try {
            service.excluir(id);
            return "redirect:/bairros";
        } catch (IllegalArgumentException e) {
            model.addAttribute("bairros", service.listar());
            model.addAttribute("erro", e.getMessage());
            return "bairros/lista";
        }
    }

    @GetMapping("/{id}/painel")
    public String painel(@PathVariable Long id, Model model) {
        try {
            List<PainelItemDTO> itens = painelService.montarPainel(id);
            model.addAttribute("bairro", service.buscarPorId(id));
            model.addAttribute("itens", itens);
            model.addAttribute("qtdNormal", painelService.contarPorStatus(itens, "NORMAL"));
            model.addAttribute("qtdAtencao", painelService.contarPorStatus(itens, "ATENCAO"));
            model.addAttribute("qtdAlerta", painelService.contarPorStatus(itens, "ALERTA"));
            return "bairros/painel";
        } catch (IllegalArgumentException e) {
            model.addAttribute("bairros", service.listar());
            model.addAttribute("erro", e.getMessage());
            return "bairros/lista";
        }
    }
}
