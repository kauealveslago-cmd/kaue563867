package br.com.fiap.kaue563867.repository;

import br.com.fiap.kaue563867.domain.Trecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrechoRepository extends JpaRepository<Trecho, Long> {

    // P4 = 2: nome do bairro (A a Z) e, no empate, logradouro
    List<Trecho> findAllByOrderByBairroNomeAscLogradouroAsc();

    List<Trecho> findByBairroIdOrderByLogradouroAsc(Long bairroId);

    boolean existsByBairroId(Long bairroId);
}
