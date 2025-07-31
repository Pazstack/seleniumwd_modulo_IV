package com.paz.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegisterPage {

    WebDriver driver;

    // Constructor
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    // Navegar a la página de registro
    public void irA() {
        driver.get("https://demoqa.com/register");
    }

    // Completar campos de texto
    public void completarFormulario(String nombre, String apellido, String usuario, String contraseña) {
        driver.findElement(By.id("firstname")).sendKeys(nombre);
        driver.findElement(By.id("lastname")).sendKeys(apellido);
        driver.findElement(By.id("userName")).sendKeys(usuario);
        driver.findElement(By.id("password")).sendKeys(contraseña);
    }

    // Hacer clic en el botón de registro
    public void hacerRegistro() {
        WebElement registerBtn = driver.findElement(By.id("register"));
        registerBtn.click();
    }

    // Validar si sigue en la misma página (bloqueado por captcha o error)
    public boolean sigueEnRegistro() {
        return driver.getCurrentUrl().contains("register");
    }

    // Obtener texto del mensaje de error (si aparece)
    public String obtenerMensaje() {
        return driver.findElement(By.id("name")).getText();
    }
}