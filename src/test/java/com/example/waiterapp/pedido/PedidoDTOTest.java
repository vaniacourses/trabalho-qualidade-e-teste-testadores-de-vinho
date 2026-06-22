package com.example.waiterapp.pedido;

import com.example.waiterapp.cliente.Cliente;
import com.example.waiterapp.enums.Estado;
import com.example.waiterapp.garcom.Garcom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para PedidoDTO")
class PedidoDTOTest {

    private static final LocalDateTime DATA = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    private Pedido pedido;
    private Cliente cliente;
    private Garcom garcom;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Ana", "ana@test.com", "11122233344", DATA);
        garcom = new Garcom(1L, "João", DATA, "99988877766");
        pedido = new Pedido();
        pedido.setId(10L);
        pedido.setDataCriacao(DATA);
        pedido.setEstado(Estado.PENDENTE);
        pedido.setPrecoTotal(50.0);
        pedido.setNotaAtendimento(5);
        pedido.setNotaPedido(4);
        pedido.setOpcoesExtras("sem cebola");
        pedido.setItems(new HashSet<>());
        pedido.setCliente(cliente);
        pedido.setGarcom(garcom);
        pedido.setPagamento(null);
    }

    @Test
    @DisplayName("construtor a partir de Pedido deve copiar todos os campos")
    void construtor_comPedido_deveCopiarTodosOsCampos() {
        PedidoDTO dto = new PedidoDTO(pedido);

        assertEquals(10L, dto.getId());
        assertEquals(DATA, dto.getDataCriacao());
        assertEquals(Estado.PENDENTE, dto.getEstado());
        assertEquals(50.0, dto.getPrecoTotal());
        assertEquals(5, dto.getNotaAtendimento());
        assertEquals(4, dto.getNotaPedido());
        assertEquals("sem cebola", dto.getOpcoesExtras());
        assertNotNull(dto.getItems());
        assertEquals(cliente, dto.getCliente());
        assertEquals(garcom, dto.getGarcom());
        assertNull(dto.getPagamento());
    }

    @Test
    @DisplayName("construtor padrão deve criar instância válida")
    void construtor_padrao_deveCriarInstancia() {
        PedidoDTO dto = new PedidoDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("setId deve atualizar o id")
    void setId_deveAtualizarId() {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(99L);
        assertEquals(99L, dto.getId());
    }

    @Test
    @DisplayName("setDataCriacao deve atualizar a data")
    void setDataCriacao_deveAtualizarData() {
        PedidoDTO dto = new PedidoDTO();
        dto.setDataCriacao(DATA);
        assertEquals(DATA, dto.getDataCriacao());
    }

    @Test
    @DisplayName("setEstado deve atualizar o estado")
    void setEstado_deveAtualizarEstado() {
        PedidoDTO dto = new PedidoDTO();
        dto.setEstado(Estado.CONCLUIDO);
        assertEquals(Estado.CONCLUIDO, dto.getEstado());
    }

    @Test
    @DisplayName("setPrecoTotal deve atualizar o preço total")
    void setPrecoTotal_deveAtualizarPreco() {
        PedidoDTO dto = new PedidoDTO();
        dto.setPrecoTotal(100.0);
        assertEquals(100.0, dto.getPrecoTotal());
    }

    @Test
    @DisplayName("setNotaAtendimento deve atualizar a nota de atendimento")
    void setNotaAtendimento_deveAtualizarNota() {
        PedidoDTO dto = new PedidoDTO();
        dto.setNotaAtendimento(3);
        assertEquals(3, dto.getNotaAtendimento());
    }

    @Test
    @DisplayName("setNotaPedido deve atualizar a nota do pedido")
    void setNotaPedido_deveAtualizarNota() {
        PedidoDTO dto = new PedidoDTO();
        dto.setNotaPedido(5);
        assertEquals(5, dto.getNotaPedido());
    }

    @Test
    @DisplayName("setOpcoesExtras deve atualizar as opções extras")
    void setOpcoesExtras_deveAtualizarOpcoes() {
        PedidoDTO dto = new PedidoDTO();
        dto.setOpcoesExtras("extra queijo");
        assertEquals("extra queijo", dto.getOpcoesExtras());
    }

    @Test
    @DisplayName("setItems deve atualizar os itens do pedido")
    void setItems_deveAtualizarItems() {
        PedidoDTO dto = new PedidoDTO();
        dto.setItems(new HashSet<>());
        assertNotNull(dto.getItems());
    }

    @Test
    @DisplayName("setCliente deve atualizar o cliente")
    void setCliente_deveAtualizarCliente() {
        PedidoDTO dto = new PedidoDTO();
        dto.setCliente(cliente);
        assertEquals(cliente, dto.getCliente());
    }

    @Test
    @DisplayName("setGarcom deve atualizar o garçom")
    void setGarcom_deveAtualizarGarcom() {
        PedidoDTO dto = new PedidoDTO();
        dto.setGarcom(garcom);
        assertEquals(garcom, dto.getGarcom());
    }
}
