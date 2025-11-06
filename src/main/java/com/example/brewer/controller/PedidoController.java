package com.example.brewer.controller;

import com.example.brewer.model.Pedido;
import com.example.brewer.service.CervejaService;
import com.example.brewer.service.ClienteService;
import com.example.brewer.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CervejaService cervejaService;

    @PostMapping("/criar")
    public String criarPedido(@RequestParam Long clienteId, Model model){
        try{
            Pedido pedido = pedidoService.criarPedido(clienteId);
            model.addAttribute("pedido", pedido);
            model.addAttribute("cervejas",cervejaService.listarCervejas());
            return "adicionar-itens";
        } catch (Exception e){
            model.addAttribute("erro", e.getMessage());
            return "redirect:/cliente/listar";
        }
    }

    @PostMapping("/adicionar-item")
    public String adicionarItem(@RequestParam Long pedidoId,@RequestParam Long cervejaId,
                                @RequestParam Integer quantidade,
                                Model model){
        try {
            pedidoService.adicionarItem(pedidoId, cervejaId, quantidade);
            Pedido pedido = pedidoService.buscarPorId(pedidoId);
            model.addAttribute("pedido", pedido);
            model.addAttribute("cervejas", cervejaService.listarCervejas());
            model.addAttribute("mensagem", "Item adicionado com sucesso!");
            return "adicionar-itens";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "adicionar-itens";
        }
    }

    @PostMapping("/finalizar")
    public String finalizarPedido(@RequestParam Long pedidoId, Model model) {
        try {
            pedidoService.finalizarPedido(pedidoId);
            model.addAttribute("mensagem", "Pedido finalizado com sucesso!");
            return "redirect:/pedido/listar";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/pedido/listar";
        }
    }

    // Listar pedidos
    @GetMapping("/listar")
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidos());
        return "listar-pedidos";
    }

    // Ver pedido (página para adicionar itens)
    @GetMapping("/adicionar-itens/{id}")
    public String verPedido(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.buscarPorId(id);
        model.addAttribute("pedido", pedido);
        model.addAttribute("cervejas", cervejaService.listarCervejas());
        return "adicionar-itens";
    }
}
