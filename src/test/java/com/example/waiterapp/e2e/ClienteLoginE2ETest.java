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

class ClienteLoginE2ETest {

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
    void devePermitirClienteContinuarInformandoNomeECpf() {
        driver.get("http://localhost:8080/#/cliente/login");

        WebElement campoNome = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("any-nome"))
        );

        WebElement campoCpf = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("any-cpf"))
        );

        campoNome.clear();
        campoNome.sendKeys("Felipe Teste");

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
                !pagina.isEmpty(),
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