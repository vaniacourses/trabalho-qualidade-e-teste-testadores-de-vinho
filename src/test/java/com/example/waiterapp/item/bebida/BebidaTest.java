package com.example.waiterapp.item.bebida;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Bebida")
class BebidaTest {

    private static final LocalDateTime DATA = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    private Bebida bebida;

    @BeforeEach
    void setUp() {
        bebida = new Bebida(1L, "Coca-Cola", "Refrigerante gelado", DATA, 6.50, "350ml");
    }

    @Test
    @DisplayName("construtor com argumentos deve inicializar todos os campos incluindo quantidade")
    void construtor_comArgumentos_deveInicializarCampos() {
        assertEquals(1L, bebida.getId());
        assertEquals("Coca-Cola", bebida.getNome());
        assertEquals("Refrigerante gelado", bebida.getDescricao());
        assertEquals(DATA, bebida.getDataCriacao());
        assertEquals(6.50, bebida.getPreco());
        assertEquals("350ml", bebida.getQuantidade());
    }

    @Test
    @DisplayName("construtor padrão deve criar instância válida")
    void construtor_padrao_deveCriarInstancia() {
        Bebida b = new Bebida();
        assertNotNull(b);
    }

    @Test
    @DisplayName("setQuantidade deve atualizar a quantidade")
    void setQuantidade_deveAtualizarQuantidade() {
        bebida.setQuantidade("500ml");
        assertEquals("500ml", bebida.getQuantidade());
    }

    @Test
    @DisplayName("equals deve retornar true para bebidas com mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Bebida outra = new Bebida(1L, "Outro Nome", "outra desc", DATA, 5.0, "200ml");
        assertEquals(bebida, outra);
    }

    @Test
    @DisplayName("equals deve retornar false para bebidas com ids diferentes")
    void equals_idsDiferentes_deveRetornarFalse() {
        Bebida outra = new Bebida(2L, "Coca-Cola", "Refrigerante gelado", DATA, 6.50, "350ml");
        assertNotEquals(bebida, outra);
    }

    @Test
    @DisplayName("hashCode deve ser igual para bebidas com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Bebida outra = new Bebida(1L, "Outro", "desc", DATA, 5.0, "200ml");
        assertEquals(bebida.hashCode(), outra.hashCode());
    }

    @Test
    @DisplayName("toString deve conter a quantidade da bebida")
    void toString_deveConterQuantidade() {
        assertTrue(bebida.toString().contains("350ml"));
    }
}
