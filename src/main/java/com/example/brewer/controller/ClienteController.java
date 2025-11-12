package com.example.brewer.controller;

import com.example.brewer.model.Cliente;
import com.example.brewer.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/listar")
    public String listarClientes(Model model){
        model.addAttribute("clientes", clienteService.listarClientes());
        // CORREÇÃO: Adicionar objeto vazio para o formulário
        model.addAttribute("novoCliente", new Cliente());
        return "listar-clientes";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente){
        clienteService.salvarCliente(cliente);
        return "redirect:/cliente/listar";
    }

    @PostMapping("/deletar/{id}")
    public String deletarCliente(@PathVariable Long id){
        clienteService.deletarCliente(id);
        return "redirect:/cliente/listar";
    }
    // ... outros métodos

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        return "editar-cliente"; // Nome da nova página HTML
    }

    @PostMapping("/atualizar")
    public String atualizarCliente(@Valid @ModelAttribute Cliente cliente, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Se houver erros, retorna para a página de edição
            return "editar-cliente";
        }
        try {
            clienteService.atualizarCliente(cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar cliente: " + e.getMessage());
        }
        return "redirect:/cliente/listar";
    }
}

