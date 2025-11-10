package com.example.brewer.repository;

import com.example.brewer.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    // Método para buscar todos os itens de um pedido específico
    // Útil caso precise fazer consultas mais complexas no futuro
    List<ItemPedido> findByPedidoId(Long pedidoId);
    @Query(value = "SELECT c.nome FROM itens_pedidos ip JOIN cervejas c ON ip.cerveja_id = c.id GROUP BY c.nome ORDER BY SUM(ip.quantidade) DESC LIMIT 1", nativeQuery = true)
    String findNomeCervejaMaisVendida();
}