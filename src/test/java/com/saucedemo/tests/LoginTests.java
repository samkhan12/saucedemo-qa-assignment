package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(description = "TC-001 Valid login with standard_user")
    public void validLoginShowsProducts() {
        InventoryPage inventory = new LoginPage(driver).open(BASE_URL).loginAs("standard_user", PASSWORD);
        Assert.assertTrue(inventory.isLoaded(), "Products page should be displayed");
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/inventory.html"), "URL should be /inventory.html");
        Assert.assertEquals(inventory.productNames().size(), 6, "Six products expected");
    }

    @Test(description = "TC-002 Locked out user cannot log in")
    public void lockedOutUserIsRejected() {
        LoginPage login = new LoginPage(driver).open(BASE_URL);
        login.loginAs("locked_out_user", PASSWORD);
        Assert.assertEquals(login.errorMessage(), "Epic sadface: Sorry, this user has been locked out.");
        Assert.assertFalse(driver.getCurrentUrl().contains("inventory"), "Must stay on login page");
    }

    @Test(description = "TC-003 Wrong password shows generic error")
    public void wrongPasswordShowsError() {
        LoginPage login = new LoginPage(driver).open(BASE_URL);
        login.loginAs("standard_user", "wrong_password");
        Assert.assertEquals(login.errorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
    }
}
