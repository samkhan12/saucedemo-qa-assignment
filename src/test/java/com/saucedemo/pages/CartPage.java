package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CART_ITEMS = By.cssSelector("[data-test='inventory-item']");
    private static final By CHECKOUT = By.id("checkout");
    private static final By CONTINUE_SHOPPING = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String title() {
        return text(TITLE);
    }

    public List<String> itemNames() {
        visible(TITLE);
        List<String> names = new ArrayList<>();
        for (WebElement e : all(ITEM_NAMES)) {
            names.add(e.getText().trim());
        }
        return names;
    }

    public int itemCount() {
        visible(TITLE);
        return all(CART_ITEMS).size();
    }

    public CartPage remove(String productName) {
        click(By.id("remove-" + productName.toLowerCase().replace(' ', '-')));
        return this;
    }

    public CheckoutPage checkout() {
        click(CHECKOUT);
        return new CheckoutPage(driver);
    }

    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING);
        return new InventoryPage(driver);
    }
}
