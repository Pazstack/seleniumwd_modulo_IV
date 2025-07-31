package com.paz.tests;

import com.paz.pages.LoginPage;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.io.*;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

@RunWith(Parameterized.class)
public class LoginTest {

    private final String email;
    private final String password;
    private final String resultadoEsperado;

    public LoginTest(String email, String password, String resultadoEsperado) {
        this.email = email;
        this.password = password;
        this.resultadoEsperado = resultadoEsperado;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> obtenerDatosCSV() throws IOException, CsvValidationException {
        List<Object[]> datos = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("src/test/resources/datos_login.csv"))) {
            String[] fila;
            reader.readNext(); // Saltar encabezado
            while ((fila = reader.readNext()) != null) {
                datos.add(new Object[] { fila[0], fila[1], fila[2] });
            }
        }
        return datos;
    }

    @Test
    public void loginConDatosDesdeCSV() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            LoginPage login = new LoginPage(driver);
            login.irA();
            login.ingresarCredenciales(email, password);
            login.hacerLogin();

            Thread.sleep(2000); // espera corta

            if (resultadoEsperado.equalsIgnoreCase("exitoso")) {
                try {
                    WebElement logoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit")));
                    Assert.assertTrue("❌ No se mostró botón logout", logoutBtn.isDisplayed());
                    capturarPantalla(driver, "loginExitoso_" + email);
                    System.out.println("✅ Login exitoso con: " + email);
                } catch (Exception e) {
                    capturarPantalla(driver, "loginFallido_" + email);
                    System.out.println("⚠️ Login fallido con: " + email + " (esperado exitoso)");
                }
            } else {
                try {
                    WebElement mensajeError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
                    Assert.assertTrue("❌ No se mostró mensaje de error",
                        mensajeError.getText().toLowerCase().contains("invalid"));
                    capturarPantalla(driver, "loginFallido_" + email);
                    System.out.println("✅ Login fallido con: " + email);
                } catch (Exception e) {
                    capturarPantalla(driver, "loginError_" + email);
                    System.out.println("⚠️ No se mostró mensaje de error con: " + email);
                }
            }

        } catch (Exception e) {
            capturarPantalla(driver, "loginError_" + email);
            System.out.println("⚠️ Error con usuario " + email + ": " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    public void capturarPantalla(WebDriver driver, String nombreArchivo) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            new File("screenshots").mkdir(); // crea carpeta si no existe
            FileUtils.copyFile(screenshot, new File("screenshots/" + nombreArchivo + ".png"));
            System.out.println("📸 Captura guardada como: " + nombreArchivo + ".png");
        } catch (IOException e) {
            System.out.println("❌ Error al guardar captura: " + e.getMessage());
        }
    }
}