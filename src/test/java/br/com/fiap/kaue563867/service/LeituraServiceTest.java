package br.com.fiap.kaue563867.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Testa a regra R1 com L = 10 sem precisar do banco
class LeituraServiceTest {

    private final LeituraService service = new LeituraService(null, null, null);

    @Test
    void calcularStatusRespeitaOsLimitesDeL10() {
        assertEquals("NORMAL", service.calcularStatus(0));
        assertEquals("NORMAL", service.calcularStatus(4));
        assertEquals("ATENCAO", service.calcularStatus(5));
        assertEquals("ATENCAO", service.calcularStatus(9));
        assertEquals("ALERTA", service.calcularStatus(10));
        assertEquals("ALERTA", service.calcularStatus(300));
    }

    @Test
    void calcularStatusDoRoteiroDeTeste() {
        assertEquals("ATENCAO", service.calcularStatus(5));  // Rua Alta
        assertEquals("ATENCAO", service.calcularStatus(7));  // Travessa da Ponte
        assertEquals("ALERTA", service.calcularStatus(11));  // Rua das Palmeiras
        assertEquals("ALERTA", service.calcularStatus(13));  // Rua do Córrego
        assertEquals("ALERTA", service.calcularStatus(15));  // Av. Beira-Rio
        assertEquals("NORMAL", service.calcularStatus(3));   // Av. Beira-Rio alterada
    }
}
