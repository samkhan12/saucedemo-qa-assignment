package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryTests extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod
    public void login() {
        inventory = new LoginPage(driver).open(BASE_URL).loginAs("standard_user", PASSWORD);
        Assert.assertTrue(inventory.isLoaded(), "Precondition failed: inventory page not loaded");
    }

    @Test(description = "TC-015 Sort by Price (low to high)")
    public void sortPriceAscending() {
        inventory.sortBy("Price (low to high)");
        List<Double> prices = inventory.productPrices();
        List<Double> expected = new ArrayList<>(prices);
        Collections.sort(expected);
        Assert.assertEquals(prices, expected);
        Assert.assertEquals(prices.get(0), 7.99, "Cheapest item should be first");
    }
}
