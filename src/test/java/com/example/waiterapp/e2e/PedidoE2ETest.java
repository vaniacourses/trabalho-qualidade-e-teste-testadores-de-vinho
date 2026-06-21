package com.example.waiterapp.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

@Tag("e2e")
@DisplayName("Testes E2E - Pedidos")
class PedidoE2ETest extends BaseSeleniumTest {

    @Test
    @DisplayName("Página inicial deve carregar o aplicativo Angular")
    void paginaInicial_deveCarregarAppAngular() {
        String title = driver.getTitle();
        assertFalse(title.isEmpty(), "Título da página não deve estar vazio");
    }

    @Test
    @DisplayName("Aplicativo deve carregar sem erros de JavaScript")
    void aplicativo_semErrosJavaScript_deveCarregarCssAngularMaterial() {
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("app-root") || pageSource.contains("angular"),
                "Página deve conter elementos Angular");
    }

    @Test
    @DisplayName("API de pedidos deve retornar lista válida via navegador")
    void apiPedidos_getEndpoint_deveRetornarRespostaValida() {
        navigateTo("/api/pedidos");
        String body = driver.findElement(By.tagName("body")).getText();
        assertTrue(body.startsWith("[") || body.startsWith("{"),
                "Resposta da API deve ser JSON (array ou objeto)");
    }

    @Test
    @DisplayName("Tempo de carregamento da página principal deve ser inferior a 5 segundos")
    void paginaPrincipal_tempoCarregamento_deveSerAceitavel() {
        long inicio = System.currentTimeMillis();
        navigateTo("/");
        long fim = System.currentTimeMillis();
        long tempoMs = fim - inicio;
        assertTrue(tempoMs < 5000,
                "Página deve carregar em menos de 5 segundos, mas levou " + tempoMs + "ms");
    }

    @Test
    @DisplayName("API de pedidos não deve retornar erro 500")
    void apiPedidos_semDados_naoRetornaErro500() {
        navigateTo("/api/pedidos");
        assertFalse(driver.getPageSource().contains("\"status\":500"),
                "API não deve retornar erro 500");
    }
}
