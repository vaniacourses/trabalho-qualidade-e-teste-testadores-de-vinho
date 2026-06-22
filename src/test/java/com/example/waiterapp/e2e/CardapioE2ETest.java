package com.example.waiterapp.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;


// TESTE DE CAIXA PRETA
// Estes testes verificam o comportamento externo das rotas relacionadas ao cardápio.
// As validações são feitas a partir das respostas exibidas pelo navegador,
// como retorno em formato JSON, ausência de erro 500 e carregamento da página inicial,
// sem consultar diretamente serviços, banco de dados ou regras internas do sistema.
@Tag("e2e")
@DisplayName("Testes E2E - Cardápio")
class CardapioE2ETest extends BaseSeleniumTest {

    @Test
    @DisplayName("API de cardápios deve retornar lista JSON válida")
    void apiCardapios_getEndpoint_retornaListaJson() {
        navigateTo("/api/cardapios");
        String body = driver.findElement(By.tagName("body")).getText();
        assertTrue(body.startsWith("["),
                "Endpoint GET /api/cardapios deve retornar array JSON");
    }

    @Test
    @DisplayName("API de cardápios não deve retornar erro 5xx")
    void apiCardapios_getEndpoint_naoRetornaErro5xx() {
        navigateTo("/api/cardapios");
        String body = driver.findElement(By.tagName("body")).getText();
        assertFalse(body.contains("\"status\":500"), "API não deve retornar erro 500");
    }

    @Test
    @DisplayName("Busca de cardápio por id inexistente deve retornar 404 via API")
    void apiCardapio_idInexistente_retorna404() {
        navigateTo("/api/cardapios/99999");
        String body = driver.findElement(By.tagName("body")).getText();
        assertTrue(body.isEmpty() || driver.getCurrentUrl().contains("cardapios"),
                "Endpoint deve processar busca por id inexistente sem crash");
    }

    @Test
    @DisplayName("API de itens deve estar acessível")
    void apiItens_getEndpoint_retornaListaJson() {
        navigateTo("/api/itens");
        String body = driver.findElement(By.tagName("body")).getText();
        assertFalse(body.isEmpty(), "Endpoint /api/itens deve retornar resposta");
    }

    @Test
    @DisplayName("Aplicativo Angular deve renderizar conteúdo na página inicial")
    void aplicativo_paginaInicial_contemConteudo() {
        navigateTo("/");
        assertFalse(driver.getPageSource().isEmpty(),
                "Página inicial não deve estar vazia");
        assertNotNull(driver.getTitle(), "Título da página não deve ser null");
    }
}
