package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By CANCEL = By.id("cancel");
    private static final By FINISH = By.id("finish");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private static final By SUBTOTAL = By.cssSelector("[data-test='subtotal-label']");
    private static final By TAX = By.cssSelector("[data-test='tax-label']");
    private static final By TOTAL = By.cssSelector("[data-test='total-label']");
    private static final By COMPLETE_HEADER = By.cssSelector("[data-test='complete-header']");
    private static final By TITLE = By.cssSelector("[data-test='title']");

    private static final Pattern MONEY = Pattern.compile("\\$(\\d+(?:\\.\\d+)?)");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillInformation(String first, String last, String postal) {
        type(FIRST_NAME, first);
        type(LAST_NAME, last);
        type(POSTAL_CODE, postal);
        return this;
    }

    public CheckoutPage clickContinue() {
        click(CONTINUE);
        return this;
    }

    public CartPage cancel() {
        click(CANCEL);
        return new CartPage(driver);
    }

    public CheckoutPage finish() {
        click(FINISH);
        return this;
    }

    public String errorMessage() {
        return text(ERROR);
    }

    public String title() {
        return text(TITLE);
    }

    public String completeHeader() {
        return text(COMPLETE_HEADER);
    }

    public double subtotal() {
        return money(SUBTOTAL);
    }

    public double tax() {
        return money(TAX);
    }

    public double total() {
        return money(TOTAL);
    }

    private double money(By locator) {
        Matcher m = MONEY.matcher(text(locator));
        if (!m.find()) {
            throw new IllegalStateException("No amount found in: " + text(locator));
        }
        return Double.parseDouble(m.group(1));
    }
}
