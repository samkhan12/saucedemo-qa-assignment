package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    private InventoryPage inventory;

    @BeforeMethod
    public void login() {
        inventory = new LoginPage(driver).open(BASE_URL).loginAs("standard_user", PASSWORD);
        Assert.assertTrue(inventory.isLoaded(), "Precondition failed: inventory page not loaded");
    }

    private CheckoutPage goToCheckoutWithTwoItems() {
        inventory.addToCart(BACKPACK).addToCart(BIKE_LIGHT);
        return inventory.openCart().checkout();
    }

    @Test(description = "TC-022 End-to-end purchase of two items completes the order")
    public void endToEndPurchase() {
        CheckoutPage checkout = goToCheckoutWithTwoItems()
                .fillInformation("John", "Doe", "12345")
                .clickContinue();
        Assert.assertEquals(checkout.title(), "Checkout: Overview");
        checkout.finish();
        Assert.assertEquals(checkout.completeHeader(), "Thank you for your order!");
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/checkout-complete.html"));
    }

    @Test(description = "TC-025 Postal code is mandatory")
    public void postalCodeIsRequired() {
        CheckoutPage checkout = goToCheckoutWithTwoItems()
                .fillInformation("John", "Doe", "").clickContinue();
        Assert.assertEquals(checkout.errorMessage(), "Error: Postal Code is required");
    }

    @Test(description = "TC-026 Order total equals item total plus tax")
    public void orderTotalIsCalculatedCorrectly() {
        CheckoutPage checkout = goToCheckoutWithTwoItems()
                .fillInformation("John", "Doe", "12345").clickContinue();
        double expectedSubtotal = 29.99 + 9.99;
        Assert.assertEquals(checkout.subtotal(), expectedSubtotal, 0.001, "Item total");
        Assert.assertEquals(checkout.total(), checkout.subtotal() + checkout.tax(), 0.011, "Total = subtotal + tax");
    }
}
