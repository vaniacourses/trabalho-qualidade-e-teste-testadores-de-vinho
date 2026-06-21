package com.example.waiterapp.Cardapio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Cardapio")
class CardapioTest {

    private Cardapio cardapio;

    @BeforeEach
    void setUp() {
        cardapio = new Cardapio(1L, LocalDateTime.now(), "Almoço", "Cardápio do almoço");
    }

    @Test
    @DisplayName("equals deve retornar true para o mesmo objeto")
    void equals_mesmoObjeto_deveRetornarTrue() {
        assertEquals(cardapio, cardapio);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com null")
    void equals_comparadoComNull_deveRetornarFalse() {
        assertNotEquals(null, cardapio);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com classe diferente")
    void equals_classeDiferente_deveRetornarFalse() {
        assertNotEquals(cardapio, "cardápio");
    }

    @Test
    @DisplayName("equals deve retornar true para cardápios com o mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Cardapio outro = new Cardapio(1L, LocalDateTime.now(), "Jantar", "Outro título");
        assertEquals(cardapio, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para cardápios com ids distintos")
    void equals_idsDiferentes_deveRetornarFalse() {
        Cardapio outro = new Cardapio(2L, LocalDateTime.now(), "Almoço", "Cardápio do almoço");
        assertNotEquals(cardapio, outro);
    }

    @Test
    @DisplayName("equals deve retornar true quando ambos os ids são null")
    void equals_ambosIdsNull_deveRetornarTrue() {
        Cardapio a = new Cardapio();
        Cardapio b = new Cardapio();
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals deve retornar false quando apenas um id é null")
    void equals_apenasUmIdNull_deveRetornarFalse() {
        Cardapio comId = new Cardapio(1L, LocalDateTime.now(), "Almoço", "Desc");
        Cardapio semId = new Cardapio();
        assertNotEquals(comId, semId);
    }

    @Test
    @DisplayName("hashCode deve ser igual para cardápios com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Cardapio outro = new Cardapio(1L, LocalDateTime.now(), "X", "Y");
        assertEquals(cardapio.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir para ids distintos")
    void hashCode_idsDiferentes_deveDiferir() {
        Cardapio outro = new Cardapio(2L, LocalDateTime.now(), "Almoço", "Cardápio do almoço");
        assertNotEquals(cardapio.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("toString deve conter o título do cardápio")
    void toString_deveConterTitulo() {
        assertTrue(cardapio.toString().contains("Almoço"));
    }
}
