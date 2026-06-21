package com.example.waiterapp.integration;

import com.example.waiterapp.cliente.ClienteDTO;
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
@DisplayName("Testes de integração para Cliente")
class ClienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void limparBase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("DELETE FROM cliente");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private ClienteDTO clienteDTO(String nome, String email, String cpf) {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome(nome);
        dto.setEmail(email);
        dto.setCpf(cpf);
        return dto;
    }

    @Test
    @DisplayName("GET /api/clientes deve retornar lista vazia inicialmente")
    void listaClientes_semDados_retornaListaVazia() throws Exception {
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /api/clientes deve criar um novo cliente e retornar 201")
    void insereCliente_dadosValidos_retorna201() throws Exception {
        ClienteDTO dto = clienteDTO("Ana Silva", "ana@test.com", "11122233344");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Ana Silva")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/clientes com CPF já cadastrado deve retornar o cliente existente")
    void insereCliente_cpfDuplicado_retornaClienteExistente() throws Exception {
        ClienteDTO dto = clienteDTO("Ana Silva", "ana@test.com", "11122233344");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf", is("11122233344")));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} deve retornar o cliente cadastrado")
    void retornaClienteById_clienteExistente_retorna200() throws Exception {
        ClienteDTO dto = clienteDTO("Carlos Mendes", "carlos@test.com", "99988877766");

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Carlos Mendes")));
    }

    @Test
    @DisplayName("PUT /api/clientes/{id} deve atualizar os dados do cliente")
    void atualizaCliente_dadosValidos_retorna200() throws Exception {
        ClienteDTO dto = clienteDTO("Maria Oliveira", "maria@test.com", "55544433322");

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        ClienteDTO atualizado = clienteDTO("Maria Costa", "mariacosta@test.com", "55544433322");

        mockMvc.perform(put("/api/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} deve remover o cliente e retornar 204")
    void apagaCliente_clienteExistente_retorna204() throws Exception {
        ClienteDTO dto = clienteDTO("Pedro Lima", "pedro@test.com", "12312312399");

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/clientes/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/clientes/cpf deve retornar cliente pelo CPF")
    void retornaClienteByCpf_cpfExistente_retorna200() throws Exception {
        ClienteDTO dto = clienteDTO("Lucas Ferreira", "lucas@test.com", "33322211100");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        ClienteDTO busca = new ClienteDTO();
        busca.setCpf("33322211100");

        mockMvc.perform(post("/api/clientes/cpf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(busca)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf", is("33322211100")));
    }

    @Test
    @DisplayName("POST /api/clientes sem CPF deve criar cliente normalmente")
    void insereCliente_semCpf_retorna201() throws Exception {
        ClienteDTO dto = clienteDTO("Sem CPF", "semcpf@test.com", null);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Sem CPF")))
                .andExpect(jsonPath("$.cpf").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/clientes após inserção deve retornar lista com 1 cliente")
    void listaClientes_aposInsercao_retornaListaComUmElemento() throws Exception {
        ClienteDTO dto = clienteDTO("Teste Lista", "lista@test.com", "10101010101");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
