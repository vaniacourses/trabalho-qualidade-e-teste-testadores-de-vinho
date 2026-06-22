package com.example.waiterapp.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

// TESTE DE CAIXA PRETA
// Esta classe testa funcionalidades relacionadas a clientes a partir da resposta visível da aplicação.
// O objetivo é validar se os endpoints e a página principal respondem corretamente,
// analisando somente saídas externas, como JSON, URL, tempo de resposta e conteúdo renderizado.
@Tag("e2e")
@DisplayName("Testes E2E - Clientes")
class ClienteE2ETest extends BaseSeleniumTest {

    @Test
    @DisplayName("API de clientes deve retornar lista JSON válida")
    void apiClientes_getEndpoint_retornaListaJson() {
        navigateTo("/api/clientes");
        String body = driver.findElement(By.tagName("body")).getText();
        assertTrue(body.startsWith("["),
                "Endpoint GET /api/clientes deve retornar array JSON");
    }

    @Test
    @DisplayName("API de clientes não deve retornar erro 5xx")
    void apiClientes_getEndpoint_naoRetornaErro5xx() {
        navigateTo("/api/clientes");
        String body = driver.findElement(By.tagName("body")).getText();
        assertFalse(body.contains("\"status\":500"), "API não deve retornar erro 500");
        assertFalse(body.contains("\"status\":503"), "API não deve retornar erro 503");
    }

    @Test
    @DisplayName("Aplicativo deve carregar a rota raiz sem redirecionamento para erro")
    void aplicativo_rotaRaiz_naoRedirecionaParaErro() {
        navigateTo("/");
        String url = driver.getCurrentUrl();
        assertFalse(url.contains("error"), "URL não deve conter 'error' após carregar a raiz");
        assertFalse(url.contains("404"), "URL não deve conter '404' após carregar a raiz");
    }

    @Test
    @DisplayName("API de clientes deve responder em menos de 3 segundos")
    void apiClientes_tempoResposta_deveSerAceitavel() {
        long inicio = System.currentTimeMillis();
        navigateTo("/api/clientes");
        waitForElement(By.tagName("body"));
        long fim = System.currentTimeMillis();
        assertTrue(fim - inicio < 3000,
                "API /api/clientes deve responder em menos de 3 segundos");
    }

    @Test
    @DisplayName("Página do app deve conter elemento raiz Angular")
    void aplicativo_paginaPrincipal_contemElementoAngular() {
        navigateTo("/");
        String source = driver.getPageSource();
        assertTrue(source.contains("app-root") || source.contains("<app-") || source.contains("ng-"),
                "Página deve conter elementos Angular");
    }
}
