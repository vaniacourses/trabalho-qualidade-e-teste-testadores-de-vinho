package com.example.waiterapp.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcluirPedidoE2ETest {

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    @Test
    void deveExcluirPedidoCriadoPeloCliente() {
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

        wait.until(ExpectedConditions.urlContains("/cliente/lista-pedidos"));

        WebElement primeiraLinhaPedido = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("(//tbody//tr[contains(@class, 'mat-row')])[1]")
                )
        );

        WebElement celulaIdPedido = primeiraLinhaPedido.findElement(
                By.cssSelector("td.mat-column-id")
        );

        String idPedido = celulaIdPedido.getText().trim();

        assertTrue(
                !idPedido.isEmpty(),
                "O pedido criado deveria aparecer na primeira linha da tabela"
        );

        System.out.println("ID do pedido criado: " + idPedido);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                celulaIdPedido
        );

        wait.until(ExpectedConditions.elementToBeClickable(celulaIdPedido));

        String urlAntesDoClique = driver.getCurrentUrl();

        new Actions(driver)
                .moveToElement(celulaIdPedido)
                .pause(Duration.ofMillis(500))
                .click()
                .perform();

        try {
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.urlToBe(urlAntesDoClique)
            ));
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    celulaIdPedido
            );
        }

        wait.until(d -> !d.getCurrentUrl().equals(urlAntesDoClique) || true);

        System.out.println("URL depois de clicar no pedido: " + driver.getCurrentUrl());

        WebElement botaoExcluirPedido = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//main//button[contains(@class, 'mat-warn') and contains(@class, 'mat-raised-button')]")
                )
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                botaoExcluirPedido
        );

        botaoExcluirPedido.click();

        WebElement botaoConfirmar = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[normalize-space()='Confirmar']]")
                )
        );

        botaoConfirmar.click();

        wait.until(ExpectedConditions.urlContains("/cliente/lista-pedidos"));

        wait.until(webDriver -> {
            try {
                return webDriver.findElements(
                        By.xpath("//tr[contains(@class, 'mat-row') and .//td[contains(@class, 'mat-column-id') and normalize-space()='" + idPedido + "']]")
                ).isEmpty();
            } catch (StaleElementReferenceException e) {
                return true;
            }
        });

        boolean pedidoAindaExiste = !driver.findElements(
                By.xpath("//tr[contains(@class, 'mat-row') and .//td[contains(@class, 'mat-column-id') and normalize-space()='" + idPedido + "']]")
        ).isEmpty();

        assertTrue(
                !pedidoAindaExiste,
                "Após confirmar a exclusão, o pedido criado deveria sair da lista"
        );
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }
}