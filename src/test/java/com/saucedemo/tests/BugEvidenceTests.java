package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.Evidence;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;

public class BugEvidenceTests extends BaseTest {

    private static final String SHOTS = "docs/screenshots";
    private static final String LOG = SHOTS + "/observations.txt";

    private InventoryPage loginAs(String user) {
        return new LoginPage(driver).open(BASE_URL).loginAs(user, PASSWORD);
    }

    @Test(description = "BUG-001 problem_user: product images")
    public void bug001_problemUserImages() {
        InventoryPage inventory = loginAs("problem_user");
        List<String> sources = inventory.productImageSources();
        Evidence.note(LOG, "BUG-001 image sources=" + sources + " distinct=" + new HashSet<>(sources).size() + " of " + sources.size());
        Evidence.screenshot(driver, "BUG-001-problem-user-same-images", SHOTS);
    }

    @Test(description = "BUG-002 problem_user: sorting")
    public void bug002_problemUserSorting() {
        InventoryPage inventory = loginAs("problem_user");
        List<String> before = inventory.productNames();
        inventory.sortBy("Name (Z to A)");
        List<String> after = inventory.productNames();
        Evidence.note(LOG, "BUG-002 before=" + before + " afterZtoA=" + after + " changed=" + !before.equals(after));
        Evidence.screenshot(driver, "BUG-002-problem-user-sort-ignored", SHOTS);
    }

    @Test(description = "BUG-003 problem_user: add/remove buttons")
    public void bug003_problemUserCartButtons() {
        loginAs("problem_user");
        int total = driver.findElements(By.cssSelector("button.btn_inventory")).size();
        for (int i = 0; i < total; i++) {
            // buttons get re-rendered after a click, so find them again
            List<WebElement> buttons = driver.findElements(By.cssSelector("button.btn_inventory"));
            String label = buttons.get(i).getText();
            buttons.get(i).click();
            List<WebElement> badge = driver.findElements(By.cssSelector("[data-test='shopping-cart-badge']"));
            String badgeText = badge.isEmpty() ? "none" : badge.get(0).getText();
            String labelAfter = driver.findElements(By.cssSelector("button.btn_inventory")).get(i).getText();
            Evidence.note(LOG, "BUG-003 item#" + (i + 1) + " clicked '" + label + "' -> button now '" + labelAfter + "', badge=" + badgeText);
        }
        Evidence.screenshot(driver, "BUG-003-problem-user-add-to-cart", SHOTS);

        // now try removing everything that shows Remove
        for (int i = 0; i < total; i++) {
            WebElement b = driver.findElements(By.cssSelector("button.btn_inventory")).get(i);
            if (b.getText().equalsIgnoreCase("Remove")) {
                b.click();
            }
        }
        List<WebElement> badge = driver.findElements(By.cssSelector("[data-test='shopping-cart-badge']"));
        Evidence.note(LOG, "BUG-003 after clicking Remove on every item, badge=" + (badge.isEmpty() ? "none" : badge.get(0).getText()));
        Evidence.screenshot(driver, "BUG-003-problem-user-remove-fails", SHOTS);
    }

    @Test(description = "BUG-004 problem_user: checkout last name field")
    public void bug004_problemUserCheckoutForm() {
        InventoryPage inventory = loginAs("problem_user");
        driver.findElements(By.cssSelector("button.btn_inventory")).get(0).click();
        CheckoutPage checkout = inventory.openCart().checkout();
        checkout.fillInformation("John", "Doe", "12345");
        String first = driver.findElement(By.id("first-name")).getAttribute("value");
        String last = driver.findElement(By.id("last-name")).getAttribute("value");
        Evidence.note(LOG, "BUG-004 typed first=John last=Doe -> fields hold first='" + first + "' last='" + last + "'");
        Evidence.screenshot(driver, "BUG-004-problem-user-last-name", SHOTS);
        checkout.clickContinue();
        List<WebElement> errors = driver.findElements(By.cssSelector("[data-test='error']"));
        Evidence.note(LOG, "BUG-004 after Continue, error=" + (errors.isEmpty() ? "none" : errors.get(0).getText()));
        Evidence.screenshot(driver, "BUG-004-problem-user-checkout-blocked", SHOTS);
    }

    @Test(description = "BUG-005 performance_glitch_user: slow login")
    public void bug005_performanceGlitchLogin() {
        LoginPage login = new LoginPage(driver).open(BASE_URL);
        login.enterCredentials("performance_glitch_user", PASSWORD);
        long start = System.currentTimeMillis();
        login.submit();
        new InventoryPage(driver).isLoaded();
        long elapsed = System.currentTimeMillis() - start;
        Evidence.note(LOG, "BUG-005 performance_glitch_user login took " + elapsed + " ms (standard_user is typically well under 1000 ms)");
        Evidence.screenshot(driver, "BUG-005-performance-glitch-login", SHOTS);

        // standard_user for comparison
        driver.manage().deleteAllCookies();
        LoginPage baseline = new LoginPage(driver).open(BASE_URL);
        baseline.enterCredentials("standard_user", PASSWORD);
        long s2 = System.currentTimeMillis();
        baseline.submit();
        new InventoryPage(driver).isLoaded();
        Evidence.note(LOG, "BUG-005 baseline standard_user login took " + (System.currentTimeMillis() - s2) + " ms");
    }

    @Test(description = "BUG-006 standard_user: checkout with empty cart")
    public void bug006_emptyCartCheckout() {
        InventoryPage inventory = loginAs("standard_user");
        CartPage cart = inventory.openCart();
        Evidence.note(LOG, "BUG-006 cart items before checkout=" + cart.itemCount());
        CheckoutPage checkout = cart.checkout();
        Evidence.note(LOG, "BUG-006 URL after clicking Checkout with empty cart=" + driver.getCurrentUrl());
        Evidence.screenshot(driver, "BUG-006-empty-cart-checkout-allowed", SHOTS);
        checkout.fillInformation("John", "Doe", "12345").clickContinue().finish();
        Evidence.note(LOG, "BUG-006 order result header='" + checkout.completeHeader() + "' url=" + driver.getCurrentUrl());
        Evidence.screenshot(driver, "BUG-006-empty-order-completed", SHOTS);
    }
}
