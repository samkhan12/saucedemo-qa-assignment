package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage extends BasePage {

    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By ITEM_PRICES = By.cssSelector("[data-test='inventory-item-price']");
    private static final By ITEM_IMAGES = By.cssSelector(".inventory_item_img img");
    private static final By SORT = By.cssSelector("[data-test='product-sort-container']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    private static final By RESET_LINK = By.id("reset_sidebar_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Products".equals(text(TITLE));
    }

    public List<String> productNames() {
        visible(ITEM_NAMES);
        List<String> names = new ArrayList<>();
        for (WebElement e : all(ITEM_NAMES)) {
            names.add(e.getText().trim());
        }
        return names;
    }

    public List<Double> productPrices() {
        visible(ITEM_PRICES);
        List<Double> prices = new ArrayList<>();
        for (WebElement e : all(ITEM_PRICES)) {
            prices.add(Double.parseDouble(e.getText().replace("$", "").trim()));
        }
        return prices;
    }

    public List<String> productImageSources() {
        visible(ITEM_IMAGES);
        List<String> sources = new ArrayList<>();
        for (WebElement e : all(ITEM_IMAGES)) {
            sources.add(e.getAttribute("src"));
        }
        return sources;
    }

    public void sortBy(String visibleText) {
        new Select(visible(SORT)).selectByVisibleText(visibleText);
    }

    private static String slug(String productName) {
        return productName.toLowerCase().replace(' ', '-');
    }

    public InventoryPage addToCart(String productName) {
        click(By.id("add-to-cart-" + slug(productName)));
        return this;
    }

    public InventoryPage removeFromCart(String productName) {
        click(By.id("remove-" + slug(productName)));
        return this;
    }

    public boolean hasRemoveButton(String productName) {
        return !all(By.id("remove-" + slug(productName))).isEmpty();
    }

    public int cartCount() {
        List<WebElement> badge = all(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText().trim());
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
        return new LoginPage(driver);
    }

    public InventoryPage resetAppState() {
        click(MENU_BUTTON);
        click(RESET_LINK);
        return this;
    }
}
