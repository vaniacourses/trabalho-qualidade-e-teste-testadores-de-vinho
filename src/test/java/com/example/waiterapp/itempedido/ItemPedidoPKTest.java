package com.example.waiterapp.itempedido;

import com.example.waiterapp.enums.Estado;
import com.example.waiterapp.item.Item;
import com.example.waiterapp.pedido.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para ItemPedidoPK")
class ItemPedidoPKTest {

    private static final LocalDateTime DATA = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    private ItemPedidoPK pk;
    private Item item;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "Pizza", "Pizza napolitana", DATA, 40.0);
        pedido = new Pedido(1L, DATA, Estado.PENDENTE, 0.0, null, null, null);
        pk = new ItemPedidoPK();
        pk.setItem(item);
        pk.setPedido(pedido);
    }

    @Test
    @DisplayName("getItem e setItem devem funcionar corretamente")
    void item_getterESetter_devemFuncionar() {
        assertEquals(item, pk.getItem());
    }

    @Test
    @DisplayName("getPedido e setPedido devem funcionar corretamente")
    void pedido_getterESetter_devemFuncionar() {
        assertEquals(pedido, pk.getPedido());
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
    @DisplayName("equals deve retornar true para PKs com mesmo item e pedido")
    void equals_mesmoItemEPedido_deveRetornarTrue() {
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(item);
        outro.setPedido(pedido);
        assertEquals(pk, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para PKs com itens diferentes")
    void equals_itensDiferentes_deveRetornarFalse() {
        Item outroItem = new Item(2L, "Suco", "Suco natural", DATA, 8.0);
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(outroItem);
        outro.setPedido(pedido);
        assertNotEquals(pk, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para PKs com pedidos diferentes")
    void equals_pedidosDiferentes_deveRetornarFalse() {
        Pedido outroPedido = new Pedido(2L, DATA, Estado.EM_PREPARACAO, 0.0, null, null, null);
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(item);
        outro.setPedido(outroPedido);
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
    @DisplayName("hashCode deve ser igual para PKs com mesmo item e pedido")
    void hashCode_mesmoItemEPedido_deveSerIgual() {
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(item);
        outro.setPedido(pedido);
        assertEquals(pk.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir quando os itens são diferentes")
    void hashCode_itensDiferentes_deveDiferir() {
        ItemPedidoPK outro = new ItemPedidoPK();
        outro.setItem(new Item(2L, "Outro", "desc", DATA, 1.0));
        outro.setPedido(pedido);
        assertNotEquals(pk.hashCode(), outro.hashCode());
    }
}
