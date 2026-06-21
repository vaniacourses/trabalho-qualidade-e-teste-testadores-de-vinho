package com.example.waiterapp.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Item")
class ItemTest {

    private static final LocalDateTime DATA = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "Frango Grelhado", "Prato saudável", DATA, 29.90);
    }

    @Test
    @DisplayName("construtor com argumentos deve inicializar todos os campos")
    void construtor_comArgumentos_deveInicializarCampos() {
        assertEquals(1L, item.getId());
        assertEquals("Frango Grelhado", item.getNome());
        assertEquals("Prato saudável", item.getDescricao());
        assertEquals(DATA, item.getDataCriacao());
        assertEquals(29.90, item.getPreco());
    }

    @Test
    @DisplayName("construtor padrão deve criar instância válida")
    void construtor_padrao_deveCriarInstancia() {
        Item i = new Item();
        assertNotNull(i);
    }

    @Test
    @DisplayName("setters devem atualizar os campos corretamente")
    void setters_devemAtualizarCampos() {
        item.setId(2L);
        item.setNome("Salmão");
        item.setDescricao("Salmão grelhado");
        item.setDataCriacao(DATA.plusDays(1));
        item.setPreco(45.00);

        assertEquals(2L, item.getId());
        assertEquals("Salmão", item.getNome());
        assertEquals("Salmão grelhado", item.getDescricao());
        assertEquals(DATA.plusDays(1), item.getDataCriacao());
        assertEquals(45.00, item.getPreco());
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
    @DisplayName("equals deve retornar true para itens com mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Item outro = new Item(1L, "Outro Nome", "outra desc", DATA, 10.0);
        assertEquals(item, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para itens com ids diferentes")
    void equals_idsDiferentes_deveRetornarFalse() {
        Item outro = new Item(2L, "Frango Grelhado", "Prato saudável", DATA, 29.90);
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
        Item comId = new Item(1L, "Hambúrguer", "Desc", DATA, 25.0);
        Item semId = new Item();
        assertNotEquals(comId, semId);
    }

    @Test
    @DisplayName("hashCode deve ser igual para itens com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Item outro = new Item(1L, "Outro", "desc", DATA, 5.0);
        assertEquals(item.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir para ids distintos")
    void hashCode_idsDiferentes_deveDiferir() {
        Item outro = new Item(2L, "Frango Grelhado", "Prato saudável", DATA, 29.90);
        assertNotEquals(item.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("toString deve conter o nome do item")
    void toString_deveConterNome() {
        assertTrue(item.toString().contains("Frango Grelhado"));
    }

    @Test
    @DisplayName("getCardapios e setCardapios devem funcionar corretamente")
    void cardapios_getterESetter_devemFuncionar() {
        item.setCardapios(new ArrayList<>());
        assertNotNull(item.getCardapios());
        assertTrue(item.getCardapios().isEmpty());
    }

    @Test
    @DisplayName("getItems e setItems devem funcionar corretamente")
    void items_getterESetter_devemFuncionar() {
        item.setItems(new HashSet<>());
        assertNotNull(item.getItems());
        assertTrue(item.getItems().isEmpty());
    }
}
