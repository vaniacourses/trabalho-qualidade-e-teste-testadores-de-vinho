package com.example.waiterapp.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FinalizarPedidoE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void abrirNavegador() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    void deveFinalizarPedidoComItemNoCarrinho() {
        driver.get("http://localhost:8080/#/cliente/login");

        WebElement campoNome = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("any-nome"))
        );

        WebElement campoCpf = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("any-cpf"))
        );

        campoNome.clear();
        campoNome.sendKeys("Guilherme");

        campoCpf.clear();
        campoCpf.sendKeys("00000000000");

        WebElement botaoContinuar = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[normalize-space()='Continuar']]")
                )
        );

        botaoContinuar.click();

        WebElement cardPrato = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[normalize-space()='Frango com quiabo']/ancestor::mat-card[1]")
                )
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                cardPrato
        );

        cardPrato.click();

        WebElement botaoAdicionarAoCarrinho = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[normalize-space()='Adicionar ao carrinho']]")
                )
        );

        botaoAdicionarAoCarrinho.click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"),
                "Valor total: R$ 59,99"
        ));

        WebElement botaoFinalizarPedido = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[contains(normalize-space(), 'Finalizar Pedido')]]")
                )
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                botaoFinalizarPedido
        );

        botaoFinalizarPedido.click();

        wait.until(webDriver ->
                webDriver.getPageSource().contains("Pagamento") ||
                webDriver.getPageSource().contains("Pedido finalizado") ||
                webDriver.getPageSource().contains("Pedido realizado") ||
                webDriver.getPageSource().contains("Forma de pagamento") ||
                webDriver.getPageSource().contains("Finalizado") ||
                !webDriver.getCurrentUrl().contains("fazer-pedido")
        );

        String pagina = driver.getPageSource();
        String urlAtual = driver.getCurrentUrl();

        assertTrue(
                pagina.contains("Pagamento") ||
                pagina.contains("Pedido finalizado") ||
                pagina.contains("Pedido realizado") ||
                pagina.contains("Forma de pagamento") ||
                pagina.contains("Finalizado") ||
                !urlAtual.contains("fazer-pedido"),
                "Após clicar em Finalizar Pedido, o sistema deveria avançar para a próxima etapa ou confirmar o pedido"
        );
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }
}