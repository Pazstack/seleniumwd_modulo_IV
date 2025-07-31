package com.paz.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void irA() {
        driver.get("https://demoqa.com/login");
    }

    public void ingresarCredenciales(String usuario, String contraseña) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userName"))).clear();
        driver.findElement(By.id("userName")).sendKeys(usuario);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(contraseña);
    }

    public void hacerLogin() {
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginBtn);
        loginBtn.click();
    }

    public boolean iniciarSesion(String usuario, String contraseña) {
        irA();
        ingresarCredenciales(usuario, contraseña);
        hacerLogin();

        try {
            WebElement logoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit")));
            return logoutBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}