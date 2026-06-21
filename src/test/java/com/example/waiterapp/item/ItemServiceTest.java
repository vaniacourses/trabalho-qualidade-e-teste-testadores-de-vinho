package com.example.waiterapp.item;

import com.example.waiterapp.cardapio.Cardapio;
import com.example.waiterapp.itempedido.ItemPedido;
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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para ItemService")
class ItemServiceTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    @Mock private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "Pizza Margherita", "Pizza clássica com tomate e mozzarella",
                FIXED_DATE, 35.0);
    }

    // ======================= listaItens() =======================

    @Test
    @DisplayName("listaItens deve retornar todos os itens cadastrados")
    void listaItens_deveRetornarTodosOsItens() {
        // Arrange
        List<Item> itens = Arrays.asList(item, new Item(2L, "Suco", "Desc", FIXED_DATE, 8.0));
        when(itemRepository.findAll()).thenReturn(itens);

        // Act
        List<Item> resultado = itemService.listaItens();

        // Assert
        assertEquals(2, resultado.size());
        verify(itemRepository).findAll();
    }

    @Test
    @DisplayName("listaItens deve retornar lista vazia quando não há itens cadastrados")
    void listaItens_semItensCadastrados_deveRetornarListaVazia() {
        // Arrange
        when(itemRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Item> resultado = itemService.listaItens();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ======================= retornaItemById() =======================

    @Test
    @DisplayName("retornaItemById deve retornar o item quando encontrado pelo id")
    void retornaItemById_idExistente_deveRetornarItem() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Act
        Item resultado = itemService.retornaItemById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pizza Margherita", resultado.getNome());
    }

    @Test
    @DisplayName("retornaItemById deve lançar ObjectNotFoundException quando o id não existe")
    void retornaItemById_idInexistente_deveLancarObjectNotFoundException() {
        // Arrange
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException ex = assertThrows(
            ObjectNotFoundException.class,
            () -> itemService.retornaItemById(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("retornaItemById deve incluir o tipo Item na mensagem de erro")
    void retornaItemById_idInexistente_mensagemDeErroDeveConterTipo() {
        // Arrange
        when(itemRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException ex = assertThrows(
            ObjectNotFoundException.class,
            () -> itemService.retornaItemById(0L)
        );
        assertTrue(ex.getMessage().contains(Item.class.getName()));
    }

    // ======================= insereItem() =======================

    @Test
    @DisplayName("insereItem deve forçar id como null antes de persistir")
    void insereItem_deveSetarIdNuloAntesDePersisteir() {
        // Arrange
        item.setId(100L);
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Item resultado = itemService.insereItem(item);

        // Assert
        assertNull(resultado.getId());
    }

    @Test
    @DisplayName("insereItem deve definir data de criação automaticamente")
    void insereItem_deveDefinirDataCriacaoAutomaticamente() {
        // Arrange
        item.setDataCriacao(null);
        LocalDateTime antes = FIXED_DATE.minusSeconds(1);
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Item resultado = itemService.insereItem(item);

        // Assert
        assertNotNull(resultado.getDataCriacao());
        assertTrue(resultado.getDataCriacao().isAfter(antes));
    }

    @Test
    @DisplayName("insereItem deve invocar o repositório para persistir o item")
    void insereItem_deveInvocarRepositorioSave() {
        // Arrange
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // Act
        itemService.insereItem(item);

        // Assert
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    @DisplayName("insereItem deve preservar nome e preço do item ao salvar")
    void insereItem_devePreservarNomeEPreco() {
        // Arrange
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Item resultado = itemService.insereItem(item);

        // Assert
        assertEquals("Pizza Margherita", resultado.getNome());
        assertEquals(35.0, resultado.getPreco(), 0.001);
    }

    // ======================= atualizaItem() =======================

    @Test
    @DisplayName("atualizaItem deve persistir e retornar o item atualizado")
    void atualizaItem_itemExistente_devePersistirERetornarItemAtualizado() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        // Act
        Item resultado = itemService.atualizaItem(item);

        // Assert
        assertNotNull(resultado);
        verify(itemRepository).save(item);
    }

    @Test
    @DisplayName("atualizaItem deve lançar ObjectNotFoundException quando o item não existe")
    void atualizaItem_itemNaoExistente_deveLancarObjectNotFoundException() {
        // Arrange
        item.setId(99L);
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> itemService.atualizaItem(item));
    }

    // ======================= apagaItem() =======================

    @Test
    @DisplayName("apagaItem deve excluir item existente sem lançar exceção")
    void apagaItem_itemExistente_deveExcluirSemExcecao() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> itemService.apagaItem(1L));
        verify(itemRepository).deleteById(1L);
    }

    @Test
    @DisplayName("apagaItem deve lançar ObjectNotFoundException quando o item não existe")
    void apagaItem_itemNaoExistente_deveLancarObjectNotFoundException() {
        // Arrange
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> itemService.apagaItem(99L));
    }

    @Test
    @DisplayName("apagaItem deve lançar DataIntegrityViolationException quando há pedidos associados")
    void apagaItem_comPedidosAssociados_deveLancarDataIntegrityViolationException() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        doThrow(new DataIntegrityViolationException("FK violation"))
            .when(itemRepository).deleteById(1L);

        // Act & Assert
        DataIntegrityViolationException ex = assertThrows(
            DataIntegrityViolationException.class,
            () -> itemService.apagaItem(1L)
        );
        assertTrue(ex.getMessage().contains("Não é possível excluir esse item"));
    }

    // ======================= transformarDTO() =======================

    @Test
    @DisplayName("transformarDTO deve mapear corretamente todos os campos escalares do DTO")
    void transformarDTO_dtoComTodosCampos_deveMappearCorretamente() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.of(2024, Month.MARCH, 15, 10, 0);
        ItemDTO dto = new ItemDTO();
        dto.setId(5L);
        dto.setNome("Salada Caesar");
        dto.setDescricao("Salada fresca com croutons");
        dto.setDataCriacao(dataCriacao);
        dto.setPreco(18.0);

        // Act
        Item resultado = itemService.transformarDTO(dto);

        // Assert
        assertEquals(5L, resultado.getId());
        assertEquals("Salada Caesar", resultado.getNome());
        assertEquals("Salada fresca com croutons", resultado.getDescricao());
        assertEquals(dataCriacao, resultado.getDataCriacao());
        assertEquals(18.0, resultado.getPreco(), 0.001);
    }

    @Test
    @DisplayName("transformarDTO deve inicializar coleções mesmo quando DTO tem coleções vazias")
    void transformarDTO_dtoColecoesVazias_deveInicializarColecoesNoItem() {
        // Arrange
        ItemDTO dto = new ItemDTO();
        dto.setId(1L);
        dto.setNome("Item");
        dto.setPreco(10.0);

        // Act
        Item resultado = itemService.transformarDTO(dto);

        // Assert
        assertNotNull(resultado.getCardapios());
        assertNotNull(resultado.getItems());
    }

    @Test
    @DisplayName("transformarDTO com preço zero deve mapear preço como zero")
    void transformarDTO_precoZero_deveMappearPrecoZero() {
        // Arrange
        ItemDTO dto = new ItemDTO();
        dto.setId(2L);
        dto.setNome("Item Gratuito");
        dto.setPreco(0.0);

        // Act
        Item resultado = itemService.transformarDTO(dto);

        // Assert
        assertEquals(0.0, resultado.getPreco(), 0.001);
    }

    @Test
    @DisplayName("transformarDTO com id null deve mapear id como null")
    void transformarDTO_idNull_deveMappearIdNull() {
        // Arrange
        ItemDTO dto = new ItemDTO();
        dto.setId(null);
        dto.setNome("Novo Item");
        dto.setPreco(5.0);

        // Act
        Item resultado = itemService.transformarDTO(dto);

        // Assert
        assertNull(resultado.getId());
    }

    @Test
    @DisplayName("transformarDTO deve transferir a lista de cardápios do DTO para o item")
    void transformarDTO_comCardapios_deveMappearCardapios() {
        // Arrange
        Cardapio cardapio = new Cardapio();
        List<Cardapio> cardapios = List.of(cardapio);

        ItemDTO dto = new ItemDTO();
        dto.setId(1L);
        dto.setNome("Pizza");
        dto.setPreco(30.0);
        dto.setCardapios(cardapios);

        // Act
        Item resultado = itemService.transformarDTO(dto);

        // Assert — verifica o conteúdo, não apenas não-nulo; mata o mutante setCardapios()
        assertEquals(1, resultado.getCardapios().size());
        assertEquals(cardapio, resultado.getCardapios().get(0));
    }

    @Test
    @DisplayName("transformarDTO deve transferir o conjunto de items do DTO para o item")
    void transformarDTO_comItems_deveMappearItems() {
        // Arrange
        ItemPedido ip = new ItemPedido();
        Set<ItemPedido> itensPedido = new HashSet<>();
        itensPedido.add(ip);

        ItemDTO dto = new ItemDTO();
        dto.setId(1L);
        dto.setNome("Pizza");
        dto.setPreco(30.0);
        dto.setItems(itensPedido);

        // Act
        Item resultado = itemService.transformarDTO(dto);

        // Assert — verifica o conteúdo, não apenas não-nulo; mata o mutante setItems()
        assertEquals(1, resultado.getItems().size());
        assertTrue(resultado.getItems().contains(ip));
    }
}
