// Em: src/main/java/com/example/brewer/service/DashboardService.java
package com.example.brewer.service;

import com.example.brewer.model.Cerveja;
import com.example.brewer.model.DashboardDTO;
import com.example.brewer.repository.CervejaRepository;
import com.example.brewer.repository.ItemPedidoRepository;
import com.example.brewer.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CervejaRepository cervejaRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public DashboardDTO getDadosDashboard() {
        DashboardDTO dto = new DashboardDTO();

        // 1. Faturamento do Mês
        dto.setFaturamentoMes(pedidoRepository.sumTotalDoMesAtual());

        // 2. Pedidos de Hoje
        dto.setPedidosHoje(pedidoRepository.countPedidosDeHoje());

        // 3. Cerveja Mais Vendida
        String maisVendida = itemPedidoRepository.findNomeCervejaMaisVendida();
        dto.setCervejaMaisVendida(maisVendida != null ? maisVendida : "N/A");

        // 4. Contagem de Pedidos por Status
        List<String> statuses = List.of("PENDENTE", "CONFIRMADO", "EM_TRANSITO", "ENTREGUE", "CANCELADO");
        Map<String, Long> contagemPorStatus = statuses.stream()
                .collect(Collectors.toMap(
                        status -> status,
                        status -> (long) pedidoRepository.findByStatus(status).size()
                ));
        dto.setPedidosPorStatus(contagemPorStatus);

        // 5. Produtos com Estoque Baixo
        dto.setEstoqueBaixo(cervejaRepository.findByQuantidadeEstoqueLessThan(10));

        return dto;
    }
}