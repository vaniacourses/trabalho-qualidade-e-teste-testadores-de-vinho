package com.example.waiterapp.garcom;

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
@DisplayName("Testes unitários para GarcomService")
class GarcomServiceTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    @Mock private GarcomRepository garcomRepository;

    @InjectMocks
    private GarcomService garcomService;

    private Garcom garcom;

    @BeforeEach
    void setUp() {
        garcom = new Garcom(1L, "Carlos Pereira", FIXED_DATE, "11122233344");
    }

    // ======================= listaGarcons() =======================

    @Test
    @DisplayName("listaGarcons deve retornar todos os garçons cadastrados")
    void listaGarcons_deveRetornarTodosOsGarcons() {
        // Arrange
        List<Garcom> garcons = Arrays.asList(
            garcom,
            new Garcom(2L, "Ana Lima", FIXED_DATE, "55566677788")
        );
        when(garcomRepository.findAll()).thenReturn(garcons);

        // Act
        List<Garcom> resultado = garcomService.listaGarcons();

        // Assert
        assertEquals(2, resultado.size());
        verify(garcomRepository).findAll();
    }

    @Test
    @DisplayName("listaGarcons deve retornar lista vazia quando não há garçons cadastrados")
    void listaGarcons_semGarconsCadastrados_deveRetornarListaVazia() {
        // Arrange
        when(garcomRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Garcom> resultado = garcomService.listaGarcons();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ======================= retornaGarcomById() =======================

    @Test
    @DisplayName("retornaGarcomById deve retornar o garçom quando encontrado pelo id")
    void retornaGarcomById_idExistente_deveRetornarGarcom() {
        // Arrange
        when(garcomRepository.findById(1L)).thenReturn(Optional.of(garcom));

        // Act
        Garcom resultado = garcomService.retornaGarcomById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Carlos Pereira", resultado.getNome());
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("retornaGarcomById deve lançar ObjectNotFoundException quando o id não existe")
    void retornaGarcomById_idInexistente_deveLancarObjectNotFoundException() {
        // Arrange
        when(garcomRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException ex = assertThrows(
            ObjectNotFoundException.class,
            () -> garcomService.retornaGarcomById(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("retornaGarcomById deve incluir o tipo Garcom na mensagem de erro")
    void retornaGarcomById_idInexistente_mensagemDeErroDeveConterTipo() {
        // Arrange
        when(garcomRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException ex = assertThrows(
            ObjectNotFoundException.class,
            () -> garcomService.retornaGarcomById(0L)
        );
        assertTrue(ex.getMessage().contains(Garcom.class.getName()));
    }

    // ======================= insereGarcom() =======================

    @Test
    @DisplayName("insereGarcom deve forçar id como null antes de persistir")
    void insereGarcom_deveSetarIdNuloAntesDePersisteir() {
        // Arrange
        garcom.setId(10L);
        when(garcomRepository.save(any(Garcom.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Garcom resultado = garcomService.insereGarcom(garcom);

        // Assert
        assertNull(resultado.getId());
    }

    @Test
    @DisplayName("insereGarcom deve definir data de criação automaticamente")
    void insereGarcom_deveDefinirDataCriacaoAutomaticamente() {
        // Arrange
        LocalDateTime antes = FIXED_DATE.minusSeconds(1);
        when(garcomRepository.save(any(Garcom.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Garcom resultado = garcomService.insereGarcom(garcom);

        // Assert
        assertNotNull(resultado.getDataCriacao());
        assertTrue(resultado.getDataCriacao().isAfter(antes));
    }

    @Test
    @DisplayName("insereGarcom deve preservar nome e CPF ao salvar")
    void insereGarcom_devePreservarNomeECpf() {
        // Arrange
        when(garcomRepository.save(any(Garcom.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Garcom resultado = garcomService.insereGarcom(garcom);

        // Assert
        assertEquals("Carlos Pereira", resultado.getNome());
        assertEquals("11122233344", resultado.getCpf());
    }

    @Test
    @DisplayName("insereGarcom deve invocar o repositório para persistir")
    void insereGarcom_deveInvocarRepositorioSave() {
        // Arrange
        when(garcomRepository.save(any(Garcom.class))).thenReturn(garcom);

        // Act
        garcomService.insereGarcom(garcom);

        // Assert
        verify(garcomRepository).save(any(Garcom.class));
    }

    // ======================= atualizaGarcom() =======================

    @Test
    @DisplayName("atualizaGarcom deve persistir e retornar o garçom atualizado")
    void atualizaGarcom_garcomExistente_devePersistirERetornar() {
        // Arrange
        when(garcomRepository.findById(1L)).thenReturn(Optional.of(garcom));
        when(garcomRepository.save(garcom)).thenReturn(garcom);

        // Act
        Garcom resultado = garcomService.atualizaGarcom(garcom);

        // Assert
        assertNotNull(resultado);
        verify(garcomRepository).save(garcom);
    }

    @Test
    @DisplayName("atualizaGarcom deve lançar ObjectNotFoundException quando garçom não existe")
    void atualizaGarcom_garcomNaoExistente_deveLancarObjectNotFoundException() {
        // Arrange
        garcom.setId(99L);
        when(garcomRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> garcomService.atualizaGarcom(garcom));
    }

    // ======================= apagaGarcom() =======================

    @Test
    @DisplayName("apagaGarcom deve excluir garçom existente sem lançar exceção")
    void apagaGarcom_garcomExistente_deveExcluirSemExcecao() {
        // Arrange
        when(garcomRepository.findById(1L)).thenReturn(Optional.of(garcom));
        doNothing().when(garcomRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> garcomService.apagaGarcom(1L));
        verify(garcomRepository).deleteById(1L);
    }

    @Test
    @DisplayName("apagaGarcom deve lançar ObjectNotFoundException quando garçom não existe")
    void apagaGarcom_garcomNaoExistente_deveLancarObjectNotFoundException() {
        // Arrange
        when(garcomRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> garcomService.apagaGarcom(99L));
    }

    @Test
    @DisplayName("apagaGarcom deve lançar DataIntegrityViolationException quando há pedidos associados")
    void apagaGarcom_comPedidosAssociados_deveLancarDataIntegrityViolationException() {
        // Arrange
        when(garcomRepository.findById(1L)).thenReturn(Optional.of(garcom));
        doThrow(new DataIntegrityViolationException("FK violation"))
            .when(garcomRepository).deleteById(1L);

        // Act & Assert
        DataIntegrityViolationException ex = assertThrows(
            DataIntegrityViolationException.class,
            () -> garcomService.apagaGarcom(1L)
        );
        assertTrue(ex.getMessage().contains("Não é possível excluir esse garcom"));
    }

    // ======================= transformarDTO() =======================

    @Test
    @DisplayName("transformarDTO deve mapear corretamente todos os campos do DTO")
    void transformarDTO_dtoComTodosCampos_deveMappearCorretamente() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.of(2023, Month.JUNE, 20, 9, 0);
        GarcomDTO dto = new GarcomDTO();
        dto.setId(7L);
        dto.setNome("Roberto Souza");
        dto.setDataCriacao(dataCriacao);
        dto.setCpf("99988877766");

        // Act
        Garcom resultado = garcomService.transformarDTO(dto);

        // Assert
        assertEquals(7L, resultado.getId());
        assertEquals("Roberto Souza", resultado.getNome());
        assertEquals(dataCriacao, resultado.getDataCriacao());
        assertEquals("99988877766", resultado.getCpf());
    }

    @Test
    @DisplayName("transformarDTO deve preservar a lista de pedidos do DTO")
    void transformarDTO_comPedidos_devePreservarPedidos() {
        // Arrange – usa construtor GarcomDTO(Garcom) para popular pedidos
        GarcomDTO dto = new GarcomDTO(garcom);

        // Act
        Garcom resultado = garcomService.transformarDTO(dto);

        // Assert
        assertNotNull(resultado.getPedidos());
    }

    @Test
    @DisplayName("transformarDTO com campos null deve mapear sem lançar exceção")
    void transformarDTO_camposNulos_deveMappearSemExcecao() {
        // Arrange
        GarcomDTO dto = new GarcomDTO();
        dto.setId(null);
        dto.setNome(null);
        dto.setCpf(null);
        dto.setDataCriacao(null);

        // Act & Assert
        assertDoesNotThrow(() -> garcomService.transformarDTO(dto));
    }
}
