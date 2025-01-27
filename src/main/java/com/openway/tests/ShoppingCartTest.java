package com.openway.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class ShoppingCartTest {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartTest.class);
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        // init chromedriver()
        driver = new ChromeDriver();
        driver.get("https://www.periplus.com/account/Login");
        logger.info("Browser initialized and navigating to the login page.");

        WebElement emailField = driver.findElement(By.name("email"));
        emailField.sendKeys("wicaksonoezekiel@gmail.com");
        logger.info("Entered email.");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("Mw28042003");
        logger.info("Entered password.");

        WebElement loginButton = driver.findElement(By.id("button-login"));
        loginButton.click();
        logger.info("Clicked the login button.");

        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("_index")) {
            logger.info("Login successful. Redirected to: {}", currentUrl);
        } else {
            logger.warn("Login failed");
        }
    }

    @Test
    public void testAddProductToCart() {
        try {
            logger.info("Starting test: testAddProductToCart.");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // wait 10 second
            WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filter_name")));
            searchBar.sendKeys("Osgood's Progressive");
            logger.info("Search term 'Osgood's Progressive' entered.");

            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
            searchButton.click();
            logger.info("Search button clicked.");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-img")));
            logger.info("Search results loaded.");

            // locate product with product-img number 1
            WebElement productContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[contains(@class, 'product-img')])[1]") // Target the second product-img
            ));

            // Find <a> inside product-img
            WebElement productLink = productContainer.findElement(By.tagName("a"));
            String hrefValue = productLink.getAttribute("href");
            driver.get(hrefValue);
            logger.info("Navigated to the URL: " + hrefValue);

            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
            WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[contains(@class, 'btn-add-to-cart')])[1]")
            ));
            cartButton.click();
            logger.info("success");

        } catch (Exception e) {
            logger.error("Test failed due to exception: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
//        driver.quit();
    }
}
