package com.example.brewer.service;

import com.example.brewer.model.Cerveja;
import com.example.brewer.model.Cliente;
import com.example.brewer.model.ItemPedido;
import com.example.brewer.model.Pedido;
import com.example.brewer.repository.ClienteRepository;
import com.example.brewer.repository.ItemPedidoRepository;
import com.example.brewer.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CervejaService cervejaService;

    // Cria um pedido novo vazio para um cliente
    @Transactional
    public Pedido criarPedido(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDate.now());
        pedido.setStatus("PENDENTE");
        pedido.setTotal(0.0);

        return pedidoRepository.save(pedido);
    }

    // Adiciona um item (cerveja) ao pedido
    @Transactional
    public void adicionarItem(Long pedidoId, Long cervejaId, Integer quantidade) {
        // Busca o pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Busca a cerveja
        Cerveja cerveja = cervejaService.buscarPorId(cervejaId);

        // Verifica se tem estoque suficiente
        if (cerveja.getQuantidadeEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente! Disponível: " + cerveja.getQuantidadeEstoque());
        }

        // Cria o item do pedido
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setCerveja(cerveja);
        item.setQuantidade(quantidade);
        item.setValorUnitario(cerveja.getValorVenda());
        item.setValorTotal(cerveja.getValorVenda() * quantidade);

        // Salva o item
        itemPedidoRepository.save(item);

        // Atualiza o total do pedido
        pedido.setTotal(pedido.getTotal() + item.getValorTotal());
        pedidoRepository.save(pedido);
    }

    // Finaliza o pedido e reduz o estoque
    @Transactional
    public void finalizarPedido(Long pedidoId) {
        // Busca o pedido (com os itens já carregados por causa do EAGER)
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Verifica se o pedido tem itens
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível finalizar um pedido sem itens");
        }

        // Para cada item, reduz o estoque da cerveja
        for (ItemPedido item : pedido.getItens()) {
            Cerveja cerveja = item.getCerveja();
            int novaQuantidade = cerveja.getQuantidadeEstoque() - item.getQuantidade();

            // Verifica se ainda tem estoque (segurança dupla)
            if (novaQuantidade < 0) {
                throw new RuntimeException("Estoque insuficiente para " + cerveja.getNome());
            }

            // Atualiza o estoque
            cerveja.setQuantidadeEstoque(novaQuantidade);
            cervejaService.atualizarCerveja(cerveja);
        }

        // Muda o status do pedido para CONFIRMADO
        pedido.setStatus("CONFIRMADO");
        pedidoRepository.save(pedido);
    }

    // Lista todos os pedidos
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    // Busca um pedido específico por ID
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}