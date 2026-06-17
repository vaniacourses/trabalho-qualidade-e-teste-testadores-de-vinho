package com.example.waiterapp.Pedido;

import com.example.waiterapp.Cliente.Cliente;
import com.example.waiterapp.Cliente.ClienteRepository;
import com.example.waiterapp.enums.Estado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Testes de integração para PedidoRepository (JPA + H2)")
class PedidoRepositoryIntegrationTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("save deve persistir o pedido e findById deve recuperá-lo")
    void save_pedidoValido_devePersistirERecuperarPorId() {
        // Arrange
        Cliente cliente = clienteRepository.save(
                new Cliente(null, "Maria Souza", "maria@test.com", "11122233344", LocalDateTime.now()));

        Pedido pedido = new Pedido(null, LocalDateTime.now(), Estado.EM_PREPARACAO, 50.0, null, null, null);
        pedido.setCliente(cliente);

        // Act
        Pedido salvo = pedidoRepository.save(pedido);
        Optional<Pedido> encontrado = pedidoRepository.findById(salvo.getId());

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals(50.0, encontrado.get().getPrecoTotal(), 0.001);
        assertEquals(cliente.getId(), encontrado.get().getCliente().getId());
    }

    @Test
    @DisplayName("findallByIdCliente deve retornar os pedidos do cliente ordenados por data de criação")
    void findallByIdCliente_clienteComPedidos_deveRetornarSeusPedidos() {
        // Arrange
        Cliente cliente = clienteRepository.save(
                new Cliente(null, "João Pereira", "joao.p@test.com", "55566677788", LocalDateTime.now()));

        Pedido pedido1 = new Pedido(null, LocalDateTime.now().minusDays(1), Estado.FECHADO, 30.0, null, null, null);
        pedido1.setCliente(cliente);

        Pedido pedido2 = new Pedido(null, LocalDateTime.now(), Estado.EM_PREPARACAO, 20.0, null, null, null);
        pedido2.setCliente(cliente);

        pedidoRepository.save(pedido1);
        pedidoRepository.save(pedido2);

        // Act
        List<Pedido> resultado = pedidoRepository.findallByIdCliente(cliente.getId());

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(pedido2.getDataCriacao(), resultado.get(0).getDataCriacao());
    }

    @Test
    @DisplayName("findallByIdCliente deve retornar lista vazia para cliente sem pedidos")
    void findallByIdCliente_clienteSemPedidos_deveRetornarListaVazia() {
        // Arrange
        Cliente cliente = clienteRepository.save(
                new Cliente(null, "Ana Lima", "ana.lima@test.com", "99988877766", LocalDateTime.now()));

        // Act
        List<Pedido> resultado = pedidoRepository.findallByIdCliente(cliente.getId());

        // Assert
        assertTrue(resultado.isEmpty());
    }
}
