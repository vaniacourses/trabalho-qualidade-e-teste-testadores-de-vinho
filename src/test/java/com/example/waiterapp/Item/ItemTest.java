package com.example.waiterapp.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Item")
class ItemTest {

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "Hambúrguer", "Desc", LocalDateTime.now(), 25.0);
    }

    @Test
    @DisplayName("equals deve retornar true para o mesmo objeto")
    void equals_mesmoObjeto_deveRetornarTrue() {
        assertEquals(item, item);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com null")
    void equals_comparadoComNull_deveRetornarFalse() {
        assertNotEquals(null, item);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com classe diferente")
    void equals_classeDiferente_deveRetornarFalse() {
        assertNotEquals(item, 42);
    }

    @Test
    @DisplayName("equals deve retornar true para itens com o mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Item outro = new Item(1L, "Outro nome", "Outra desc", LocalDateTime.now(), 99.0);
        assertEquals(item, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para itens com ids distintos")
    void equals_idsDiferentes_deveRetornarFalse() {
        Item outro = new Item(2L, "Hambúrguer", "Desc", LocalDateTime.now(), 25.0);
        assertNotEquals(item, outro);
    }

    @Test
    @DisplayName("equals deve retornar true quando ambos os ids são null")
    void equals_ambosIdsNull_deveRetornarTrue() {
        Item a = new Item();
        Item b = new Item();
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals deve retornar false quando apenas um id é null")
    void equals_apenasUmIdNull_deveRetornarFalse() {
        Item comId = new Item(1L, "Hambúrguer", "Desc", LocalDateTime.now(), 25.0);
        Item semId = new Item();
        assertNotEquals(comId, semId);
    }

    @Test
    @DisplayName("hashCode deve ser igual para itens com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Item outro = new Item(1L, "X", "Y", LocalDateTime.now(), 1.0);
        assertEquals(item.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir para ids distintos")
    void hashCode_idsDiferentes_deveDiferir() {
        Item outro = new Item(2L, "Hambúrguer", "Desc", LocalDateTime.now(), 25.0);
        assertNotEquals(item.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("toString deve conter o nome do item")
    void toString_deveConterNome() {
        assertTrue(item.toString().contains("Hambúrguer"));
    }
}
