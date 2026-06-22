package com.example.waiterapp.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


// TESTE DE CAIXA PRETA
// Este teste simula o fluxo de login do cliente como um usuário real faria.
// São informados nome e CPF, o botão Continuar é acionado
// e o resultado esperado é que o sistema avance para outra tela.
// O teste não verifica como o login é processado internamente,
// apenas se o comportamento final da interface está correto.
public class ClienteLoginE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void abrirNavegador() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Deixe comentado para ver o Chrome abrindo
        // options.addArguments("--headless=new");

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    void devePermitirClienteContinuarInformandoNomeECpf() {
        driver.get("http://localhost:8080/#/cliente/login");

        WebElement campoNome = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("any-nome"))
        );

        WebElement campoCpf = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("any-cpf"))
        );

        campoNome.clear();
        campoNome.sendKeys("Guilherme Teste");

        campoCpf.clear();
        campoCpf.sendKeys("12345678901");

        WebElement botaoContinuar = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[normalize-space()='Continuar']]")
                )
        );

        botaoContinuar.click();

        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/cliente/login")
        ));

        String urlAtual = driver.getCurrentUrl();
        String pagina = driver.getPageSource();

        assertFalse(
                urlAtual.contains("/cliente/login"),
                "Após preencher nome e CPF e clicar em Continuar, o cliente não deveria continuar na tela de login"
        );

        assertTrue(
                pagina.length() > 0,
                "A próxima tela deveria carregar conteúdo"
        );
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }
}