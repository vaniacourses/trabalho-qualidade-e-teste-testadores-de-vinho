package com.example.waiterapp.ItemPedido;

import com.example.waiterapp.Item.Item;
import com.example.waiterapp.Pedido.Pedido;
import com.example.waiterapp.enums.Estado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para ItemPedidoPK")
class ItemPedidoPKTest {

    private ItemPedidoPK pk;
    private Item item;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "Pizza", "Desc", LocalDateTime.now(), 30.0);
        pedido = new Pedido(10L, LocalDateTime.now(), Estado.EM_PREPARACAO, 0.0, null, null, null);
        pk = new ItemPedidoPK();
        pk.setItem(item);
        pk.setPedido(pedido);
    }

    @Test
    @DisplayName("equals deve retornar true para o mesmo objeto")
    void equals_mesmoObjeto_deveRetornarTrue() {
        assertEquals(pk, pk);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com null")
    void equals_comparadoComNull_deveRetornarFalse() {
        assertNotEquals(null, pk);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com classe diferente")
    void equals_classeDiferente_deveRetornarFalse() {
        assertNotEquals(pk, "outro tipo");
    }

    @Test
    @DisplayName("equals deve retornar true para mesmos item e pedido")
    void equals_mesmosItemEPedido_deveRetornarTrue() {
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(item);
        outro.setPedido(pedido);
        assertEquals(pk, outro);
    }

    @Test
    @DisplayName("equals deve retornar false quando o item é diferente")
    void equals_itemDiferente_deveRetornarFalse() {
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(new Item(2L, "Suco", "Desc", LocalDateTime.now(), 5.0));
        outro.setPedido(pedido);
        assertNotEquals(pk, outro);
    }

    @Test
    @DisplayName("equals deve retornar false quando o pedido é diferente")
    void equals_pedidoDiferente_deveRetornarFalse() {
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(item);
        outro.setPedido(new Pedido(99L, LocalDateTime.now(), Estado.PENDENTE, 0.0, null, null, null));
        assertNotEquals(pk, outro);
    }

    @Test
    @DisplayName("equals deve retornar true quando item e pedido são null em ambos")
    void equals_itemEPedidoNullEmAmbos_deveRetornarTrue() {
        ItemPedidoPK a = new ItemPedidoPK();
        ItemPedidoPK b = new ItemPedidoPK();
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals deve retornar false quando apenas um tem item null")
    void equals_apenasUmComItemNull_deveRetornarFalse() {
        ItemPedidoPK comItem = new ItemPedidoPK();
        comItem.setItem(item);
        ItemPedidoPK semItem = new ItemPedidoPK();
        assertNotEquals(comItem, semItem);
    }

    @Test
    @DisplayName("equals deve retornar false quando apenas um tem pedido null")
    void equals_apenasUmComPedidoNull_deveRetornarFalse() {
        ItemPedidoPK comPedido = new ItemPedidoPK();
        comPedido.setPedido(pedido);
        ItemPedidoPK semPedido = new ItemPedidoPK();
        assertNotEquals(comPedido, semPedido);
    }

    @Test
    @DisplayName("equals deve retornar false na ordem inversa quando apenas um tem item null")
    void equals_ordemInversaItemNull_deveRetornarFalse() {
        ItemPedidoPK comItem = new ItemPedidoPK();
        comItem.setItem(item);
        ItemPedidoPK semItem = new ItemPedidoPK();
        assertNotEquals(semItem, comItem);
    }

    @Test
    @DisplayName("hashCode deve ser igual para chaves equivalentes")
    void hashCode_chavesEquivalentes_deveSerIgual() {
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(item);
        outro.setPedido(pedido);
        assertEquals(pk.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir quando os itens são diferentes")
    void hashCode_itensDiferentes_deveDiferir() {
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(new Item(2L, "Outro", "Desc", LocalDateTime.now(), 1.0));
        outro.setPedido(pedido);
        assertNotEquals(pk.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("getters e setters devem manter item e pedido associados")
    void gettersSetters_devemManterAssociacoes() {
        assertSame(item, pk.getItem());
        assertSame(pedido, pk.getPedido());
    }
}
