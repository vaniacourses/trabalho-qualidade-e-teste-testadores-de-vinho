package com.example.waiterapp.Cliente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Cliente")
class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Ana", "ana@test.com", "11122233344", LocalDateTime.now());
    }

    @Test
    @DisplayName("equals deve retornar true para o mesmo objeto")
    void equals_mesmoObjeto_deveRetornarTrue() {
        assertEquals(cliente, cliente);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com null")
    void equals_comparadoComNull_deveRetornarFalse() {
        assertNotEquals(null, cliente);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com classe diferente")
    void equals_classeDiferente_deveRetornarFalse() {
        assertNotEquals(cliente, "cliente");
    }

    @Test
    @DisplayName("equals deve retornar true para clientes com o mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Cliente outro = new Cliente(1L, "Outro", "outro@test.com", "99999999999", LocalDateTime.now());
        assertEquals(cliente, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para clientes com ids distintos")
    void equals_idsDiferentes_deveRetornarFalse() {
        Cliente outro = new Cliente(2L, "Ana", "ana@test.com", "11122233344", LocalDateTime.now());
        assertNotEquals(cliente, outro);
    }

    @Test
    @DisplayName("equals deve retornar true quando ambos os ids são null")
    void equals_ambosIdsNull_deveRetornarTrue() {
        Cliente a = new Cliente();
        Cliente b = new Cliente();
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals deve retornar false quando apenas um id é null")
    void equals_apenasUmIdNull_deveRetornarFalse() {
        Cliente comId = new Cliente(1L, "Ana", "ana@test.com", "11122233344", LocalDateTime.now());
        Cliente semId = new Cliente();
        assertNotEquals(comId, semId);
    }

    @Test
    @DisplayName("hashCode deve ser igual para clientes com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Cliente outro = new Cliente(1L, "X", "x@test.com", "00000000000", LocalDateTime.now());
        assertEquals(cliente.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir para ids distintos")
    void hashCode_idsDiferentes_deveDiferir() {
        Cliente outro = new Cliente(2L, "Ana", "ana@test.com", "11122233344", LocalDateTime.now());
        assertNotEquals(cliente.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("toString deve conter o nome do cliente")
    void toString_deveConterNome() {
        assertTrue(cliente.toString().contains("Ana"));
    }
}
