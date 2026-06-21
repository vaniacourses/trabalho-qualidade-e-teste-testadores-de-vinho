package com.example.waiterapp.integration;

import com.example.waiterapp.cardapio.CardapioDTO;
import com.example.waiterapp.cardapio.CardapioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de integração para Cardapio")
class CardapioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardapioRepository cardapioRepository;

    @BeforeEach
    void limparBase() {
        cardapioRepository.deleteAll();
    }

    private CardapioDTO cardapioDTO(String titulo, String descricao) {
        CardapioDTO dto = new CardapioDTO();
        dto.setTitulo(titulo);
        dto.setDescricao(descricao);
        return dto;
    }

    @Test
    @DisplayName("GET /api/cardapios deve retornar lista vazia inicialmente")
    void listaCardapios_semDados_retornaListaVazia() throws Exception {
        mockMvc.perform(get("/api/cardapios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /api/cardapios deve criar cardapio e retornar 201")
    void insereCardapio_dadosValidos_retorna201() throws Exception {
        CardapioDTO dto = cardapioDTO("Cardápio do Almoço", "Pratos do dia");

        mockMvc.perform(post("/api/cardapios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /api/cardapios/{id} deve retornar o cardapio cadastrado")
    void retornaCardapioById_cardapioExistente_retorna200() throws Exception {
        CardapioDTO dto = cardapioDTO("Cardápio Jantar", "Pratos da noite");

        String locationHeader = mockMvc.perform(post("/api/cardapios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        mockMvc.perform(get("/api/cardapios/{id}", Long.parseLong(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Cardápio Jantar")));
    }

    @Test
    @DisplayName("GET /api/cardapios/{id} com id inexistente deve retornar 404")
    void retornaCardapioById_idInexistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/cardapios/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/cardapios/{id} deve atualizar o cardapio")
    void atualizaCardapio_dadosValidos_retorna200() throws Exception {
        CardapioDTO dto = cardapioDTO("Cardápio Original", "Descrição original");

        String locationHeader = mockMvc.perform(post("/api/cardapios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        CardapioDTO atualizado = cardapioDTO("Cardápio Atualizado", "Nova descrição");

        mockMvc.perform(put("/api/cardapios/{id}", Long.parseLong(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/cardapios/{id}", Long.parseLong(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Cardápio Atualizado")));
    }

    @Test
    @DisplayName("DELETE /api/cardapios/{id} deve remover o cardapio e retornar 204")
    void apagaCardapio_cardapioExistente_retorna204() throws Exception {
        CardapioDTO dto = cardapioDTO("Cardápio Temp", "Para ser excluído");

        String locationHeader = mockMvc.perform(post("/api/cardapios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        mockMvc.perform(delete("/api/cardapios/{id}", Long.parseLong(id)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/cardapios/{id}", Long.parseLong(id)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/cardapios/{id} com id inexistente deve retornar 404")
    void apagaCardapio_idInexistente_retorna404() throws Exception {
        mockMvc.perform(delete("/api/cardapios/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/cardapios após inserção deve retornar lista com elementos")
    void listaCardapios_aposInsercoes_retornaLista() throws Exception {
        mockMvc.perform(post("/api/cardapios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardapioDTO("C1", "Desc1"))))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/cardapios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardapioDTO("C2", "Desc2"))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/cardapios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
