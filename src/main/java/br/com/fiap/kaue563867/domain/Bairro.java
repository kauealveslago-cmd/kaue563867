package br.com.fiap.kaue563867.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "GS668_BAIRRO")
@Getter
@Setter
@NoArgsConstructor
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "zona", nullable = false, length = 10)
    private String zona;

    // P3 = 2: atributo Boolean (não obrigatório)
    @Column(name = "possui_piscinao")
    private Boolean possuiPiscinao;
}
