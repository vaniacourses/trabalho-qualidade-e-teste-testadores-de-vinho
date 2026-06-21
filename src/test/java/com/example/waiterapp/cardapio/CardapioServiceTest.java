package com.example.waiterapp.cardapio;

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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para CardapioService")
class CardapioServiceTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    @Mock private CardapioRepository cardapioRepository;

    @InjectMocks
    private CardapioService cardapioService;

    private Cardapio cardapio;

    @BeforeEach
    void setUp() {
        cardapio = new Cardapio(1L, FIXED_DATE, "Cardápio do Almoço",
                "Pratos principais para o almoço");
    }

    // ======================= listaCardapios() =======================

    @Test
    @DisplayName("listaCardapios deve retornar todos os cardápios cadastrados")
    void listaCardapios_deveRetornarTodosOsCardapios() {
        // Arrange
        List<Cardapio> cardapios = Arrays.asList(
            cardapio,
            new Cardapio(2L, FIXED_DATE, "Cardápio Noturno", "Jantar")
        );
        when(cardapioRepository.findAll()).thenReturn(cardapios);

        // Act
        List<Cardapio> resultado = cardapioService.listaCardapios();

        // Assert
        assertEquals(2, resultado.size());
        verify(cardapioRepository).findAll();
    }

    @Test
    @DisplayName("listaCardapios deve retornar lista vazia quando não há cardápios cadastrados")
    void listaCardapios_semCardapiosCadastrados_deveRetornarListaVazia() {
        // Arrange
        when(cardapioRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Cardapio> resultado = cardapioService.listaCardapios();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ======================= retornaCardapioById() =======================

    @Test
    @DisplayName("retornaCardapioById deve retornar o cardápio quando encontrado pelo id")
    void retornaCardapioById_idExistente_deveRetornarCardapio() {
        // Arrange
        when(cardapioRepository.findById(1L)).thenReturn(Optional.of(cardapio));

        // Act
        Cardapio resultado = cardapioService.retornaCardapioById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Cardápio do Almoço", resultado.getTitulo());
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("retornaCardapioById deve lançar ObjectNotFoundException quando o id não existe")
    void retornaCardapioById_idInexistente_deveLancarObjectNotFoundException() {
        // Arrange
        when(cardapioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException ex = assertThrows(
            ObjectNotFoundException.class,
            () -> cardapioService.retornaCardapioById(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("retornaCardapioById deve incluir o tipo Cardapio na mensagem de erro")
    void retornaCardapioById_idInexistente_mensagemDeErroDeveConterTipo() {
        // Arrange
        when(cardapioRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException ex = assertThrows(
            ObjectNotFoundException.class,
            () -> cardapioService.retornaCardapioById(0L)
        );
        assertTrue(ex.getMessage().contains(Cardapio.class.getName()));
    }

    // ======================= insereCardapio() =======================

    @Test
    @DisplayName("insereCardapio deve forçar id como null antes de persistir")
    void insereCardapio_deveSetarIdNuloAntesDePersisteir() {
        // Arrange
        cardapio.setId(100L);
        when(cardapioRepository.save(any(Cardapio.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Cardapio resultado = cardapioService.insereCardapio(cardapio);

        // Assert
        assertNull(resultado.getId());
    }

    @Test
    @DisplayName("insereCardapio deve definir data de criação automaticamente")
    void insereCardapio_deveDefinirDataCriacaoAutomaticamente() {
        // Arrange
        LocalDateTime antes = FIXED_DATE;
        when(cardapioRepository.save(any(Cardapio.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Cardapio resultado = cardapioService.insereCardapio(cardapio);

        // Assert
        assertNotNull(resultado.getDataCriacao());
        assertTrue(resultado.getDataCriacao().isAfter(antes));
    }

    @Test
    @DisplayName("insereCardapio deve preservar título e descrição ao salvar")
    void insereCardapio_devePreservarTituloEDescricao() {
        // Arrange
        when(cardapioRepository.save(any(Cardapio.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Cardapio resultado = cardapioService.insereCardapio(cardapio);

        // Assert
        assertEquals("Cardápio do Almoço", resultado.getTitulo());
        assertEquals("Pratos principais para o almoço", resultado.getDescricao());
    }

    @Test
    @DisplayName("insereCardapio deve invocar o repositório para persistir")
    void insereCardapio_deveInvocarRepositorioSave() {
        // Arrange
        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(cardapio);

        // Act
        cardapioService.insereCardapio(cardapio);

        // Assert
        verify(cardapioRepository).save(any(Cardapio.class));
    }

    // ======================= atualizaCardapio() =======================

    @Test
    @DisplayName("atualizaCardapio deve persistir e retornar o cardápio atualizado")
    void atualizaCardapio_cardapioExistente_devePersistirERetornar() {
        // Arrange
        when(cardapioRepository.findById(1L)).thenReturn(Optional.of(cardapio));
        when(cardapioRepository.save(cardapio)).thenReturn(cardapio);

        // Act
        Cardapio resultado = cardapioService.atualizaCardapio(cardapio);

        // Assert
        assertNotNull(resultado);
        verify(cardapioRepository).save(cardapio);
    }

    @Test
    @DisplayName("atualizaCardapio deve lançar ObjectNotFoundException quando cardápio não existe")
    void atualizaCardapio_cardapioNaoExistente_deveLancarObjectNotFoundException() {
        // Arrange
        cardapio.setId(99L);
        when(cardapioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> cardapioService.atualizaCardapio(cardapio));
    }

    // ======================= apagaCardapio() =======================

    @Test
    @DisplayName("apagaCardapio deve excluir cardápio existente sem lançar exceção")
    void apagaCardapio_cardapioExistente_deveExcluirSemExcecao() {
        // Arrange
        when(cardapioRepository.findById(1L)).thenReturn(Optional.of(cardapio));
        doNothing().when(cardapioRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> cardapioService.apagaCardapio(1L));
        verify(cardapioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("apagaCardapio deve lançar ObjectNotFoundException quando cardápio não existe")
    void apagaCardapio_cardapioNaoExistente_deveLancarObjectNotFoundException() {
        // Arrange
        when(cardapioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> cardapioService.apagaCardapio(99L));
    }

    @Test
    @DisplayName("apagaCardapio deve lançar DataIntegrityViolationException quando há itens associados")
    void apagaCardapio_comItensAssociados_deveLancarDataIntegrityViolationException() {
        // Arrange
        when(cardapioRepository.findById(1L)).thenReturn(Optional.of(cardapio));
        doThrow(new DataIntegrityViolationException("FK violation"))
            .when(cardapioRepository).deleteById(1L);

        // Act & Assert
        DataIntegrityViolationException ex = assertThrows(
            DataIntegrityViolationException.class,
            () -> cardapioService.apagaCardapio(1L)
        );
        assertTrue(ex.getMessage().contains("Não é possível excluir esse cardapio"));
    }

    // ======================= transformarDTO() =======================

    @Test
    @DisplayName("transformarDTO deve mapear corretamente todos os campos do DTO")
    void transformarDTO_dtoComTodosCampos_deveMappearCorretamente() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.of(2024, Month.FEBRUARY, 5, 12, 0);
        CardapioDTO dto = new CardapioDTO();
        dto.setId(10L);
        dto.setDataCriacao(dataCriacao);
        dto.setTitulo("Menu Especial");
        dto.setDescricao("Pratos especiais do chef");

        // Act
        Cardapio resultado = cardapioService.transformarDTO(dto);

        // Assert
        assertEquals(10L, resultado.getId());
        assertEquals(dataCriacao, resultado.getDataCriacao());
        assertEquals("Menu Especial", resultado.getTitulo());
        assertEquals("Pratos especiais do chef", resultado.getDescricao());
    }

    @Test
    @DisplayName("transformarDTO deve preservar a lista de itens do DTO")
    void transformarDTO_comItens_devePreservarListaDeItens() {
        // Arrange
        CardapioDTO dto = new CardapioDTO(cardapio);

        // Act
        Cardapio resultado = cardapioService.transformarDTO(dto);

        // Assert
        assertNotNull(resultado.getItems());
    }

    @Test
    @DisplayName("transformarDTO com descrição null deve mapear sem lançar exceção")
    void transformarDTO_descricaoNull_deveMappearSemExcecao() {
        // Arrange
        CardapioDTO dto = new CardapioDTO();
        dto.setId(1L);
        dto.setTitulo("Título");
        dto.setDescricao(null);

        // Act & Assert
        assertDoesNotThrow(() -> cardapioService.transformarDTO(dto));
    }

    @Test
    @DisplayName("transformarDTO com título vazio deve mapear sem lançar exceção")
    void transformarDTO_tituloVazio_deveMappearSemExcecao() {
        // Arrange
        CardapioDTO dto = new CardapioDTO();
        dto.setId(1L);
        dto.setTitulo("");

        // Act
        Cardapio resultado = cardapioService.transformarDTO(dto);

        // Assert
        assertEquals("", resultado.getTitulo());
    }
}
