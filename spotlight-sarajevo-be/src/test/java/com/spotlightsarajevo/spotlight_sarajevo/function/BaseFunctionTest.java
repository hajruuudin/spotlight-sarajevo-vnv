package com.spotlightsarajevo.spotlight_sarajevo.function;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import java.time.Duration;

@SuppressWarnings("all")
public class BaseFunctionTest {
    protected static WebDriver webDriver;
    protected static String baseUrl;
    protected static JavascriptExecutor js;
    protected static Actions actions;

    /**
     *
     * This is the setup done initially before all tests are started.
     * Basically set the baseUrl and the driver for each user.
     * Depending on who is running the tests, we use the appropriate driver.
     *
     */
    @BeforeAll
    static void setUp() throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "/Users/hajrudin.imamovic/geckodriver");

//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.90 Safari/537.36");
//        options.addArguments("--start-maximized");
//        options.addArguments("--ignore-certificate-errors");
//        options.addArguments("--disable-features=IsolateOrigins,site-per-process");
//        options.addArguments("--disable-blink-features=AutomationControlled");
//        options.setExperimentalOption("useAutomationExtension", false);
//        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
//        options.addArguments("--disable-extensions");
//        options.addArguments("--enable-javascript");
//        options.addArguments("--disable-cookie-encryption");
//        options.addArguments("--disable-extensions");
//        options.addArguments("--disable-component-update");
//        options.addArguments("--disable-background-networking");
        FirefoxOptions options = new FirefoxOptions();
        webDriver = new FirefoxDriver(options);
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.90 Safari/537.36");
        options.addArguments("--enable-javascript");
        baseUrl = "https://spotlight-sarajevo-fe.vercel.app";

        webDriver.get(baseUrl);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        js = (JavascriptExecutor) webDriver;
        actions = new Actions(webDriver);

        Thread.sleep(2000);
    }

    /**
     * This is done after each test is individually completed. We return to the base URL.
     * Because many test initially start from the base URL, we return to the base every time.
     */
    @AfterEach
    void returnToHome(){
        webDriver.navigate().to(baseUrl);
    }

    /**
     * After each test scenario is completed, we quit the Selenium web driven windows.
     */
    @AfterAll
    static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}