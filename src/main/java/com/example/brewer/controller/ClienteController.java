package com.example.brewer.controller;

import com.example.brewer.model.Cliente;
import com.example.brewer.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}
