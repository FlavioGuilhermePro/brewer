package com.example.brewer.controller;

import com.example.brewer.model.Cliente;
import com.example.brewer.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ClienteController {
    private ClienteService clienteService;

    public String listarClientes(Model model){
        model.addAttribute("clientes",clienteService.listarClientes());
        return "listar-clientes";
    }

    public String salvarCliente(@ModelAttribute Cliente cliente){
        clienteService.salvarCliente(cliente);
        return "redirect:/cliente/listar";
    }

    public String deletarCliente(@PathVariable Long id){
        clienteService.deletarCliente(id);
        return "redirect:/cliente/listar";
    }
}
