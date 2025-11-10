// Em: src/main/java/com/example/brewer/model/DashboardDTO.java
package com.example.brewer.model;

import java.util.List;
import java.util.Map;

// Classe simples para transportar os dados do dashboard
public class DashboardDTO {
    private double faturamentoMes;
    private long pedidosHoje;
    private String cervejaMaisVendida;
    private Map<String, Long> pedidosPorStatus;
    private List<Cerveja> estoqueBaixo;

    // Getters e Setters
    public double getFaturamentoMes() { return faturamentoMes; }
    public void setFaturamentoMes(double faturamentoMes) { this.faturamentoMes = faturamentoMes; }

    public long getPedidosHoje() { return pedidosHoje; }
    public void setPedidosHoje(long pedidosHoje) { this.pedidosHoje = pedidosHoje; }

    public String getCervejaMaisVendida() { return cervejaMaisVendida; }
    public void setCervejaMaisVendida(String cervejaMaisVendida) { this.cervejaMaisVendida = cervejaMaisVendida; }

    public Map<String, Long> getPedidosPorStatus() { return pedidosPorStatus; }
    public void setPedidosPorStatus(Map<String, Long> pedidosPorStatus) { this.pedidosPorStatus = pedidosPorStatus; }

    public List<Cerveja> getEstoqueBaixo() { return estoqueBaixo; }
    public void setEstoqueBaixo(List<Cerveja> estoqueBaixo) { this.estoqueBaixo = estoqueBaixo; }
}