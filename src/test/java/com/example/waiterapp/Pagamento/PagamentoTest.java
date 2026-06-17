package com.example.waiterapp.Pagamento;

import com.example.waiterapp.Pagamento.PagamentoComCartao.PagamentoComCartao;
import com.example.waiterapp.Pagamento.PagamentoComDinheiro.PagamentoComDinheiro;
import com.example.waiterapp.Pedido.Pedido;
import com.example.waiterapp.enums.Estado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a hierarquia Pagamento")
class PagamentoTest {

    private LocalDateTime agora;

    @BeforeEach
    void setUp() {
        agora = LocalDateTime.now();
    }

    // ======================= PagamentoComCartao =======================

    @Test
    @DisplayName("PagamentoComCartao deve armazenar id e estado corretos")
    void pagamentoComCartao_construtor_deveArmazenarCampos() {
        PagamentoComCartao p = new PagamentoComCartao(1L, Estado.PENDENTE, agora);

        assertEquals(1L, p.getId());
        assertEquals(Estado.PENDENTE, p.getEstadoPagamento());
        assertEquals(agora, p.getDataPagamento());
    }

    @Test
    @DisplayName("PagamentoComCartao.confirmarPagamento deve retornar null enquanto não implementado")
    void pagamentoComCartao_confirmarPagamento_retornaNull() {
        PagamentoComCartao p = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        assertNull(p.confirmarPagamento());
    }

    @Test
    @DisplayName("PagamentoComCartao.setEstadoPagamento deve atualizar o estado")
    void pagamentoComCartao_setEstadoPagamento_deveAtualizar() {
        PagamentoComCartao p = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        p.setEstadoPagamento(Estado.PAGO);
        assertEquals(Estado.PAGO, p.getEstadoPagamento());
    }

    @Test
    @DisplayName("PagamentoComCartao.equals deve retornar true para mesmo id")
    void pagamentoComCartao_equals_mesmoId_retornaTrue() {
        PagamentoComCartao p1 = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        PagamentoComCartao p2 = new PagamentoComCartao(1L, Estado.PAGO, agora);
        assertEquals(p1, p2);
    }

    @Test
    @DisplayName("PagamentoComCartao.equals deve retornar false para ids diferentes")
    void pagamentoComCartao_equals_idsDiferentes_retornaFalse() {
        PagamentoComCartao p1 = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        PagamentoComCartao p2 = new PagamentoComCartao(2L, Estado.PENDENTE, agora);
        assertNotEquals(p1, p2);
    }

    @Test
    @DisplayName("PagamentoComCartao.equals deve retornar false comparado a null")
    void pagamentoComCartao_equals_null_retornaFalse() {
        PagamentoComCartao p = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        assertNotEquals(null, p);
    }

    @Test
    @DisplayName("PagamentoComCartao.equals deve retornar true comparado ao próprio objeto")
    void pagamentoComCartao_equals_mesmoObjeto_retornaTrue() {
        PagamentoComCartao p = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        assertEquals(p, p);
    }

    @Test
    @DisplayName("PagamentoComCartao.hashCode deve ser igual para mesmo id")
    void pagamentoComCartao_hashCode_mesmoId_igual() {
        PagamentoComCartao p1 = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        PagamentoComCartao p2 = new PagamentoComCartao(1L, Estado.PAGO, agora);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("PagamentoComCartao.hashCode deve diferir para ids distintos")
    void pagamentoComCartao_hashCode_idsDiferentes_difere() {
        PagamentoComCartao p1 = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        PagamentoComCartao p2 = new PagamentoComCartao(2L, Estado.PENDENTE, agora);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("PagamentoComCartao.toString deve conter o id")
    void pagamentoComCartao_toString_contemId() {
        PagamentoComCartao p = new PagamentoComCartao(42L, Estado.PENDENTE, agora);
        assertTrue(p.toString().contains("42"));
    }

    @Test
    @DisplayName("PagamentoComCartao.setPedido deve associar o pedido corretamente")
    void pagamentoComCartao_setPedido_associaCorretamente() {
        PagamentoComCartao p = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        Pedido pedido = new Pedido();
        p.setPedido(pedido);
        assertEquals(pedido, p.getPedido());
    }

    @Test
    @DisplayName("PagamentoComCartao construtor padrão deve criar instância sem nulos obrigatórios")
    void pagamentoComCartao_construtorPadrao_criaInstancia() {
        PagamentoComCartao p = new PagamentoComCartao();
        assertNotNull(p);
        assertNull(p.getId());
    }

    // ======================= PagamentoComDinheiro =======================

    @Test
    @DisplayName("PagamentoComDinheiro deve armazenar id e estado corretos")
    void pagamentoComDinheiro_construtor_deveArmazenarCampos() {
        PagamentoComDinheiro p = new PagamentoComDinheiro(2L, Estado.PAGO, agora);

        assertEquals(2L, p.getId());
        assertEquals(Estado.PAGO, p.getEstadoPagamento());
        assertEquals(agora, p.getDataPagamento());
    }

    @Test
    @DisplayName("PagamentoComDinheiro.confirmarPagamento deve retornar null enquanto não implementado")
    void pagamentoComDinheiro_confirmarPagamento_retornaNull() {
        PagamentoComDinheiro p = new PagamentoComDinheiro(2L, Estado.PAGO, agora);
        assertNull(p.confirmarPagamento());
    }

    @Test
    @DisplayName("PagamentoComDinheiro.setEstadoPagamento deve atualizar o estado")
    void pagamentoComDinheiro_setEstadoPagamento_deveAtualizar() {
        PagamentoComDinheiro p = new PagamentoComDinheiro(2L, Estado.PENDENTE, agora);
        p.setEstadoPagamento(Estado.PAGO);
        assertEquals(Estado.PAGO, p.getEstadoPagamento());
    }

    @Test
    @DisplayName("PagamentoComDinheiro.equals deve retornar true para mesmo id")
    void pagamentoComDinheiro_equals_mesmoId_retornaTrue() {
        PagamentoComDinheiro p1 = new PagamentoComDinheiro(3L, Estado.PENDENTE, agora);
        PagamentoComDinheiro p2 = new PagamentoComDinheiro(3L, Estado.PAGO, agora);
        assertEquals(p1, p2);
    }

    @Test
    @DisplayName("PagamentoComDinheiro.equals deve retornar false para ids diferentes")
    void pagamentoComDinheiro_equals_idsDiferentes_retornaFalse() {
        PagamentoComDinheiro p1 = new PagamentoComDinheiro(3L, Estado.PAGO, agora);
        PagamentoComDinheiro p2 = new PagamentoComDinheiro(4L, Estado.PAGO, agora);
        assertNotEquals(p1, p2);
    }

    @Test
    @DisplayName("PagamentoComDinheiro.hashCode deve ser igual para mesmo id")
    void pagamentoComDinheiro_hashCode_mesmoId_igual() {
        PagamentoComDinheiro p1 = new PagamentoComDinheiro(5L, Estado.PENDENTE, agora);
        PagamentoComDinheiro p2 = new PagamentoComDinheiro(5L, Estado.PAGO, agora);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("PagamentoComDinheiro.toString deve conter o id")
    void pagamentoComDinheiro_toString_contemId() {
        PagamentoComDinheiro p = new PagamentoComDinheiro(99L, Estado.PAGO, agora);
        assertTrue(p.toString().contains("99"));
    }

    @Test
    @DisplayName("PagamentoComDinheiro construtor padrão deve criar instância")
    void pagamentoComDinheiro_construtorPadrao_criaInstancia() {
        PagamentoComDinheiro p = new PagamentoComDinheiro();
        assertNotNull(p);
        assertNull(p.getId());
    }

    @Test
    @DisplayName("PagamentoComDinheiro.setDataPagamento deve atualizar a data")
    void pagamentoComDinheiro_setDataPagamento_atualiza() {
        PagamentoComDinheiro p = new PagamentoComDinheiro(1L, Estado.PENDENTE, agora);
        LocalDateTime novaData = agora.plusDays(1);
        p.setDataPagamento(novaData);
        assertEquals(novaData, p.getDataPagamento());
    }

    // ======================= Polimorfismo via interface IPagamento =======================

    @Test
    @DisplayName("PagamentoComCartao é instância de Pagamento e IPagamento")
    void pagamentoComCartao_implementaHierarquia() {
        PagamentoComCartao p = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        assertInstanceOf(Pagamento.class, p);
        assertInstanceOf(IPagamento.class, p);
    }

    @Test
    @DisplayName("PagamentoComDinheiro é instância de Pagamento e IPagamento")
    void pagamentoComDinheiro_implementaHierarquia() {
        PagamentoComDinheiro p = new PagamentoComDinheiro(2L, Estado.PAGO, agora);
        assertInstanceOf(Pagamento.class, p);
        assertInstanceOf(IPagamento.class, p);
    }

    @Test
    @DisplayName("Pagamentos de tipos diferentes com mesmo id não são iguais (classes distintas)")
    void pagamentos_tiposDiferentes_mesmoId_naoSaoIguais() {
        Pagamento cartao = new PagamentoComCartao(1L, Estado.PENDENTE, agora);
        Pagamento dinheiro = new PagamentoComDinheiro(1L, Estado.PENDENTE, agora);
        assertNotEquals(cartao, dinheiro);
    }
}
