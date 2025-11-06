package com.example.brewer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "data_pedido")
    private LocalDate dataPedido;

    @ManyToOne
    @NotNull(message = "O cliente é obrigatório")
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotNull
    private double total;

    private String status;

    // CORREÇÃO: fetch = FetchType.EAGER para evitar LazyInitializationException
    // Isso carrega os itens automaticamente junto com o pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();
}