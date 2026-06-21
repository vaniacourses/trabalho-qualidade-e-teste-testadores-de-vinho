package com.example.waiterapp.integration;

import com.example.waiterapp.cliente.ClienteDTO;
import com.example.waiterapp.garcom.GarcomDTO;
import com.example.waiterapp.pedido.PedidoDTO;
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
@DisplayName("Testes de integração para Pedido")
class PedidoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long clienteId;
    private Long garcomId;

    @BeforeEach
    void setUp() throws Exception {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("DELETE FROM item_pedido");
        jdbcTemplate.execute("DELETE FROM pedido");
        jdbcTemplate.execute("DELETE FROM cliente");
        jdbcTemplate.execute("DELETE FROM garcom");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        clienteId = criarCliente("Ana Teste", "ana@pedido.com", "11100011100");
        garcomId = criarGarcom("Garçom Teste", "99900099900");
    }

    private Long criarCliente(String nome, String email, String cpf) throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome(nome);
        dto.setEmail(email);
        dto.setCpf(cpf);

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private Long criarGarcom(String nome, String cpf) throws Exception {
        GarcomDTO dto = new GarcomDTO();
        dto.setNome(nome);
        dto.setCpf(cpf);

        String location = mockMvc.perform(post("/api/garcons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getHeader("Location");

        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }

    private PedidoDTO pedidoDTO() {
        PedidoDTO dto = new PedidoDTO();
        com.example.waiterapp.cliente.Cliente cliente = new com.example.waiterapp.cliente.Cliente();
        cliente.setId(clienteId);
        com.example.waiterapp.garcom.Garcom garcom = new com.example.waiterapp.garcom.Garcom();
        garcom.setId(garcomId);
        dto.setCliente(cliente);
        dto.setGarcom(garcom);
        return dto;
    }

    @Test
    @DisplayName("GET /api/pedidos deve retornar lista vazia inicialmente")
    void listaPedidos_semDados_retornaListaVazia() throws Exception {
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /api/pedidos deve criar um novo pedido e retornar 201")
    void inserePedido_dadosValidos_retorna201() throws Exception {
        PedidoDTO dto = pedidoDTO();

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /api/pedidos/{id} deve retornar o pedido cadastrado")
    void retornaPedidoById_pedidoExistente_retorna200() throws Exception {
        PedidoDTO dto = pedidoDTO();

        String response = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/pedidos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));
    }

    @Test
    @DisplayName("GET /api/pedidos/clientes/{idCliente} deve retornar pedidos do cliente")
    void retornaPedidoByIdCliente_clienteExistente_retorna200() throws Exception {
        PedidoDTO dto = pedidoDTO();

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/pedidos/clientes/{idCliente}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("PUT /api/pedidos/{id} deve atualizar o pedido e retornar 200")
    void atualizaPedido_pedidoExistente_retorna200() throws Exception {
        PedidoDTO dto = pedidoDTO();

        String response = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        PedidoDTO atualizado = pedidoDTO();
        atualizado.setOpcoesExtras("sem cebola");

        mockMvc.perform(put("/api/pedidos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/pedidos/{id} deve remover o pedido e retornar 204")
    void apagaPedido_pedidoExistente_retorna204() throws Exception {
        PedidoDTO dto = pedidoDTO();

        String response = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/pedidos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/pedidos após inserção deve retornar lista com 1 pedido")
    void listaPedidos_aposInsercao_retornaListaComUmElemento() throws Exception {
        PedidoDTO dto = pedidoDTO();

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }
}
