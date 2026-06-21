package com.example.waiterapp.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Tag("e2e")
@DisplayName("Testes E2E - Garçom")
class GarcomE2ETest extends BaseSeleniumTest {

    @Test
    @DisplayName("API de garçons deve retornar lista JSON válida")
    void apiGarcons_getEndpoint_retornaListaJson() {
        navigateTo("/api/garcons");
        String body = driver.findElement(By.tagName("body")).getText();
        assertTrue(body.startsWith("["),
                "Endpoint GET /api/garcons deve retornar array JSON");
    }

    @Test
    @DisplayName("API de garçons não deve retornar erro 5xx")
    void apiGarcons_getEndpoint_naoRetornaErro5xx() {
        navigateTo("/api/garcons");
        String body = driver.findElement(By.tagName("body")).getText();
        assertFalse(body.contains("\"status\":500"), "API não deve retornar erro 500");
        assertFalse(body.contains("\"status\":503"), "API não deve retornar erro 503");
    }

    @Test
    @DisplayName("Servidor deve responder ao health check na raiz")
    void servidor_raiz_responde() {
        navigateTo("/");
        WebElement body = waitForElement(By.tagName("body"));
        assertNotNull(body, "Body da página não deve ser null");
        assertFalse(body.getText().isEmpty() || driver.getPageSource().contains("<html>"),
                "Servidor deve retornar conteúdo");
    }

    @Test
    @DisplayName("API de garçons deve responder em menos de 3 segundos")
    void apiGarcons_tempoResposta_deveSerAceitavel() {
        long inicio = System.currentTimeMillis();
        navigateTo("/api/garcons");
        waitForElement(By.tagName("body"));
        long fim = System.currentTimeMillis();
        assertTrue(fim - inicio < 3000,
                "API /api/garcons deve responder em menos de 3 segundos");
    }

}
