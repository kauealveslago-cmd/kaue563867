package br.com.fiap.kaue563867.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "GS668_TRECHO")
@Getter
@Setter
@NoArgsConstructor
public class Trecho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "logradouro", nullable = false, length = 120)
    private String logradouro;

    @Column(name = "tipo_calcada", nullable = false, length = 20)
    private String tipoCalcada;

    @Column(name = "altitude", nullable = false)
    private Integer altitude;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;
}
