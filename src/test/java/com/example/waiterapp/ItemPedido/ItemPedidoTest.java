package com.example.waiterapp.ItemPedido;

import com.example.waiterapp.Item.Item;
import com.example.waiterapp.Pedido.Pedido;
import com.example.waiterapp.enums.Estado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para ItemPedido")
class ItemPedidoTest {

    private Pedido pedido;
    private Item item;

    @BeforeEach
    void setUp() {
        pedido = new Pedido(1L, LocalDateTime.now(), Estado.EM_PREPARACAO, 0.0, null, null, null);
        item   = new Item(1L, "Pizza Napolitana", "Pizza com tomate e manjericão", LocalDateTime.now(), 40.0);
    }

    // ======================= getSubTotal() =======================

    @Test
    @DisplayName("getSubTotal deve retornar quantidade multiplicada pelo preço do item")
    void getSubTotal_quantidadePositiva_deveCalcularCorretamente() {
        // Arrange
        ItemPedido itemPedido = new ItemPedido(pedido, item, 3); // 3 * 40 = 120

        // Act
        Double subtotal = itemPedido.getSubTotal();

        // Assert
        assertEquals(120.0, subtotal, 0.001);
    }

    @Test
    @DisplayName("getSubTotal com quantidade 1 deve retornar exatamente o preço do item")
    void getSubTotal_quantidadeUm_deveRetornarPrecoDoItem() {
        // Arrange
        ItemPedido itemPedido = new ItemPedido(pedido, item, 1);

        // Act
        Double subtotal = itemPedido.getSubTotal();

        // Assert
        assertEquals(40.0, subtotal, 0.001);
    }

    @Test
    @DisplayName("getSubTotal com quantidade zero deve retornar zero (caso limite)")
    void getSubTotal_quantidadeZero_deveRetornarZero() {
        // Arrange
        ItemPedido itemPedido = new ItemPedido(pedido, item, 0);

        // Act
        Double subtotal = itemPedido.getSubTotal();

        // Assert
        assertEquals(0.0, subtotal, 0.001);
    }

    @Test
    @DisplayName("getSubTotal com preço decimal deve calcular corretamente (caso limite)")
    void getSubTotal_precoDecimal_deveCalcularCorretamente() {
        // Arrange – 4 unidades de R$ 7,50 = R$ 30,00
        Item itemDecimal = new Item(2L, "Suco Natural", "Fresco", LocalDateTime.now(), 7.50);
        ItemPedido itemPedido = new ItemPedido(pedido, itemDecimal, 4);

        // Act
        Double subtotal = itemPedido.getSubTotal();

        // Assert
        assertEquals(30.0, subtotal, 0.001);
    }

    @Test
    @DisplayName("getSubTotal usa o preço atual do item, não o preço armazenado no momento da criação")
    void getSubTotal_apesDeAlterarPrecoDoItem_deveUsarPrecoAtual() {
        // Arrange – preço original 40.0; altera para 50.0 após criação
        ItemPedido itemPedido = new ItemPedido(pedido, item, 2);
        item.setPreco(50.0);

        // Act
        Double subtotal = itemPedido.getSubTotal();

        // Assert – getSubTotal usa id.getItem().getPreco(), que reflete a mudança
        assertEquals(100.0, subtotal, 0.001);
    }

    @Test
    @DisplayName("getSubTotal ignora o preço armazenado em ItemPedido e usa o preço do item (comportamento documentado)")
    void getSubTotal_comPrecoArmazenadoDiferente_deveUsarPrecoDoItem() {
        // Arrange – item R$ 40, qtd 2; preço armazenado alterado para R$ 15 (ex.: após inserePedido)
        ItemPedido itemPedido = new ItemPedido(pedido, item, 2);
        itemPedido.setPreco(15.0);

        // Act
        Double subtotal = itemPedido.getSubTotal();

        // Assert – getSubTotal usa id.getItem().getPreco(), não o campo preco do ItemPedido
        assertEquals(80.0, subtotal, 0.001);
        assertEquals(15.0, itemPedido.getPreco(), 0.001);
    }

    @Test
    @DisplayName("getSubTotal com preço de item igual a zero deve retornar zero")
    void getSubTotal_precoDoItemZero_deveRetornarZero() {
        // Arrange
        Item itemGratuito = new Item(3L, "Água", "Cortesia", LocalDateTime.now(), 0.0);
        ItemPedido itemPedido = new ItemPedido(pedido, itemGratuito, 5);

        // Act
        Double subtotal = itemPedido.getSubTotal();

        // Assert
        assertEquals(0.0, subtotal, 0.001);
    }

    @Test
    @DisplayName("getSubTotal com grande quantidade deve calcular corretamente (valor de fronteira alto)")
    void getSubTotal_grandeQuantidade_deveCalcularCorretamente() {
        // Arrange – 1000 unidades de R$ 10,00 = R$ 10.000,00
        Item itemCaro = new Item(4L, "Item Especial", "Desc", LocalDateTime.now(), 10.0);
        ItemPedido itemPedido = new ItemPedido(pedido, itemCaro, 1000);

        // Act
        Double subtotal = itemPedido.getSubTotal();

        // Assert
        assertEquals(10000.0, subtotal, 0.001);
    }

    // ======================= Construtores =======================

    @Test
    @DisplayName("construtor com preço explícito deve armazenar o preço informado")
    void construtor_comPrecoExplicito_deveArmazenarPrecoInformado() {
        // Arrange & Act
        ItemPedido itemPedido = new ItemPedido(pedido, item, 2, 99.0);

        // Assert
        assertEquals(99.0, itemPedido.getPreco(), 0.001);
        assertEquals(2, itemPedido.getQuantidade());
    }

    @Test
    @DisplayName("construtor sem preço deve usar o preço atual do item")
    void construtor_semPreco_deveUsarPrecoDoItem() {
        // Arrange & Act
        ItemPedido itemPedido = new ItemPedido(pedido, item, 1);

        // Assert
        assertEquals(item.getPreco(), itemPedido.getPreco(), 0.001);
    }

    @Test
    @DisplayName("construtor deve associar item e pedido corretamente")
    void construtor_deveAssociarItemEPedidoCorretamente() {
        // Arrange & Act
        ItemPedido itemPedido = new ItemPedido(pedido, item, 1);

        // Assert
        assertEquals(item, itemPedido.getItem());
        assertEquals(pedido, itemPedido.getPedido());
    }

    // ======================= Setters =======================

    @Test
    @DisplayName("setQuantidade deve alterar a quantidade do item no pedido")
    void setQuantidade_deveAlterarQuantidade() {
        // Arrange
        ItemPedido itemPedido = new ItemPedido(pedido, item, 1);

        // Act
        itemPedido.setQuantidade(5);

        // Assert
        assertEquals(5, itemPedido.getQuantidade());
    }

    @Test
    @DisplayName("setPreco deve alterar o preço armazenado")
    void setPreco_deveAlterarPrecoArmazenado() {
        // Arrange
        ItemPedido itemPedido = new ItemPedido(pedido, item, 1);

        // Act
        itemPedido.setPreco(15.0);

        // Assert
        assertEquals(15.0, itemPedido.getPreco(), 0.001);
    }

    @Test
    @DisplayName("setItem deve alterar o item associado ao ItemPedido")
    void setItem_deveAlterarItemAssociado() {
        // Arrange
        ItemPedido itemPedido = new ItemPedido(pedido, item, 1);
        Item novoItem = new Item(2L, "Salada", "Verde", LocalDateTime.now(), 12.0);

        // Act
        itemPedido.setItem(novoItem);

        // Assert
        assertEquals(novoItem, itemPedido.getItem());
    }

    @Test
    @DisplayName("setPedido deve alterar o pedido associado ao ItemPedido")
    void setPedido_deveAlterarPedidoAssociado() {
        // Arrange
        ItemPedido itemPedido = new ItemPedido(pedido, item, 1);
        Pedido novoPedido = new Pedido(2L, LocalDateTime.now(), Estado.PENDENTE, 0.0, null, null, null);

        // Act
        itemPedido.setPedido(novoPedido);

        // Assert
        assertEquals(novoPedido, itemPedido.getPedido());
    }
}
