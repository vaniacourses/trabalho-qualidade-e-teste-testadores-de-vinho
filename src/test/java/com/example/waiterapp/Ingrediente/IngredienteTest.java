package com.example.waiterapp.Ingrediente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Ingrediente")
class IngredienteTest {

    private Ingrediente ingrediente;

    @BeforeEach
    void setUp() {
        ingrediente = new Ingrediente(1L, "Tomate", "Fresco", LocalDateTime.now(), 18.0f);
    }

    @Test
    @DisplayName("equals deve retornar true para o mesmo objeto")
    void equals_mesmoObjeto_deveRetornarTrue() {
        assertEquals(ingrediente, ingrediente);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com null")
    void equals_comparadoComNull_deveRetornarFalse() {
        assertNotEquals(null, ingrediente);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com classe diferente")
    void equals_classeDiferente_deveRetornarFalse() {
        assertNotEquals(ingrediente, "ingrediente");
    }

    @Test
    @DisplayName("equals deve retornar true para ingredientes com o mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Ingrediente outro = new Ingrediente(1L, "Cebola", "Picada", LocalDateTime.now(), 40.0f);
        assertEquals(ingrediente, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para ingredientes com ids distintos")
    void equals_idsDiferentes_deveRetornarFalse() {
        Ingrediente outro = new Ingrediente(2L, "Tomate", "Fresco", LocalDateTime.now(), 18.0f);
        assertNotEquals(ingrediente, outro);
    }

    @Test
    @DisplayName("equals deve retornar true quando ambos os ids são null")
    void equals_ambosIdsNull_deveRetornarTrue() {
        Ingrediente a = new Ingrediente();
        Ingrediente b = new Ingrediente();
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals deve retornar false quando apenas um id é null")
    void equals_apenasUmIdNull_deveRetornarFalse() {
        Ingrediente comId = new Ingrediente(1L, "Tomate", "Fresco", LocalDateTime.now(), 18.0f);
        Ingrediente semId = new Ingrediente();
        assertNotEquals(comId, semId);
    }

    @Test
    @DisplayName("hashCode deve ser igual para ingredientes com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Ingrediente outro = new Ingrediente(1L, "X", "Y", LocalDateTime.now(), 1.0f);
        assertEquals(ingrediente.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir para ids distintos")
    void hashCode_idsDiferentes_deveDiferir() {
        Ingrediente outro = new Ingrediente(2L, "Tomate", "Fresco", LocalDateTime.now(), 18.0f);
        assertNotEquals(ingrediente.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("toString deve conter o nome do ingrediente")
    void toString_deveConterNome() {
        assertTrue(ingrediente.toString().contains("Tomate"));
    }
}
