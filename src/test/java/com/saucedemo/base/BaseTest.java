package com.saucedemo.base;

import com.saucedemo.utils.Evidence;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest {

    protected static final String BASE_URL = System.getProperty("baseUrl", "https://www.saucedemo.com/");
    protected static final String PASSWORD = "secret_sauce";

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1366,900", "--no-sandbox", "--disable-dev-shm-usage",
                "--disable-gpu", "--disable-notifications", "--disable-search-engine-choice-screen");

        // stop chrome password popups from blocking clicks
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (driver != null && result.getStatus() == ITestResult.FAILURE) {
                Evidence.screenshot(driver, "FAILED-" + result.getMethod().getMethodName(), "target/screenshots");
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
