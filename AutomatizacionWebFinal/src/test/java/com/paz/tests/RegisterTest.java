package com.paz.tests;

import com.paz.pages.RegisterPage;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class RegisterTest {

    public void capturarPantalla(WebDriver driver, String nombreArchivo) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            new File("screenshots").mkdir();
            FileUtils.copyFile(screenshot, new File("screenshots/" + nombreArchivo + ".png"));
            System.out.println("📸 Captura guardada como: " + nombreArchivo + ".png");
        } catch (IOException e) {
            System.out.println("❌ Error al guardar captura: " + e.getMessage());
        }
    }

    @Test
    public void registroConCamposVaciosChrome() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            RegisterPage registro = new RegisterPage(driver);
            registro.irA();

            WebElement registerBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("register")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerBtn);
            wait.until(ExpectedConditions.elementToBeClickable(registerBtn));
            registro.hacerRegistro();

            Assert.assertTrue("❌ El registro no fue bloqueado", registro.sigueEnRegistro());
            capturarPantalla(driver, "registroFallidoChrome");
            System.out.println("✅ Registro fallido verificado en Chrome");
        } catch (Exception e) {
            capturarPantalla(driver, "errorRegistroChrome");
            Assert.fail("Excepción en test de registro vacío en Chrome");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void registroConDatosValidosFirefox() {
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            RegisterPage registro = new RegisterPage(driver);
            registro.irA();
            registro.completarFormulario("Paz", "Automatizadora", "PazPruebaFirefox", "ClaveSegura2025!");

            WebElement registerBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("register")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerBtn);
            wait.until(ExpectedConditions.elementToBeClickable(registerBtn));
            registro.hacerRegistro();

            Assert.assertTrue("❌ Registro bloqueado por captcha (esperado)", registro.sigueEnRegistro());
            capturarPantalla(driver, "registroSimuladoFirefox");
            System.out.println("✅ Registro simulado verificado en Firefox");
        } catch (Exception e) {
            capturarPantalla(driver, "errorRegistroFirefox");
            Assert.fail("Excepción en test de registro válido en Firefox");
        } finally {
            driver.quit();
        }
    }
}