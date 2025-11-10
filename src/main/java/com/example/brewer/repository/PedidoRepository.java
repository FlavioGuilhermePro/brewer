package com.example.brewer.repository;

import com.example.brewer.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatus(String status);
    // Nova query para somar o total dos pedidos do mês atual
    @Query("SELECT COALESCE(SUM(p.total), 0.0) FROM Pedido p WHERE MONTH(p.dataPedido) = MONTH(CURRENT_DATE) AND YEAR(p.dataPedido) = YEAR(CURRENT_DATE)")
    double sumTotalDoMesAtual();

    // Nova query para contar pedidos de hoje
    @Query("SELECT COUNT(p) FROM Pedido p WHERE DATE(p.dataPedido) = CURRENT_DATE")
    long countPedidosDeHoje();
}