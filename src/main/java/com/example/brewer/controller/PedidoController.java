package com.example.brewer.controller;

import com.example.brewer.model.Pedido;
import com.example.brewer.service.CervejaService;
import com.example.brewer.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private CervejaService cervejaService;

    // Cria um novo pedido para o cliente
    @PostMapping("/criar")
    public String criarPedido(@RequestParam Long clienteId, RedirectAttributes redirectAttributes) {
        try {
            // Cria o pedido vazio
            Pedido pedido = pedidoService.criarPedido(clienteId);

            // Redireciona para adicionar itens
            return "redirect:/pedido/adicionar-itens/" + pedido.getId();

        } catch (Exception e) {
            // Se der erro, mostra a mensagem e volta para clientes
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar pedido: " + e.getMessage());
            return "redirect:/cliente/listar";
        }
    }

    // Adiciona um item (cerveja) ao pedido
    @PostMapping("/adicionar-item")
    public String adicionarItem(@RequestParam Long pedidoId,
                                @RequestParam Long cervejaId,
                                @RequestParam Integer quantidade,
                                RedirectAttributes redirectAttributes) {
        try {
            // Adiciona o item
            pedidoService.adicionarItem(pedidoId, cervejaId, quantidade);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagem", "Item adicionado com sucesso!");

        } catch (Exception e) {
            // Mensagem de erro
            redirectAttributes.addFlashAttribute("erro", "Erro: " + e.getMessage());
        }

        // Volta para a página de adicionar itens
        return "redirect:/pedido/adicionar-itens/" + pedidoId;
    }

    // Finaliza o pedido (confirma e reduz estoque)
    @PostMapping("/finalizar")
    public String finalizarPedido(@RequestParam Long pedidoId, RedirectAttributes redirectAttributes) {
        try {
            // Finaliza o pedido
            pedidoService.finalizarPedido(pedidoId);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagem", "Pedido #" + pedidoId + " finalizado com sucesso!");

            // Vai para lista de pedidos
            return "redirect:/pedido/listar";

        } catch (Exception e) {
            // Se der erro, mostra mensagem e volta para adicionar itens
            redirectAttributes.addFlashAttribute("erro", "Erro ao finalizar: " + e.getMessage());
            return "redirect:/pedido/adicionar-itens/" + pedidoId;
        }
    }

    // Lista todos os pedidos
    @GetMapping("/listar")
    public String listarPedidos(Model model) {
        try {
            model.addAttribute("pedidos", pedidoService.listarPedidos());
            return "listar-pedidos";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar pedidos: " + e.getMessage());
            return "listar-pedidos";
        }
    }

    // Página para adicionar itens ao pedido
    @GetMapping("/adicionar-itens/{id}")
    public String verPedido(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Busca o pedido
            Pedido pedido = pedidoService.buscarPorId(id);

            // Verifica se o pedido já foi finalizado
            if ("CONFIRMADO".equals(pedido.getStatus()) || "EM_TRANSITO".equals(pedido.getStatus()) || "ENTREGUE".equals(pedido.getStatus())) {
                redirectAttributes.addFlashAttribute("erro", "Este pedido já foi processado!");
                return "redirect:/pedido/listar";
            }

            // Adiciona dados no modelo para a página
            model.addAttribute("pedido", pedido);
            model.addAttribute("cervejas", cervejaService.listarCervejas());

            return "adicionar-itens";

        } catch (Exception e) {
            // Se der erro (pedido não existe), volta para lista
            redirectAttributes.addFlashAttribute("erro", "Pedido não encontrado!");
            return "redirect:/pedido/listar";
        }
    }

    // Excluir um pedido
    @PostMapping("/excluir/{id}")
    public String excluirPedido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.excluirPedido(id);
            redirectAttributes.addFlashAttribute("mensagem", "Pedido #" + id + " excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir pedido: " + e.getMessage());
        }
        return "redirect:/pedido/listar";
    }

    // Formulário para editar pedido
    @GetMapping("/editar/{id}")
    public String editarPedidoForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);

            // Verifica se o pedido já foi processado
            if ("CONFIRMADO".equals(pedido.getStatus()) || "EM_TRANSITO".equals(pedido.getStatus()) || "ENTREGUE".equals(pedido.getStatus())) {
                redirectAttributes.addFlashAttribute("erro", "Não é possível editar um pedido que já foi processado!");
                return "redirect:/pedido/listar";
            }

            model.addAttribute("pedido", pedido);
            return "editar-pedido";  // Removido .html
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao carregar pedido: " + e.getMessage());
            return "redirect:/pedido/listar";
        }
    }

    // Atualizar informações do pedido
    @PostMapping("/atualizar")
    public String atualizarPedido(@ModelAttribute Pedido pedido, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.atualizarPedido(pedido);
            redirectAttributes.addFlashAttribute("mensagem", "Pedido atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar pedido: " + e.getMessage());
            return "redirect:/pedido/editar/" + pedido.getId();
        }
        return "redirect:/pedido/listar";
    }

    // Atualizar status do pedido
    @PostMapping("/atualizar-status")
    public String atualizarStatus(@RequestParam Long pedidoId,
                                  @RequestParam String status,
                                  RedirectAttributes redirectAttributes) {
        try {
            pedidoService.atualizarStatus(pedidoId, status);
            redirectAttributes.addFlashAttribute("mensagem", "Status do pedido atualizado para " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/pedido/listar";
    }

    // Remover item do pedido
    @PostMapping("/remover-item")
    public String removerItem(@RequestParam Long pedidoId,
                              @RequestParam Long itemId,
                              RedirectAttributes redirectAttributes) {
        try {
            pedidoService.removerItem(pedidoId, itemId);
            redirectAttributes.addFlashAttribute("mensagem", "Item removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover item: " + e.getMessage());
        }
        return "redirect:/pedido/adicionar-itens/" + pedidoId;
    }
}