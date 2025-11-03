package com.example.brewer.service;

import com.example.brewer.model.Cerveja;
import com.example.brewer.model.Cliente;
import com.example.brewer.model.ItemPedido;
import com.example.brewer.model.Pedido;
import com.example.brewer.repository.ClienteRepository;
import com.example.brewer.repository.ItemPedidoRepository;
import com.example.brewer.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PedidoService {
    private PedidoRepository pedidoRepository;
    private ItemPedidoRepository itemPedidoRepository;
    private ClienteRepository clienteRepository;
    private CervejaService cervejaService;

    public Pedido criarPedido (Long clienteId){
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        if(cliente == null){
            throw new RuntimeException("CLiente não encontrado");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDate.now());
        pedido.setStatus("PENDENTE");
        pedido.setTotal(0.0);

        return pedidoRepository.save(pedido);
    }

    public void adicionarItem(Long pedidoId, Long cervejaId, Integer quantidade){
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        Cerveja cerveja;
        cerveja = cervejaService.buscarPorId(cervejaId);
        if(pedido == null){
            throw new RuntimeException("Pedido não encontrado");
        }
        if(cerveja.getQuantidadeEstoque() < quantidade){
            throw new RuntimeException("Estoque insuficiente");
        }

        //Criar novo item
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setCerveja(cerveja);
        item.setQuantidade(quantidade);
        item.setValorUnitario(cerveja.getValorVenda());
        item.setValorTotal(cerveja.getValorVenda() * quantidade);
        itemPedidoRepository.save(item);

        Double totalAtual = pedido.getTotal();
        Double valorItem = item.getValorTotal();
        Double novoTotal = (totalAtual != null ? totalAtual : 0.0) + (valorItem != null ? valorItem : 0.0);
        pedido.setTotal(novoTotal);
        pedidoRepository.save(pedido);

    }
    // Finalizar pedido (reduz estoque)
    @Transactional
    public void finalizarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }

        // Buscar itens do pedido e reduzir estoque
        List<ItemPedido> itens = itemPedidoRepository.findAll();
        for (ItemPedido item : itens) {
            if (item.getPedido().getId().equals(pedidoId)) {
                Cerveja cerveja = item.getCerveja();
                int novaQuantidade = cerveja.getQuantidadeEstoque() - item.getQuantidade();
                cerveja.setQuantidadeEstoque(novaQuantidade);
                cervejaService.atualizarCerveja(cerveja);
            }
        }

        // Atualizar status do pedido
        pedido.setStatus("CONFIRMADO");
        pedidoRepository.save(pedido);
    }

    // Listar todos os pedidos
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    // Buscar pedido por ID
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }
}
