package com.example.waiterapp.integration;

import com.example.waiterapp.item.ItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de integração para Item")
class ItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void limparBase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("DELETE FROM item");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private ItemDTO itemDTO(String nome, String descricao, Double preco) {
        ItemDTO dto = new ItemDTO();
        dto.setNome(nome);
        dto.setDescricao(descricao);
        dto.setPreco(preco);
        return dto;
    }

    @Test
    @DisplayName("GET /api/itens deve retornar lista vazia inicialmente")
    void listaItens_semDados_retornaListaVazia() throws Exception {
        mockMvc.perform(get("/api/itens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /api/itens deve criar um novo item e retornar 201")
    void insereItem_dadosValidos_retorna201() throws Exception {
        ItemDTO dto = itemDTO("Frango Grelhado", "Prato com frango", 29.90);

        mockMvc.perform(post("/api/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /api/itens/{id} deve retornar o item cadastrado")
    void retornaItemById_itemExistente_retorna200() throws Exception {
        ItemDTO dto = itemDTO("Suco de Laranja", "Suco natural", 8.50);

        String locationHeader = mockMvc.perform(post("/api/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        mockMvc.perform(get("/api/itens/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Suco de Laranja")));
    }

    @Test
    @DisplayName("GET /api/itens/{id} com id inexistente deve retornar 404")
    void retornaItemById_itemInexistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/itens/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/itens/{id} deve atualizar os dados do item")
    void atualizaItem_dadosValidos_retorna200() throws Exception {
        ItemDTO dto = itemDTO("Coca-Cola", "Refrigerante", 6.00);

        String locationHeader = mockMvc.perform(post("/api/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        ItemDTO atualizado = itemDTO("Coca-Cola Zero", "Refrigerante zero açúcar", 6.50);

        mockMvc.perform(put("/api/itens/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/itens/{id} com id inexistente deve retornar 404")
    void atualizaItem_itemInexistente_retorna404() throws Exception {
        ItemDTO dto = itemDTO("Inexistente", "Desc", 1.0);

        mockMvc.perform(put("/api/itens/{id}", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/itens/{id} deve remover o item e retornar 204")
    void apagaItem_itemExistente_retorna204() throws Exception {
        ItemDTO dto = itemDTO("Item para deletar", "Desc", 10.0);

        String locationHeader = mockMvc.perform(post("/api/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        mockMvc.perform(delete("/api/itens/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/itens após inserção deve retornar lista com 1 item")
    void listaItens_aposInsercao_retornaListaComUmElemento() throws Exception {
        ItemDTO dto = itemDTO("Teste Lista", "Desc", 5.0);

        mockMvc.perform(post("/api/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/itens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }
}
