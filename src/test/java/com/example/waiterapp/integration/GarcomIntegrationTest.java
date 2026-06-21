package com.example.waiterapp.integration;

import com.example.waiterapp.garcom.GarcomDTO;
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
@DisplayName("Testes de integração para Garcom")
class GarcomIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void limparBase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("DELETE FROM garcom");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private GarcomDTO garcomDTO(String nome, String cpf) {
        GarcomDTO dto = new GarcomDTO();
        dto.setNome(nome);
        dto.setCpf(cpf);
        return dto;
    }

    @Test
    @DisplayName("GET /api/garcons deve retornar lista vazia inicialmente")
    void listaGarcoms_semDados_retornaListaVazia() throws Exception {
        mockMvc.perform(get("/api/garcons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /api/garcons deve criar um novo garçom e retornar 201")
    void insereGarcom_dadosValidos_retorna201() throws Exception {
        GarcomDTO dto = garcomDTO("João Silva", "11122233344");

        mockMvc.perform(post("/api/garcons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /api/garcons/{id} deve retornar o garçom cadastrado")
    void retornaGarcomById_garcomExistente_retorna200() throws Exception {
        GarcomDTO dto = garcomDTO("Maria Souza", "99988877766");

        String locationHeader = mockMvc.perform(post("/api/garcons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        mockMvc.perform(get("/api/garcons/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Maria Souza")));
    }

    @Test
    @DisplayName("GET /api/garcons/{id} com id inexistente deve retornar 404")
    void retornaGarcomById_garcomInexistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/garcons/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/garcons/{id} deve atualizar os dados do garçom")
    void atualizaGarcom_dadosValidos_retorna200() throws Exception {
        GarcomDTO dto = garcomDTO("Carlos Lima", "55544433322");

        String locationHeader = mockMvc.perform(post("/api/garcons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        GarcomDTO atualizado = garcomDTO("Carlos Mendes", "55544433322");

        mockMvc.perform(put("/api/garcons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/garcons/{id} com id inexistente deve retornar 404")
    void atualizaGarcom_garcomInexistente_retorna404() throws Exception {
        GarcomDTO dto = garcomDTO("Inexistente", "00000000000");

        mockMvc.perform(put("/api/garcons/{id}", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/garcons/{id} deve remover o garçom e retornar 204")
    void apagaGarcom_garcomExistente_retorna204() throws Exception {
        GarcomDTO dto = garcomDTO("Pedro Oliveira", "12312312399");

        String locationHeader = mockMvc.perform(post("/api/garcons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        mockMvc.perform(delete("/api/garcons/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/garcons após inserção deve retornar lista com 1 garçom")
    void listaGarcoms_aposInsercao_retornaListaComUmElemento() throws Exception {
        GarcomDTO dto = garcomDTO("Teste Lista", "10101010101");

        mockMvc.perform(post("/api/garcons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/garcons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
