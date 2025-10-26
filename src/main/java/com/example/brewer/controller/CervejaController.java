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

    // CervejaController.java

    @PostMapping("/adicionarcerveja")
    public String adicionarCerveja(
            @Valid @ModelAttribute("novaCerveja") Cerveja cerveja,
            BindingResult result,
            Model model,
            // Adiciona o Pageable, mas com PageableDefault para listar a primeira página em caso de erro
            @PageableDefault(size = 8, sort = "id") Pageable pageable) {

        if (result.hasErrors()) {
            // CORREÇÃO: Usar o método paginado e adicionar com o nome "pagina"
            Page<Cerveja> paginaCervejas = cervejaService.buscarComFiltro(null, pageable);
            model.addAttribute("pagina", paginaCervejas);

            // Também precisamos adicionar "nomeBusca" para evitar erro no rodapé de paginação
            model.addAttribute("nomeBusca", "");

            return "adicionar-cerveja"; // Retorna para a view para mostrar erros
        }

        // SE CHEGOU AQUI, DEU SUCESSO!
        cervejaService.adicionarCerveja(cerveja);

        // LINHA CRUCIAL FALTANTE: REDIRECIONA PARA O LISTAR
        return "redirect:/brewer/listar";
    }

    @GetMapping("/listar")
    public String listarCervejas(
            // 1. Recebe o termo de busca
            @RequestParam(value = "nome", required = false) String nomeBusca,
            // 2. Recebe o Pageable (padrão de 3 por página, ordenado por ID)
            @PageableDefault(size = 8, sort = "id") Pageable pageable,
            Model model) {

        // 3. Chama o Service com ambos os parâmetros. O Service decide se filtra ou não.
        Page<Cerveja> paginaCervejas = cervejaService.buscarComFiltro(nomeBusca, pageable);

        // 4. Adiciona a página completa ao Model (renomeamos para "pagina")
        model.addAttribute("pagina", paginaCervejas);

        // 5. CRUCIAL PARA RECARREGAR A BUSCA:
        // Adiciona o termo de busca atual ao Model para que os botões de paginação o usem
        model.addAttribute("nomeBusca", nomeBusca);

        // 6. Adiciona o objeto vazio para o formulário de adição
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
            // Removi o Model para simplificar, ele não é necessário aqui
            @Valid @ModelAttribute Cerveja cerveja,
            BindingResult result) {

        // Se houver erros, retorna para a página de edição (e o Spring
        // injeta o objeto 'cerveja' com os erros no Model para a view 'editar-cerveja')
        if (result.hasErrors()) {
            return "editar-cerveja";
        }

        // Se não houver erros, salva (atualiza)
        cervejaService.atualizarCerveja(cerveja);
        return "redirect:/brewer/listar";
    }



}
