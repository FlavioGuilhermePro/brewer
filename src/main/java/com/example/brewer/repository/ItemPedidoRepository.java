package com.example.brewer.repository;

import com.example.brewer.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    // Método para buscar todos os itens de um pedido específico
    // Útil caso precise fazer consultas mais complexas no futuro
    List<ItemPedido> findByPedidoId(Long pedidoId);
}