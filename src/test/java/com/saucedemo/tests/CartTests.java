package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

public class CartTests extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    private InventoryPage inventory;

    @BeforeMethod
    public void login() {
        inventory = new LoginPage(driver).open(BASE_URL).loginAs("standard_user", PASSWORD);
        Assert.assertTrue(inventory.isLoaded(), "Precondition failed: inventory page not loaded");
    }

    @Test(description = "TC-018 Adding multiple items shows them all in the cart")
    public void addMultipleItemsAppearInCart() {
        inventory.addToCart(BACKPACK).addToCart(BIKE_LIGHT);
        Assert.assertEquals(inventory.cartCount(), 2);
        CartPage cart = inventory.openCart();
        Assert.assertEquals(cart.title(), "Your Cart");
        Assert.assertTrue(cart.itemNames().containsAll(Arrays.asList(BACKPACK, BIKE_LIGHT)));
        Assert.assertEquals(cart.itemCount(), 2);
    }
}
