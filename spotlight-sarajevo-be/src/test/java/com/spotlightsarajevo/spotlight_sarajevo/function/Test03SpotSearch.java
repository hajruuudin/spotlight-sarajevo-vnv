package com.spotlightsarajevo.spotlight_sarajevo.function;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("all")
public class Test03SpotSearch extends BaseFunctionTest {

    @Order(1)
    @Test
    void testSpotSearchWeb() throws InterruptedException {
        // Navigate to spot search page
        webDriver.navigate().to(baseUrl + "/spots");
        Thread.sleep(2000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/spots", webDriver.getCurrentUrl());

        // Test searching for absurd term and verify no results
        WebElement searchBar = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/div/app-search-bar/div/input"));

        // Verify results are now displayed
        List<WebElement> resultCards = webDriver.findElements(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[2]/div/app-search-spot-card"));
        Assertions.assertTrue(resultCards.size() > 0, "Should display spot results after clearing search");
        Thread.sleep(1000);

        // Test opening sort popup
        WebElement sortButton = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[1]/button[1]"));
        sortButton.click();
        Thread.sleep(1000);

        // Verify sort popup is visible
        WebElement sortPopup = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[1]/div/div[1]"));
        Assertions.assertTrue(sortPopup.isDisplayed(), "Sort popup should be visible");
        Thread.sleep(800);

        // Move mouse away to close sort popup
        actions.moveToElement(sortButton).moveByOffset(0, -100).perform();
        Thread.sleep(1000);

        // Test opening filter popup
        WebElement filterButton = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[1]/button[2]"));
        filterButton.click();
        Thread.sleep(1000);

        // Verify filter popup is visible
        WebElement filterPopup = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[1]/div"));
        Assertions.assertTrue(filterPopup.isDisplayed(), "Filter popup should be visible");
        Thread.sleep(800);

        // Move mouse away to close filter popup
        actions.moveToElement(filterButton).moveByOffset(0, -100).perform();
        Thread.sleep(1000);

        // Click on a random result card (first card if available)
        List<WebElement> spotCards = webDriver.findElements(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[2]/div/app-search-spot-card"));
        if (spotCards.size() > 0) {
            spotCards.get(0).click();
            Thread.sleep(2000);

            // Verify we navigated to spot overview page
            String currentUrl = webDriver.getCurrentUrl();
            Assertions.assertTrue(currentUrl.contains("/spots/"), "Should navigate to spot overview page");
        }

        webDriver.navigate().to(baseUrl + "/spots");

        searchBar = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/div/app-search-bar/div/input"));
        searchBar.clear();
        Thread.sleep(1000);
        searchBar.sendKeys("fjoeisjhfgoir");
        Thread.sleep(2000);

        WebElement searchButton = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/div/app-search-bar/div/button"));
        searchButton.click();
        Thread.sleep(2000);

        // Verify no results message appears
        WebElement noResultsComponent = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[2]/app-not-found-component"));
        Assertions.assertNotNull(noResultsComponent, "Should show no results component");
        Thread.sleep(1000);
    }

    @Order(2)
    @Test
    void testSpotSearchMobile() throws InterruptedException {
        // Set mobile viewport
        webDriver.manage().window().setSize(new Dimension(500, 793));
        Thread.sleep(1500);

        // Navigate to spot search page
        webDriver.navigate().to(baseUrl + "/spots");
        Thread.sleep(2000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/spots", webDriver.getCurrentUrl());

        // Test searching for absurd term on mobile
        WebElement searchBar = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/div[1]/app-search-bar/input"));
        searchBar.clear();
        searchBar.sendKeys("fjoeisjhfgoir");
        searchBar.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        // Verify no results on mobile
        WebElement noResultsComponent = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[2]/app-not-found-component"));
        Assertions.assertNotNull(noResultsComponent, "Should show no results on mobile");
        Thread.sleep(1000);

        // Clear search on mobile
        searchBar.clear();
        searchBar.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        // Verify results on mobile
        List<WebElement> resultCards = webDriver.findElements(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[2]/div/app-search-spot-card"));
        Assertions.assertTrue(resultCards.size() > 0, "Should display results on mobile");
        Thread.sleep(1000);

        // Test opening sort popup on mobile
        WebElement sortButton = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[1]/div/button[1]"));
        sortButton.click();
        Thread.sleep(1000);

        // Verify sort popup on mobile
        WebElement sortPopup = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[1]/div/div[1]"));
        Assertions.assertTrue(sortPopup.isDisplayed(), "Sort popup should be visible on mobile");
        Thread.sleep(800);

        // Move away to close sort popup on mobile
        actions.moveToElement(sortButton).moveByOffset(0, -100).perform();
        Thread.sleep(1000);

        // Test opening filter popup on mobile
        WebElement filterButton = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[1]/div/button[2]"));
        filterButton.click();
        Thread.sleep(1000);

        // Verify filter popup on mobile
        WebElement filterPopup = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[1]/div/div[2]"));
        Assertions.assertTrue(filterPopup.isDisplayed(), "Filter popup should be visible on mobile");
        Thread.sleep(800);

        // Move away to close filter popup
        actions.moveToElement(filterButton).moveByOffset(0, -100).perform();
        Thread.sleep(1000);

        // Click on random result card on mobile
        List<WebElement> spotCards = webDriver.findElements(By.xpath("/html/body/app-root/app-user/app-spot-search/section/div/form/section/div[2]/div/app-search-spot-card"));
        if (spotCards.size() > 0) {
            spotCards.get(0).click();
            Thread.sleep(2000);

            // Verify navigation on mobile
            String currentUrl = webDriver.getCurrentUrl();
            Assertions.assertTrue(currentUrl.contains("/spots/"), "Should navigate to spot overview on mobile");
        }
    }
}
