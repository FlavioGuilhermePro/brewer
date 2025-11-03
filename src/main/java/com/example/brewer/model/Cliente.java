package com.example.brewer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome só pode ter no máximo 100 caracteres")
    private String nome;
    @NotBlank(message = "O CPF é obrigatório")
    @Size(max = 11, message = "Um cpf tem no maximo 11 caracteres")
    private String cpf;
    @NotBlank(message = "O endereço é obrigatorio")
    private String endereço;

}
