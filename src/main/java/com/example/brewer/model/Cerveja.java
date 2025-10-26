package com.example.brewer.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cervejas")
public class Cerveja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "O SKU é obrigatório.")
    @Size(max = 20, message = "O SKU deve ter no máximo 20 caracteres.")
    private String sku;
    @NotBlank(message = "O nome da cerveja é obrigatório.")
    private String nome;
    private Double volume;
    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    private Integer quantidadeEstoque;
    @NotNull(message = "O valor de venda é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private Double valorVenda;

}
