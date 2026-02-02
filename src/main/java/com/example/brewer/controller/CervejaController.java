package com.example.brewer.controller;

import com.example.brewer.model.Cerveja;
import com.example.brewer.service.CervejaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/brewer")
public class CervejaController {
    private final CervejaService cervejaService;



    @PostMapping("/adicionarcerveja")
    public String adicionarCerveja(
            @Valid @ModelAttribute("novaCerveja") Cerveja cerveja,
            BindingResult result,
            Model model,

            @PageableDefault(size = 8, sort = "id") Pageable pageable) {

        if (result.hasErrors()) {

            Page<Cerveja> paginaCervejas = cervejaService.buscarComFiltro(null, pageable);
            model.addAttribute("pagina", paginaCervejas);


            model.addAttribute("nomeBusca", "");

            return "adicionar-cerveja";
        }


        cervejaService.adicionarCerveja(cerveja);


        return "redirect:/brewer/listar";
    }

    @GetMapping("/listar")
    public String listarCervejas(

            @RequestParam(value = "nome", required = false) String nomeBusca,

            @PageableDefault(size = 8, sort = "id") Pageable pageable,
            Model model) {


        Page<Cerveja> paginaCervejas = cervejaService.buscarComFiltro(nomeBusca, pageable);


        model.addAttribute("pagina", paginaCervejas);


        model.addAttribute("nomeBusca", nomeBusca);

        model.addAttribute("novaCerveja", new Cerveja());

        return "adicionar-cerveja";
    }

    @PostMapping("/listar/deletar/{id}")
    public String deletarCerveja(@PathVariable Long id){
        cervejaService.deletarCerveja(id);
        return "redirect:/brewer/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarCerveja(@PathVariable Long id, Model model){
        Cerveja cerveja = cervejaService.buscarPorId(id);
        model.addAttribute("cerveja",cerveja);
        return "editar-cerveja";
    }

    @PostMapping("/atualizar")
    public String atualizarCerveja(

            @Valid @ModelAttribute Cerveja cerveja,
            BindingResult result) {


        if (result.hasErrors()) {
            return "editar-cerveja";
        }

        cervejaService.atualizarCerveja(cerveja);
        return "redirect:/brewer/listar";
    }



}
