package com.example.waiterapp.Pedido;

import com.example.waiterapp.Garcom.Garcom;
import com.example.waiterapp.Item.Item;
import com.example.waiterapp.ItemPedido.ItemPedido;
import com.example.waiterapp.Pagamento.Pagamento;
import com.example.waiterapp.Pagamento.PagamentoComCartao.PagamentoComCartao;
import com.example.waiterapp.enums.Estado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Pedido")
class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido(1L, LocalDateTime.now(), Estado.EM_PREPARACAO, 0.0, null, null, null);
    }

    // ======================= setPrecoTotal() =======================

    @Test
    @DisplayName("setPrecoTotal deve calcular a soma dos subtotais de dois itens")
    void setPrecoTotal_comDoisItens_deveCalcularSomaCorreta() {
        // Arrange
        Item pizza = new Item(1L, "Pizza", "Desc", LocalDateTime.now(), 30.0);
        Item suco  = new Item(2L, "Suco",  "Desc", LocalDateTime.now(), 10.0);
        ItemPedido ip1 = new ItemPedido(pedido, pizza, 2); // 2 * 30 = 60
        ItemPedido ip2 = new ItemPedido(pedido, suco,  3); // 3 * 10 = 30
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip1);
        items.add(ip2);
        pedido.setItems(items);

        // Act
        pedido.setPrecoTotal();

        // Assert
        assertEquals(90.0, pedido.getPrecoTotal(), 0.001);
    }

    @Test
    @DisplayName("setPrecoTotal com lista vazia deve resultar em preço total zero")
    void setPrecoTotal_semItens_deveRetornarZero() {
        // Arrange
        pedido.setItems(new HashSet<>());

        // Act
        pedido.setPrecoTotal();

        // Assert
        assertEquals(0.0, pedido.getPrecoTotal(), 0.001);
    }

    @Test
    @DisplayName("setPrecoTotal com um único item de quantidade 1 deve retornar o preço do item")
    void setPrecoTotal_comUmItemQuantidadeUm_deveRetornarPrecoDoItem() {
        // Arrange
        Item hamburguer = new Item(1L, "Hamburguer", "Desc", LocalDateTime.now(), 25.0);
        ItemPedido ip = new ItemPedido(pedido, hamburguer, 1); // 1 * 25 = 25
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip);
        pedido.setItems(items);

        // Act
        pedido.setPrecoTotal();

        // Assert
        assertEquals(25.0, pedido.getPrecoTotal(), 0.001);
    }

    @Test
    @DisplayName("setPrecoTotal com quantidade zero deve resultar em preço total zero")
    void setPrecoTotal_comQuantidadeZero_deveResultarEmZero() {
        // Arrange
        Item item = new Item(1L, "Item", "Desc", LocalDateTime.now(), 50.0);
        ItemPedido ip = new ItemPedido(pedido, item, 0); // 0 * 50 = 0
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip);
        pedido.setItems(items);

        // Act
        pedido.setPrecoTotal();

        // Assert
        assertEquals(0.0, pedido.getPrecoTotal(), 0.001);
    }

    @Test
    @DisplayName("setPrecoTotal com itens de preços decimais deve somar corretamente")
    void setPrecoTotal_comPrecosDecimais_deveSomarCorretamente() {
        // Arrange
        Item i1 = new Item(1L, "I1", "Desc", LocalDateTime.now(), 9.99);
        Item i2 = new Item(2L, "I2", "Desc", LocalDateTime.now(), 0.01);
        ItemPedido ip1 = new ItemPedido(pedido, i1, 1); // 9.99
        ItemPedido ip2 = new ItemPedido(pedido, i2, 1); // 0.01
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip1);
        items.add(ip2);
        pedido.setItems(items);

        // Act
        pedido.setPrecoTotal();

        // Assert
        assertEquals(10.0, pedido.getPrecoTotal(), 0.001);
    }

    // ======================= fecharPedido() =======================

    @Test
    @DisplayName("fecharPedido deve alterar o estado do pedido para FECHADO")
    void fecharPedido_qualquerEstadoInicial_deveAlterarParaFechado() {
        // Arrange
        pedido.setEstado(Estado.EM_PREPARACAO);

        // Act
        pedido.fecharPedido();

        // Assert
        assertEquals(Estado.FECHADO, pedido.getEstado());
    }

    @Test
    @DisplayName("fecharPedido a partir de estado PENDENTE deve resultar em FECHADO")
    void fecharPedido_deEstadoPendente_deveResultarEmFechado() {
        // Arrange
        pedido.setEstado(Estado.PENDENTE);

        // Act
        pedido.fecharPedido();

        // Assert
        assertEquals(Estado.FECHADO, pedido.getEstado());
    }

    @Test
    @DisplayName("fecharPedido chamado duas vezes deve manter estado FECHADO")
    void fecharPedido_chamadoDuasVezes_deveManterId() {
        // Arrange & Act
        pedido.fecharPedido();
        pedido.fecharPedido();

        // Assert
        assertEquals(Estado.FECHADO, pedido.getEstado());
    }

    // ======================= adicionarItemExtra() =======================

    @Test
    @DisplayName("adicionarItemExtra não adiciona item ao set de items (bug documentado)")
    void adicionarItemExtra_naoAdicionaItemAoSetDeItems() {
        // Arrange
        Item item = new Item(1L, "Extra", "Desc", LocalDateTime.now(), 5.0);
        int tamanhoAntes = pedido.getItems().size();

        // Act
        pedido.adicionarItemExtra(item);

        // Assert — o método atual cria um ItemPedido mas não o insere no Set
        assertEquals(tamanhoAntes, pedido.getItems().size());
    }

    // ======================= getters/setters de associações =======================

    @Test
    @DisplayName("getGarcom deve retornar o garçom associado ao pedido")
    void getGarcom_garcomAssociado_deveRetornarGarcom() {
        // Arrange
        Garcom garcom = new Garcom(1L, "João", LocalDateTime.now(), "12345678901");

        // Act
        pedido.setGarcom(garcom);

        // Assert
        assertSame(garcom, pedido.getGarcom());
    }

    @Test
    @DisplayName("getPagamento deve retornar o pagamento associado ao pedido")
    void getPagamento_pagamentoAssociado_deveRetornarPagamento() {
        // Arrange
        Pagamento pagamento = new PagamentoComCartao(1L, Estado.CONCLUIDO, LocalDateTime.now());

        // Act
        pedido.setPagamento(pagamento);

        // Assert
        assertSame(pagamento, pedido.getPagamento());
    }

    @Test
    @DisplayName("chamarGarcom não deve lançar exceção")
    void chamarGarcom_naoDeveLancarExcecao() {
        assertDoesNotThrow(() -> pedido.chamarGarcom());
    }

    // ======================= equals() e hashCode() =======================

    @Test
    @DisplayName("equals deve retornar true para pedidos com o mesmo id")
    void equals_mesmosIds_deveRetornarTrue() {
        // Arrange
        Pedido outro = new Pedido(1L, LocalDateTime.now(), Estado.PAGO, 100.0, null, null, null);

        // Act & Assert
        assertEquals(pedido, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para pedidos com ids distintos")
    void equals_idsDiferentes_deveRetornarFalse() {
        // Arrange
        Pedido outro = new Pedido(2L, LocalDateTime.now(), Estado.EM_PREPARACAO, 0.0, null, null, null);

        // Act & Assert
        assertNotEquals(pedido, outro);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com null")
    void equals_comparadoComNull_deveRetornarFalse() {
        assertNotEquals(null, pedido);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com objeto de classe diferente")
    void equals_classesDiferentes_deveRetornarFalse() {
        assertNotEquals("uma string qualquer", pedido);
    }

    @Test
    @DisplayName("equals deve retornar true quando comparado com o próprio objeto")
    void equals_mesmoObjeto_deveRetornarTrue() {
        assertEquals(pedido, pedido);
    }

    @Test
    @DisplayName("hashCode deve ser igual para pedidos com mesmo id")
    void hashCode_mesmosIds_deveGerarMesmoHash() {
        // Arrange
        Pedido outro = new Pedido(1L, LocalDateTime.now(), Estado.PAGO, 200.0, null, null, null);

        // Act & Assert
        assertEquals(pedido.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve ser diferente para pedidos com ids distintos")
    void hashCode_idsDiferentes_deveGerarHashesDiferentes() {
        // Arrange
        Pedido outro = new Pedido(2L, LocalDateTime.now(), Estado.EM_PREPARACAO, 0.0, null, null, null);

        // Act & Assert — não é garantia absoluta, mas deve ser diferente para ids simples
        assertNotEquals(pedido.hashCode(), outro.hashCode());
    }

    // ======================= Estado e campos opcionais =======================

    @Test
    @DisplayName("pedido criado com estado EM_PREPARACAO deve manter esse estado")
    void pedido_estadoInicial_deveSer_EM_PREPARACAO() {
        assertEquals(Estado.EM_PREPARACAO, pedido.getEstado());
    }

    @Test
    @DisplayName("notas de atendimento e pedido podem ser null inicialmente")
    void pedido_notasOpcionalmentePodeSerNull() {
        assertNull(pedido.getNotaAtendimento());
        assertNull(pedido.getNotaPedido());
    }

    @Test
    @DisplayName("nota de atendimento deve aceitar valor mínimo (1) e máximo (10)")
    void pedido_notaAtendimento_valoresLimite() {
        // Arrange & Act
        pedido.setNotaAtendimento(1);
        assertEquals(1, pedido.getNotaAtendimento());

        pedido.setNotaAtendimento(10);
        assertEquals(10, pedido.getNotaAtendimento());
    }

    @Test
    @DisplayName("nota de pedido deve aceitar valor mínimo (1) e máximo (10)")
    void pedido_notaPedido_valoresLimite() {
        pedido.setNotaPedido(1);
        assertEquals(1, pedido.getNotaPedido());

        pedido.setNotaPedido(10);
        assertEquals(10, pedido.getNotaPedido());
    }

    @Test
    @DisplayName("opcoesExtras pode ser null")
    void pedido_opcoesExtras_podeSerNull() {
        assertNull(pedido.getOpcoesExtras());
    }

    @Test
    @DisplayName("toString deve incluir o id do pedido")
    void toString_deveConterIdDoPedido() {
        assertTrue(pedido.toString().contains("1"));
    }
}
