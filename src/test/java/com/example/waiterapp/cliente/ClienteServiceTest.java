package com.example.waiterapp.cliente;

import com.example.waiterapp.pedido.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para ClienteService")
class ClienteServiceTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    @Mock private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Maria Santos", "maria@test.com", "98765432100", FIXED_DATE);
    }

    // ======================= listaClientes() =======================

    @Test
    @DisplayName("listaClientes deve retornar todos os clientes cadastrados")
    void listaClientes_deveRetornarTodosOsClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(
            cliente,
            new Cliente(2L, "João Pereira", "joao@test.com", "11111111111", FIXED_DATE)
        );
        when(clienteRepository.findAll()).thenReturn(clientes);

        // Act
        List<Cliente> resultado = clienteService.listaClientes();

        // Assert
        assertEquals(2, resultado.size());
        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("listaClientes deve retornar lista vazia quando não há clientes cadastrados")
    void listaClientes_semClientesCadastrados_deveRetornarListaVazia() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Cliente> resultado = clienteService.listaClientes();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ======================= retornaClienteById() =======================

    @Test
    @DisplayName("retornaClienteById deve retornar o cliente quando encontrado pelo id")
    void retornaClienteById_idExistente_deveRetornarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.retornaClienteById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Maria Santos", resultado.getNome());
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("retornaClienteById deve retornar null quando o id não existe")
    void retornaClienteById_idInexistente_deveRetornarNull() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Cliente resultado = clienteService.retornaClienteById(99L);

        // Assert
        assertNull(resultado);
    }

    // ======================= retornaClienteByCpf() =======================

    @Test
    @DisplayName("retornaClienteByCpf deve retornar o cliente com CPF existente")
    void retornaClienteByCpf_cpfExistente_deveRetornarCliente() {
        // Arrange
        when(clienteRepository.findByCpf("98765432100")).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.retornaClienteByCpf("98765432100");

        // Assert
        assertNotNull(resultado);
        assertEquals("98765432100", resultado.getCpf());
    }

    @Test
    @DisplayName("retornaClienteByCpf deve retornar null para CPF não cadastrado")
    void retornaClienteByCpf_cpfNaoCadastrado_deveRetornarNull() {
        // Arrange
        when(clienteRepository.findByCpf("00000000000")).thenReturn(Optional.empty());

        // Act
        Cliente resultado = clienteService.retornaClienteByCpf("00000000000");

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("retornaClienteByCpf deve retornar null para CPF vazio")
    void retornaClienteByCpf_cpfVazio_deveRetornarNull() {
        // Arrange
        when(clienteRepository.findByCpf("")).thenReturn(Optional.empty());

        // Act
        Cliente resultado = clienteService.retornaClienteByCpf("");

        // Assert
        assertNull(resultado);
    }

    // ======================= insereCliente() =======================

    @Test
    @DisplayName("insereCliente deve forçar id como null antes de persistir")
    void insereCliente_deveSetarIdNuloAntesDePersisteir() {
        // Arrange
        cliente.setId(50L);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Cliente resultado = clienteService.insereCliente(cliente);

        // Assert
        assertNull(resultado.getId());
    }

    @Test
    @DisplayName("insereCliente deve definir data de criação automaticamente")
    void insereCliente_deveDefinirDataCriacaoAutomaticamente() {
        // Arrange
        LocalDateTime antes = FIXED_DATE.minusSeconds(1);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Cliente resultado = clienteService.insereCliente(cliente);

        // Assert
        assertNotNull(resultado.getDataCriacao());
        assertTrue(resultado.getDataCriacao().isAfter(antes));
    }

    @Test
    @DisplayName("insereCliente deve preservar nome, email e CPF ao salvar")
    void insereCliente_devePreservarDadosPessoais() {
        // Arrange
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Cliente resultado = clienteService.insereCliente(cliente);

        // Assert
        assertEquals("Maria Santos", resultado.getNome());
        assertEquals("maria@test.com", resultado.getEmail());
        assertEquals("98765432100", resultado.getCpf());
    }

    // ======================= atualizaCliente() =======================

    @Test
    @DisplayName("atualizaCliente deve persistir e retornar o cliente atualizado")
    void atualizaCliente_clienteExistente_devePersistirERetornar() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.atualizaCliente(cliente);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("atualizaCliente com id inexistente ainda persiste o cliente (bug documentado)")
    void atualizaCliente_idInexistente_devePersistirMesmoAssim() {
        // Arrange – retornaClienteById retorna null, mas o serviço não valida o retorno
        Cliente clienteInexistente = new Cliente(99L, "Nome", "email@test.com", "11111111111", FIXED_DATE);
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        when(clienteRepository.save(clienteInexistente)).thenReturn(clienteInexistente);

        // Act
        Cliente resultado = clienteService.atualizaCliente(clienteInexistente);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository).save(clienteInexistente);
    }

    // ======================= apagaCliente() =======================

    @Test
    @DisplayName("apagaCliente deve excluir cliente existente sem lançar exceção")
    void apagaCliente_clienteExistente_deveExcluirSemExcecao() {
        // Arrange
        doNothing().when(clienteRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> clienteService.apagaCliente(1L));
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    @DisplayName("apagaCliente deve lançar DataIntegrityViolationException quando há pedidos associados")
    void apagaCliente_comPedidosAssociados_deveLancarDataIntegrityViolationException() {
        // Arrange
        doThrow(new DataIntegrityViolationException("FK violation"))
            .when(clienteRepository).deleteById(1L);

        // Act & Assert
        DataIntegrityViolationException ex = assertThrows(
            DataIntegrityViolationException.class,
            () -> clienteService.apagaCliente(1L)
        );
        assertTrue(ex.getMessage().contains("Não é possível excluir esse cliente"));
    }

    @Test
    @DisplayName("apagaCliente com id inexistente ainda tenta excluir sem verificar existência (bug documentado)")
    void apagaCliente_idInexistente_deveExcluirSemVerificarExistencia() {
        // Arrange – apagaCliente não chama retornaClienteById antes de deleteById
        doNothing().when(clienteRepository).deleteById(99L);

        // Act & Assert
        assertDoesNotThrow(() -> clienteService.apagaCliente(99L));
        verify(clienteRepository).deleteById(99L);
        verify(clienteRepository, never()).findById(any());
    }

    // ======================= retornaPedidosCliente() =======================

    @Test
    @DisplayName("retornaPedidosCliente deve retornar a lista de pedidos do cliente")
    void retornaPedidosCliente_clienteExistente_deveRetornarSeusPedidos() {
        // Arrange
        List<Pedido> pedidos = Arrays.asList(new Pedido(), new Pedido());
        cliente.setPedidos(pedidos);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        List<Pedido> resultado = clienteService.retornaPedidosCliente(1L);

        // Assert
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("retornaPedidosCliente deve retornar lista vazia para cliente sem pedidos")
    void retornaPedidosCliente_clienteSemPedidos_deveRetornarListaVazia() {
        // Arrange
        cliente.setPedidos(new ArrayList<>());
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        List<Pedido> resultado = clienteService.retornaPedidosCliente(1L);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("retornaPedidosCliente deve lançar NullPointerException quando cliente não existe (bug documentado)")
    void retornaPedidosCliente_clienteNaoExistente_deveLancarNullPointerException() {
        // Arrange – retornaClienteById retorna null quando não encontrado
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert – NPE porque o serviço chama .getPedidos() em um objeto null
        assertThrows(NullPointerException.class,
            () -> clienteService.retornaPedidosCliente(99L));
    }

    // ======================= inserePedidosCliente() =======================

    @Test
    @DisplayName("inserePedidosCliente deve substituir a lista de pedidos do cliente")
    void inserePedidosCliente_clienteExistente_deveSubstituirSeusPedidos() {
        // Arrange
        List<Pedido> novosPedidos = Arrays.asList(new Pedido());
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        clienteService.inserePedidosCliente(1L, novosPedidos);

        // Assert
        assertEquals(novosPedidos, cliente.getPedidos());
    }

    @Test
    @DisplayName("inserePedidosCliente deve aceitar lista vazia de pedidos")
    void inserePedidosCliente_listaPedidosVazia_deveAceitarEAtribuir() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        clienteService.inserePedidosCliente(1L, new ArrayList<>());

        // Assert
        assertTrue(cliente.getPedidos().isEmpty());
    }

    @Test
    @DisplayName("inserePedidosCliente deve lançar NullPointerException quando cliente não existe (bug documentado)")
    void inserePedidosCliente_clienteInexistente_deveLancarNullPointerException() {
        // Arrange – retornaClienteById retorna null quando não encontrado
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert – NPE porque o serviço chama setPedidos em um objeto null
        assertThrows(NullPointerException.class,
            () -> clienteService.inserePedidosCliente(99L, new ArrayList<>()));
    }

    // ======================= transformarDTO() =======================

    @Test
    @DisplayName("transformarDTO deve mapear corretamente todos os campos do DTO")
    void transformarDTO_dtoComTodosCampos_deveMappearCorretamente() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.of(2024, Month.JANUARY, 10, 8, 30);
        ClienteDTO dto = new ClienteDTO();
        dto.setId(3L);
        dto.setNome("Carlos Oliveira");
        dto.setEmail("carlos@test.com");
        dto.setCpf("55555555555");
        dto.setdataCriacao(dataCriacao);

        // Act
        Cliente resultado = clienteService.transformarDTO(dto);

        // Assert
        assertEquals(3L, resultado.getId());
        assertEquals("Carlos Oliveira", resultado.getNome());
        assertEquals("carlos@test.com", resultado.getEmail());
        assertEquals("55555555555", resultado.getCpf());
        assertEquals(dataCriacao, resultado.getDataCriacao());
    }

    @Test
    @DisplayName("transformarDTO deve preservar a lista de pedidos do DTO")
    void transformarDTO_comPedidos_devePreservarPedidos() {
        // Arrange
        ClienteDTO dto = new ClienteDTO(cliente);
        List<Pedido> pedidos = Arrays.asList(new Pedido());
        dto.setPedidos(pedidos);

        // Act
        Cliente resultado = clienteService.transformarDTO(dto);

        // Assert
        assertEquals(pedidos, resultado.getPedidos());
    }
}
