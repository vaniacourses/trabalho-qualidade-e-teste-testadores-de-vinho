package com.example.waiterapp.Garcom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Garcom")
class GarcomTest {

    private Garcom garcom;

    @BeforeEach
    void setUp() {
        garcom = new Garcom(1L, "João", LocalDateTime.now(), "12345678901");
    }

    @Test
    @DisplayName("equals deve retornar true para o mesmo objeto")
    void equals_mesmoObjeto_deveRetornarTrue() {
        assertEquals(garcom, garcom);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com null")
    void equals_comparadoComNull_deveRetornarFalse() {
        assertNotEquals(null, garcom);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com classe diferente")
    void equals_classeDiferente_deveRetornarFalse() {
        assertNotEquals(garcom, "garçom");
    }

    @Test
    @DisplayName("equals deve retornar true para garçons com o mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Garcom outro = new Garcom(1L, "Maria", LocalDateTime.now(), "99999999999");
        assertEquals(garcom, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para garçons com ids distintos")
    void equals_idsDiferentes_deveRetornarFalse() {
        Garcom outro = new Garcom(2L, "João", LocalDateTime.now(), "12345678901");
        assertNotEquals(garcom, outro);
    }

    @Test
    @DisplayName("equals deve retornar true quando ambos os ids são null")
    void equals_ambosIdsNull_deveRetornarTrue() {
        Garcom a = new Garcom();
        Garcom b = new Garcom();
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals deve retornar false quando apenas um id é null")
    void equals_apenasUmIdNull_deveRetornarFalse() {
        Garcom comId = new Garcom(1L, "João", LocalDateTime.now(), "12345678901");
        Garcom semId = new Garcom();
        assertNotEquals(comId, semId);
    }

    @Test
    @DisplayName("hashCode deve ser igual para garçons com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Garcom outro = new Garcom(1L, "Outro", LocalDateTime.now(), "00000000000");
        assertEquals(garcom.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir para ids distintos")
    void hashCode_idsDiferentes_deveDiferir() {
        Garcom outro = new Garcom(2L, "João", LocalDateTime.now(), "12345678901");
        assertNotEquals(garcom.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("toString deve conter o nome do garçom")
    void toString_deveConterNome() {
        assertTrue(garcom.toString().contains("João"));
    }
}
