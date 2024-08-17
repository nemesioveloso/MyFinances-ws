package com.finances.myfinances.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.NonNull;

@Entity
@Table(name = "finances")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Finance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String data; // Pode ser ajustado para LocalDate dependendo das necessidades

    @NonNull
    private String categoria;

    @NonNull
    private String descricao;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NonNull
    private Double valor;

    @NonNull
    @Enumerated(EnumType.STRING)
    private FixoVariavel fixoVariavel;

    private Integer parcelas;

    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
