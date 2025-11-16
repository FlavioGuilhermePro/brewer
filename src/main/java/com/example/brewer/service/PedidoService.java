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

    // ===== MÉTODO MODIFICADO =====
    @Transactional
    public void adicionarItem(Long pedidoId, Long cervejaId, Integer quantidade) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        Cerveja cerveja = cervejaService.buscarPorId(cervejaId);

        if (cerveja.getQuantidadeEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente! Disponível: " + cerveja.getQuantidadeEstoque());
        }

        // Cria o item
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setQuantidade(quantidade);

        // ===== MUDANÇA IMPORTANTE =====
        // Copia os dados da cerveja para o item (snapshot/foto do momento)
        item.copiarDadosCerveja(cerveja);

        // Calcula o total
        item.setValorTotal(item.getValorUnitario() * quantidade);

        itemPedidoRepository.save(item);

        // Atualiza o total do pedido
        pedido.setTotal(pedido.getTotal() + item.getValorTotal());
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void finalizarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível finalizar um pedido sem itens");
        }

        // Reduz o estoque
        for (ItemPedido item : pedido.getItens()) {
            // ===== MUDANÇA IMPORTANTE =====
            // Verifica se a cerveja ainda existe antes de atualizar o estoque
            if (item.getCerveja() != null) {
                Cerveja cerveja = item.getCerveja();
                int novaQuantidade = cerveja.getQuantidadeEstoque() - item.getQuantidade();

                if (novaQuantidade < 0) {
                    throw new RuntimeException("Estoque insuficiente para " + cerveja.getNome());
                }

                cerveja.setQuantidadeEstoque(novaQuantidade);
                cervejaService.atualizarCerveja(cerveja);
            }
        }

        pedido.setStatus("CONFIRMADO");
        pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Transactional
    public void excluirPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        if ("CONFIRMADO".equals(pedido.getStatus()) ||
                "EM_TRANSITO".equals(pedido.getStatus()) ||
                "ENTREGUE".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível excluir um pedido que já foi processado");
        }

        itemPedidoRepository.deleteAll(pedido.getItens());
        pedidoRepository.delete(pedido);
    }

    @Transactional
    public void atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = buscarPorId(id);

        if ("PENDENTE".equals(pedido.getStatus()) &&
                !("CONFIRMADO".equals(novoStatus) || "CANCELADO".equals(novoStatus))) {
            throw new RuntimeException("Status inválido para pedido pendente");
        }

        if ("CONFIRMADO".equals(pedido.getStatus()) &&
                !("EM_TRANSITO".equals(novoStatus) || "CANCELADO".equals(novoStatus))) {
            throw new RuntimeException("Status inválido para pedido confirmado");
        }

        if ("EM_TRANSITO".equals(pedido.getStatus()) && !("ENTREGUE".equals(novoStatus))) {
            throw new RuntimeException("Status inválido para pedido em trânsito");
        }

        // ===== MUDANÇA IMPORTANTE =====
        // Ao cancelar, devolve ao estoque (se a cerveja ainda existir)
        if ("CANCELADO".equals(novoStatus) && !"PENDENTE".equals(pedido.getStatus())) {
            for (ItemPedido item : pedido.getItens()) {
                if (item.getCerveja() != null) {
                    Cerveja cerveja = item.getCerveja();
                    cerveja.setQuantidadeEstoque(cerveja.getQuantidadeEstoque() + item.getQuantidade());
                    cervejaService.atualizarCerveja(cerveja);
                }
            }
        }

        if ("ENTREGUE".equals(novoStatus)) {
            pedido.setDataEntrega(LocalDate.now());
        }

        pedido.setStatus(novoStatus);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void atualizarPedido(Pedido pedido) {
        Pedido pedidoExistente = buscarPorId(pedido.getId());

        if ("CONFIRMADO".equals(pedidoExistente.getStatus()) ||
                "EM_TRANSITO".equals(pedidoExistente.getStatus()) ||
                "ENTREGUE".equals(pedidoExistente.getStatus())) {
            throw new RuntimeException("Não é possível editar um pedido que já foi processado");
        }

        pedidoExistente.setEnderecoEntrega(pedido.getEnderecoEntrega());
        pedidoExistente.setObservacoes(pedido.getObservacoes());

        pedidoRepository.save(pedidoExistente);
    }

    @Transactional
    public void removerItem(Long pedidoId, Long itemId) {
        Pedido pedido = buscarPorId(pedidoId);

        if ("CONFIRMADO".equals(pedido.getStatus()) ||
                "EM_TRANSITO".equals(pedido.getStatus()) ||
                "ENTREGUE".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível remover itens de um pedido que já foi processado");
        }

        ItemPedido item = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        pedido.setTotal(pedido.getTotal() - item.getValorTotal());
        pedidoRepository.save(pedido);

        itemPedidoRepository.delete(item);
    }
}