package com.example.brewer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "itens_pedidos")
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // ===== MUDANÇA IMPORTANTE =====
    // Mantém a referência à cerveja, mas com nullable e sem constraint rígida
    @ManyToOne
    @JoinColumn(name = "cerveja_id", nullable = true)
    private Cerveja cerveja;

    // ===== CAMPOS DESNORMALIZADOS (CÓPIA DOS DADOS) =====
    // Armazenamos os dados da cerveja NO MOMENTO DA VENDA
    // Assim, mesmo se a cerveja for deletada, temos o histórico

    @Column(name = "cerveja_sku", length = 20)
    private String cervejaSku;

    @Column(name = "cerveja_nome", nullable = false)
    private String cervejaNome;

    @Column(name = "cerveja_volume")
    private Double cervejaVolume;

    @NotNull(message = "Quantidade Obrigatória")
    private Integer quantidade;

    @Column(name = "valor_unitario", nullable = false)
    private Double valorUnitario;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    // ===== MÉTODO AUXILIAR =====
    // Copia os dados da cerveja para o item no momento da venda
    public void copiarDadosCerveja(Cerveja cerveja) {
        this.cerveja = cerveja;
        this.cervejaSku = cerveja.getSku();
        this.cervejaNome = cerveja.getNome();
        this.cervejaVolume = cerveja.getVolume();
        this.valorUnitario = cerveja.getValorVenda();
    }
}