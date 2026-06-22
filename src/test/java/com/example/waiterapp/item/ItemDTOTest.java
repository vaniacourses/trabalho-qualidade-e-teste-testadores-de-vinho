package com.example.waiterapp.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para ItemDTO")
class ItemDTOTest {

    private static final LocalDateTime DATA = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "Frango Grelhado", "Prato saudável", DATA, 29.90);
    }

    @Test
    @DisplayName("construtor a partir de Item deve copiar todos os campos")
    void construtor_comItem_deveCopiarCampos() {
        ItemDTO dto = new ItemDTO(item);

        assertEquals(1L, dto.getId());
        assertEquals("Frango Grelhado", dto.getNome());
        assertEquals("Prato saudável", dto.getDescricao());
        assertEquals(DATA, dto.getDataCriacao());
        assertEquals(29.90, dto.getPreco());
    }

    @Test
    @DisplayName("construtor padrão deve criar instância válida")
    void construtor_padrao_deveCriarInstancia() {
        ItemDTO dto = new ItemDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("setters devem atualizar os campos corretamente")
    void setters_devemAtualizarCampos() {
        ItemDTO dto = new ItemDTO();
        dto.setId(2L);
        dto.setNome("Salmão");
        dto.setDescricao("Salmão grelhado");
        dto.setDataCriacao(DATA);
        dto.setPreco(45.00);

        assertEquals(2L, dto.getId());
        assertEquals("Salmão", dto.getNome());
        assertEquals("Salmão grelhado", dto.getDescricao());
        assertEquals(DATA, dto.getDataCriacao());
        assertEquals(45.00, dto.getPreco());
    }

    @Test
    @DisplayName("setCardapios deve atualizar a lista de cardápios")
    void setCardapios_deveAtualizarCardapios() {
        ItemDTO dto = new ItemDTO();
        dto.setCardapios(new ArrayList<>());
        assertNotNull(dto.getCardapios());
        assertTrue(dto.getCardapios().isEmpty());
    }

    @Test
    @DisplayName("setItems deve atualizar o conjunto de itens")
    void setItems_deveAtualizarItems() {
        ItemDTO dto = new ItemDTO();
        dto.setItems(new HashSet<>());
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }
}
