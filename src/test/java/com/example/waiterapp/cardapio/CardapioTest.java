package com.example.waiterapp.cardapio;

import com.example.waiterapp.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Cardapio")
class CardapioTest {

    private static final LocalDateTime DATA = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    private Cardapio cardapio;

    @BeforeEach
    void setUp() {
        cardapio = new Cardapio(1L, DATA, "Cardápio Principal", "Pratos do dia");
    }

    @Test
    @DisplayName("construtor com argumentos deve inicializar todos os campos")
    void construtor_comArgumentos_deveInicializarCampos() {
        assertEquals(1L, cardapio.getId());
        assertEquals(DATA, cardapio.getDataCriacao());
        assertEquals("Cardápio Principal", cardapio.getTitulo());
        assertEquals("Pratos do dia", cardapio.getDescricao());
    }

    @Test
    @DisplayName("construtor padrão deve criar instância válida")
    void construtor_padrao_deveCriarInstancia() {
        Cardapio c = new Cardapio();
        assertNotNull(c);
    }

    @Test
    @DisplayName("setters devem atualizar os campos corretamente")
    void setters_devemAtualizarCampos() {
        cardapio.setId(2L);
        cardapio.setTitulo("Novo Título");
        cardapio.setDescricao("Nova descrição");
        cardapio.setDataCriacao(DATA.plusDays(1));

        assertEquals(2L, cardapio.getId());
        assertEquals("Novo Título", cardapio.getTitulo());
        assertEquals("Nova descrição", cardapio.getDescricao());
        assertEquals(DATA.plusDays(1), cardapio.getDataCriacao());
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
    @DisplayName("equals deve retornar true para cardapios com mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Cardapio outro = new Cardapio(1L, DATA, "Outro Título", "outra desc");
        assertEquals(cardapio, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para cardapios com ids diferentes")
    void equals_idsDiferentes_deveRetornarFalse() {
        Cardapio outro = new Cardapio(2L, DATA, "Cardápio Principal", "Pratos do dia");
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
        Cardapio comId = new Cardapio(1L, DATA, "Almoço", "Desc");
        Cardapio semId = new Cardapio();
        assertNotEquals(comId, semId);
    }

    @Test
    @DisplayName("hashCode deve ser igual para cardapios com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Cardapio outro = new Cardapio(1L, DATA, "Outro", "desc");
        assertEquals(cardapio.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir para ids distintos")
    void hashCode_idsDiferentes_deveDiferir() {
        Cardapio outro = new Cardapio(2L, DATA, "Cardápio Principal", "Pratos do dia");
        assertNotEquals(cardapio.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("toString deve conter o título do cardápio")
    void toString_deveConterTitulo() {
        assertTrue(cardapio.toString().contains("Cardápio Principal"));
    }

    @Test
    @DisplayName("getItems e setItems devem funcionar corretamente")
    void items_getterESetter_devemFuncionar() {
        List<Item> items = new ArrayList<>();
        cardapio.setItems(items);
        assertEquals(items, cardapio.getItems());
    }
}
