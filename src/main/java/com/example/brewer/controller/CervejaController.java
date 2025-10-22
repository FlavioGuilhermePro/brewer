package com.example.brewer.controller;

import com.example.brewer.model.Cerveja;
import com.example.brewer.service.CervejaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequiredArgsConstructor
@RequestMapping("/brewer")
public class CervejaController {
    private final CervejaService cervejaService;
    
    @PostMapping("/adicionarcerveja")
    public String adicionarCerveja(@ModelAttribute Cerveja cerveja){
        cervejaService.adicionarCerveja(cerveja);
        return "redirect:/brewer/listar";
    }

    @GetMapping("/listar")
    public String listarCervejas(Model model){
        model.addAttribute("cervejas",cervejaService.listarCervejas());
        model.addAttribute("novaCerveja", new Cerveja());
        return "adicionar-cerveja";
    }

    @PostMapping
    public String deletarCerveja(@PathVariable Long id){
        cervejaService.deletarCerveja(id);
        return "redirect:/brewer/listar";
    }


}
