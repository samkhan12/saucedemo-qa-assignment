package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        visible(LOGIN_BUTTON);
        return this;
    }

    public void enterCredentials(String user, String pass) {
        type(USERNAME, user);
        type(PASSWORD, pass);
    }

    public void submit() {
        click(LOGIN_BUTTON);
    }

    public InventoryPage loginAs(String user, String pass) {
        enterCredentials(user, pass);
        submit();
        return new InventoryPage(driver);
    }

    public String errorMessage() {
        return text(ERROR);
    }

    public boolean isLoginFormDisplayed() {
        return visible(LOGIN_BUTTON).isDisplayed();
    }

    public String passwordFieldType() {
        return visible(PASSWORD).getAttribute("type");
    }
}
