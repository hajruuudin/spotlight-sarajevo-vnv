package com.spotlightsarajevo.spotlight_sarajevo.function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@SuppressWarnings("all")
public class Test04EventSearch extends BaseFunctionTest {
    @Order(1)
    @Test
    void testEventSearchWeb() throws InterruptedException {
        // Navigate to event search page
        webDriver.navigate().to(baseUrl + "/events");
        Thread.sleep(2000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/events", webDriver.getCurrentUrl());

        WebElement searchBar = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/div/app-search-bar/div/input"));

        WebElement sortBar = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[1]/button[1]"));
        sortBar.click();
        Thread.sleep(1000);
        WebElement alphabeticalSort = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[1]/div/div/app-sorting-selector[1]"));
        alphabeticalSort.click();
        Thread.sleep(1000);
        actions.moveToElement(sortBar).moveByOffset(0, -100).perform();

        // Verify results are now displayed
        List<WebElement> resultCards = webDriver.findElements(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[2]/div/app-search-event-card"));
        Assertions.assertTrue(resultCards.size() > 0, "Should display event results after clearing search");
        Thread.sleep(1000);

        // Test opening sort popup
        WebElement sortButton = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[1]/button[1]"));
        sortButton.click();
        Thread.sleep(1000);

        // Verify sort popup is visible
        WebElement sortPopup = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[1]/div/div[1]"));
        Assertions.assertTrue(sortPopup.isDisplayed(), "Sort popup should be visible");
        Thread.sleep(800);

        // Move mouse away to close sort popup
        actions.moveToElement(sortButton).moveByOffset(0, -100).perform();
        Thread.sleep(1000);

        // Test opening filter popup
        WebElement filterButton = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[1]/button[2]"));
        filterButton.click();
        Thread.sleep(1000);

        // Verify filter popup is visible
        WebElement filterPopup = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[1]/div"));
        Assertions.assertTrue(filterPopup.isDisplayed(), "Filter popup should be visible");
        Thread.sleep(800);

        // Move mouse away to close filter popup
        actions.moveToElement(filterButton).moveByOffset(0, -100).perform();
        Thread.sleep(1000);

        // Click on a random result card (first card if available)
        List<WebElement> eventCards = webDriver.findElements(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[2]/div/app-search-event-card"));
        if (eventCards.size() > 0) {
            eventCards.get(0).click();
            Thread.sleep(2000);

            // Verify we navigated to spot overview page
            String currentUrl = webDriver.getCurrentUrl();
            Assertions.assertTrue(currentUrl.contains("/events/"), "Should navigate to event overview page");
        }

        webDriver.navigate().to(baseUrl + "/events");

        searchBar = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/div/app-search-bar/div/input"));
        searchBar.clear();
        Thread.sleep(1000);
        searchBar.sendKeys("fjoeisjhfgoir");
        Thread.sleep(2000);

        WebElement searchButton = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/div/app-search-bar/div/button"));
        searchButton.click();
        Thread.sleep(2000);

        // Verify no results message appears
        WebElement noResultsComponent = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-event-search/section/div/form/section/div[2]/app-not-found-component"));
        Assertions.assertNotNull(noResultsComponent, "Should show no results component");
        Thread.sleep(1000);
    }
}
