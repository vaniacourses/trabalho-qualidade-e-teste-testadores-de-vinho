package com.example.waiterapp.pedido;

import com.example.waiterapp.cliente.Cliente;
import com.example.waiterapp.cliente.ClienteService;
import com.example.waiterapp.item.Item;
import com.example.waiterapp.item.ItemService;
import com.example.waiterapp.itempedido.ItemPedido;
import com.example.waiterapp.itempedido.ItemPedidoRepository;
import com.example.waiterapp.pagamento.PagamentoRepository;
import com.example.waiterapp.enums.Estado;
import com.example.waiterapp.exceptions.ObjectNotFoundException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para PedidoService")
class PedidoServiceTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    @Mock private PedidoRepository pedidoRepository;
    @Mock private PagamentoRepository pagamentoRepository;
    @Mock private ItemPedidoRepository itemPedidoRepository;
    @Mock private ItemService itemService;
    @Mock private ClienteService clienteService;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private Cliente cliente;
    private Item item;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "João Silva", "joao@test.com", "12345678901", FIXED_DATE);
        item    = new Item(1L, "Pizza Margherita", "Pizza clássica", FIXED_DATE, 35.0);
        pedido  = new Pedido(1L, FIXED_DATE, Estado.EM_PREPARACAO, 35.0, null, null, null);
        pedido.setCliente(cliente);
    }

    // ======================= listaPedidos() =======================

    @Test
    @DisplayName("listaPedidos deve retornar todos os pedidos cadastrados")
    void listaPedidos_deveRetornarTodosOsPedidos() {
        // Arrange
        List<Pedido> pedidos = Arrays.asList(pedido, new Pedido());
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        // Act
        List<Pedido> resultado = pedidoService.listaPedidos();

        // Assert
        assertEquals(2, resultado.size());
        verify(pedidoRepository).findAll();
    }

    @Test
    @DisplayName("listaPedidos deve retornar lista vazia quando não há pedidos")
    void listaPedidos_semPedidosCadastrados_deveRetornarListaVazia() {
        // Arrange
        when(pedidoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Pedido> resultado = pedidoService.listaPedidos();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ======================= listaPedidosByIdCliente() =======================

    @Test
    @DisplayName("listaPedidosByIdCliente deve retornar os pedidos do cliente informado")
    void listaPedidosByIdCliente_clienteExistente_deveRetornarSeusPedidos() {
        // Arrange
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoRepository.findallByIdCliente(1L)).thenReturn(pedidos);

        // Act
        List<Pedido> resultado = pedidoService.listaPedidosByIdCliente(1L);

        // Assert
        assertEquals(1, resultado.size());
        verify(pedidoRepository).findallByIdCliente(1L);
    }

    @Test
    @DisplayName("listaPedidosByIdCliente deve retornar lista vazia para cliente sem pedidos")
    void listaPedidosByIdCliente_clienteSemPedidos_deveRetornarListaVazia() {
        // Arrange
        when(pedidoRepository.findallByIdCliente(2L)).thenReturn(new ArrayList<>());

        // Act
        List<Pedido> resultado = pedidoService.listaPedidosByIdCliente(2L);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ======================= retornaPedidoById() =======================

    @Test
    @DisplayName("retornaPedidoById deve retornar o pedido quando encontrado pelo id")
    void retornaPedidoById_idExistente_deveRetornarPedido() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act
        Pedido resultado = pedidoService.retornaPedidoById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("retornaPedidoById deve retornar null quando o pedido não é encontrado")
    void retornaPedidoById_idInexistente_deveRetornarNull() {
        // Arrange
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Pedido resultado = pedidoService.retornaPedidoById(99L);

        // Assert
        assertNull(resultado);
    }

    // ======================= inserePedido() =======================

    @Test
    @DisplayName("inserePedido deve criar pedido com estado EM_PREPARACAO e id nulo")
    void inserePedido_pedidoValido_deveCriarComEstadoCorretoEIdNulo() {
        // Arrange
        Cliente clienteRef = new Cliente();
        clienteRef.setId(1L);

        Item itemRef = new Item();
        itemRef.setId(1L);

        ItemPedido ip = new ItemPedido(null, itemRef, 2);
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip);

        Pedido novoPedido = new Pedido();
        novoPedido.setId(5L);
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(items);

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemService.retornaItemById(1L)).thenReturn(item);
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Pedido resultado = pedidoService.inserePedido(novoPedido);

        // Assert
        assertNull(resultado.getId());
        assertEquals(Estado.EM_PREPARACAO, resultado.getEstado());
        assertNotNull(resultado.getDataCriacao());
        assertEquals(cliente, resultado.getCliente());
    }

    @Test
    @DisplayName("inserePedido deve calcular o preço total a partir dos itens do pedido")
    void inserePedido_comUmItem_deveCalcularPrecoTotalCorretamente() {
        // Arrange – item custa 35.0, quantidade 2 → total esperado 70.0
        Cliente clienteRef = new Cliente();
        clienteRef.setId(1L);

        Item itemRef = new Item();
        itemRef.setId(1L);

        ItemPedido ip = new ItemPedido(null, itemRef, 2);
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip);

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(items);

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemService.retornaItemById(1L)).thenReturn(item);
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Pedido resultado = pedidoService.inserePedido(novoPedido);

        // Assert
        assertEquals(70.0, resultado.getPrecoTotal(), 0.001);
    }

    @Test
    @DisplayName("inserePedido sem itens deve ter preço total zero")
    void inserePedido_semItens_deveDefinirPrecoTotalComoZero() {
        // Arrange
        Cliente clienteRef = new Cliente();
        clienteRef.setId(1L);

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(new HashSet<>());

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Pedido resultado = pedidoService.inserePedido(novoPedido);

        // Assert
        assertEquals(0.0, resultado.getPrecoTotal(), 0.001);
    }

    @Test
    @DisplayName("inserePedido deve salvar o pedido no repositório exatamente duas vezes")
    void inserePedido_deveInvocarSaveNoRepositorioDuasVezes() {
        // Arrange
        Cliente clienteRef = new Cliente();
        clienteRef.setId(1L);

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(new HashSet<>());

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        pedidoService.inserePedido(novoPedido);

        // Assert
        verify(pedidoRepository, times(2)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("inserePedido deve buscar o item pelo id para cada ItemPedido")
    void inserePedido_comItem_deveBuscarItemPeloId() {
        // Arrange
        Cliente clienteRef = new Cliente();
        clienteRef.setId(1L);

        Item itemRef = new Item();
        itemRef.setId(1L);

        ItemPedido ip = new ItemPedido(null, itemRef, 1);
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip);

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(items);

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemService.retornaItemById(1L)).thenReturn(item);
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Pedido resultado = pedidoService.inserePedido(novoPedido);

        // Assert
        verify(itemService).retornaItemById(1L);
        ItemPedido itemPedidoSalvo = resultado.getItems().iterator().next();
        assertEquals(item.getPreco(), itemPedidoSalvo.getPreco());
        assertSame(resultado, itemPedidoSalvo.getPedido());
    }

    @Test
    @DisplayName("inserePedido deve associar o cliente correto ao pedido")
    void inserePedido_deveAssociarClienteAoPedido() {
        // Arrange
        Cliente clienteRef = new Cliente();
        clienteRef.setId(1L);

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(new HashSet<>());

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Pedido resultado = pedidoService.inserePedido(novoPedido);

        // Assert — verifica o objeto COMPLETO (não apenas id), matando o mutante setCliente
        assertEquals(cliente, resultado.getCliente());
        assertEquals("João Silva", resultado.getCliente().getNome());
        verify(clienteService).retornaClienteById(1L);
    }

    @Test
    @DisplayName("inserePedido deve persistir os ItemPedido via saveAll")
    void inserePedido_comItens_deveInvocarSaveAllNoRepositorio() {
        // Arrange
        Set<ItemPedido> items = new HashSet<>();
        items.add(itemPedidoComItem(1L, 1));
        Pedido novoPedido = criarPedidoParaInserir(1L, items);

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemService.retornaItemById(1L)).thenReturn(item);
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        pedidoService.inserePedido(novoPedido);

        // Assert
        verify(itemPedidoRepository).saveAll(any());
    }

    @Test
    @DisplayName("inserePedido deve associar o pedido correto em cada ItemPedido")
    void inserePedido_comItem_deveAssociarPedidoAoItemPedido() {
        // Arrange
        Cliente clienteRef = new Cliente();
        clienteRef.setId(1L);

        Item itemRef = new Item();
        itemRef.setId(1L);

        ItemPedido ip = new ItemPedido(null, itemRef, 1);
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip);

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(items);

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemService.retornaItemById(1L)).thenReturn(item);
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Pedido resultado = pedidoService.inserePedido(novoPedido);

        // Assert — ip.setPedido(pedido) deve ter sido chamado; verifica via getPedido()
        ItemPedido ipResultado = resultado.getItems().iterator().next();
        assertNotNull(ipResultado.getPedido(), "ItemPedido deve ter o pedido associado após inserção");
    }

    @Test
    @DisplayName("inserePedido deve registrar o preço no ItemPedido")
    void inserePedido_comItem_deveSetarPrecoNoItemPedido() {
        // Arrange
        Cliente clienteRef = new Cliente();
        clienteRef.setId(1L);

        Item itemRef = new Item();
        itemRef.setId(1L);

        ItemPedido ip = new ItemPedido(null, itemRef, 1);
        Set<ItemPedido> items = new HashSet<>();
        items.add(ip);

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(items);

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemService.retornaItemById(1L)).thenReturn(item);
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        pedidoService.inserePedido(novoPedido);

        // Assert — ip.setPreco() deve ter sido chamado com o preço do item (35.0)
        assertEquals(35.0, ip.getPreco(), 0.001,
                "ItemPedido deve registrar o preço do item para persistência");
    }

    @Test
    @DisplayName("inserePedido com múltiplos itens deve calcular o preço total e buscar cada item")
    void inserePedido_comMultiplosItens_deveCalcularTotalEBuscarCadaItem() {
        // Arrange – pizza 35×2 + suco 10×3 = 100
        Item suco = new Item(2L, "Suco", "Natural", FIXED_DATE, 10.0);
        Set<ItemPedido> items = new HashSet<>();
        items.add(itemPedidoComItem(1L, 2));
        items.add(itemPedidoComItem(2L, 3));

        Pedido novoPedido = criarPedidoParaInserir(1L, items);

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemService.retornaItemById(1L)).thenReturn(item);
        when(itemService.retornaItemById(2L)).thenReturn(suco);
        when(itemPedidoRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Pedido resultado = pedidoService.inserePedido(novoPedido);

        // Assert
        assertEquals(100.0, resultado.getPrecoTotal(), 0.001);
        verify(itemService).retornaItemById(1L);
        verify(itemService).retornaItemById(2L);
        verify(itemPedidoRepository).saveAll(any());
    }

    @Test
    @DisplayName("inserePedido deve propagar ObjectNotFoundException quando o item não existe")
    void inserePedido_itemInexistente_deveLancarObjectNotFoundException() {
        // Arrange
        Set<ItemPedido> items = new HashSet<>();
        items.add(itemPedidoComItem(99L, 1));
        Pedido novoPedido = criarPedidoParaInserir(1L, items);

        when(clienteService.retornaClienteById(1L)).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemService.retornaItemById(99L))
            .thenThrow(new ObjectNotFoundException("Objeto nao encontrado! ID: 99"));

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> pedidoService.inserePedido(novoPedido));
        verify(itemPedidoRepository, never()).saveAll(any());
    }

    // ======================= atualizaPedido() =======================

    @Test
    @DisplayName("atualizaPedido deve salvar e retornar o pedido atualizado")
    void atualizaPedido_pedidoExistente_deveSalvarERetornarPedidoAtualizado() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.atualizaPedido(pedido);

        // Assert
        assertNotNull(resultado);
        verify(pedidoRepository).save(pedido);
    }

    // ======================= apagaPedido() =======================

    @Test
    @DisplayName("apagaPedido deve excluir o pedido sem lançar exceção")
    void apagaPedido_pedidoExistente_deveExcluirSemExcecao() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        doNothing().when(pedidoRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> pedidoService.apagaPedido(1L));
        verify(pedidoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("apagaPedido deve lançar DataIntegrityViolationException quando há violação referencial")
    void apagaPedido_comViolacaoReferencial_deveLancarDataIntegrityViolationException() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        doThrow(new DataIntegrityViolationException("FK violation"))
            .when(pedidoRepository).deleteById(1L);

        // Act & Assert
        DataIntegrityViolationException ex = assertThrows(
            DataIntegrityViolationException.class,
            () -> pedidoService.apagaPedido(1L)
        );
        assertTrue(ex.getMessage().contains("Não é possível excluir esse pedido"));
    }

    private Pedido criarPedidoParaInserir(Long clienteId, Set<ItemPedido> items) {
        Cliente clienteRef = new Cliente();
        clienteRef.setId(clienteId);

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(clienteRef);
        novoPedido.setItems(items);
        return novoPedido;
    }

    private ItemPedido itemPedidoComItem(Long itemId, int quantidade) {
        Item itemRef = new Item();
        itemRef.setId(itemId);
        return new ItemPedido(null, itemRef, quantidade);
    }
}
