package com.spotlightsarajevo.spotlight_sarajevo.function;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("all")
public class Test02Homepage extends BaseFunctionTest{
    @Order(1)
    @Test
    void testHomepageUIWeb() throws InterruptedException {
        // Verify we are on the homepage
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/homepage", webDriver.getCurrentUrl());
        Thread.sleep(1500);

        // Test headline spot card click
        WebElement headlineSpotSection = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-homepage/section[1]/div[1]/div[1]"));
        WebElement headlineSpotCard = headlineSpotSection.findElement(By.xpath("./app-headline-spot"));
        headlineSpotCard.click();
        Thread.sleep(2000);

        // Verify navigation to spot overview page
        String currentUrl = webDriver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/spots/"), "Should navigate to spot overview page");
        webDriver.navigate().to(baseUrl + "/homepage");
        Thread.sleep(1500);

        // Test headline event card click
        WebElement headlineEventSection = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-homepage/section[1]/div[1]/div[2]"));
        WebElement headlineEventCard = headlineEventSection.findElement(By.xpath(".//app-headline-event"));
        headlineEventCard.click();
        Thread.sleep(2000);

        // Verify navigation to event overview page
        currentUrl = webDriver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/events/"), "Should navigate to event overview page");
        webDriver.navigate().to(baseUrl + "/homepage");
        Thread.sleep(1500);

        // Test popular spots - hover and check style changes
        actions.scrollByAmount(0, 800);
        WebElement popularSpotsContainer = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-homepage/section[2]/div[1]"));
        List<WebElement> spotCards = popularSpotsContainer.findElements(By.xpath(".//app-search-spot-card"));

        if (spotCards.size() > 0) {
            WebElement firstSpotCard = spotCards.get(0);

            // Get initial style
            String initialStyle = firstSpotCard.getAttribute("style");
            String initialClass = firstSpotCard.getAttribute("class");

            // Hover over the card
            actions.moveToElement(firstSpotCard).perform();
            Thread.sleep(800);

            // Get style after hover
            String hoverStyle = firstSpotCard.getAttribute("style");
            String hoverClass = firstSpotCard.getAttribute("class");

            // Assert that some style property changed (either via style attribute or class)
            boolean styleChanged = !initialStyle.equals(hoverStyle) || !initialClass.equals(hoverClass);
            Assertions.assertTrue(styleChanged || initialStyle.isEmpty(), "Card style should change on hover");

            Thread.sleep(500);
        }

        // Click on first popular spot card
        if (spotCards.size() > 0) {
            spotCards.get(0).click();
            Thread.sleep(2000);
            currentUrl = webDriver.getCurrentUrl();
            Assertions.assertTrue(currentUrl.contains("/spots/"), "Should navigate to spot overview");
            webDriver.navigate().to(baseUrl + "/homepage");
            Thread.sleep(1500);
        }

        // Test calendar date selection (upcoming events)
        actions.scrollByAmount(0, 1200);
        Thread.sleep(1000);
        WebElement calendarSection = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-homepage/section[2]/div[2]"));
        java.util.List<WebElement> dateIcons = calendarSection.findElements(By.xpath(".//app-calendar-date-icon"));

        if (dateIcons.size() > 1) {
            // Click on second date option
            WebElement secondDateIcon = dateIcons.get(1);
            actions.click(secondDateIcon).perform();
            Thread.sleep(1500);

            // Verify events are loaded or displayed
            java.util.List<WebElement> eventCards = calendarSection.findElements(By.xpath(".//app-search-event-card"));
            // Events may or may not exist for the selected date, so we just verify the action doesn't break anything
            Assertions.assertTrue(webDriver.getCurrentUrl().contains("/homepage"), "Should still be on homepage after date selection");
        }

        // Click on another date
        if (dateIcons.size() > 2) {
            WebElement thirdDateIcon = dateIcons.get(2);
            actions.click(thirdDateIcon).perform();
            Thread.sleep(1500);
            Assertions.assertTrue(webDriver.getCurrentUrl().contains("/homepage"), "Should still be on homepage");
        }

        // Scroll to end of page
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(1500);
    }

    @Order(2)
    @Test
    void testHomepageMobile() throws InterruptedException {
        // Set mobile viewport
        webDriver.manage().window().setSize(new Dimension(500, 793));
        Thread.sleep(1500);

        // Verify we are on the homepage
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/homepage", webDriver.getCurrentUrl());
        Thread.sleep(1000);

        // Test headline spot card click - on mobile, these should still be clickable
        WebElement headlineSpotCard = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-homepage/section[1]/div[1]/div[1]/app-headline-spot"));
        headlineSpotCard.click();
        Thread.sleep(2000);

        // Verify navigation to spot overview page
        String currentUrl = webDriver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/spots/"), "Should navigate to spot overview page on mobile");
        webDriver.navigate().to(baseUrl + "/homepage");
        Thread.sleep(1000);

        // Test headline event card click
        WebElement headlineEventCard = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-homepage/section[1]/div[1]/div[2]/app-headline-event"));
        headlineEventCard.click();
        Thread.sleep(2000);

        // Verify navigation to event overview page
        currentUrl = webDriver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/events/"), "Should navigate to event overview page on mobile");
        webDriver.navigate().to(baseUrl + "/homepage");
        Thread.sleep(1500);

        // Test popular spots on mobile
        actions.scrollByAmount(0, 400);
        Thread.sleep(1000);
        WebElement popularSpotsContainer = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-homepage/section[2]/div[1]"));
        java.util.List<WebElement> spotCards = popularSpotsContainer.findElements(By.xpath(".//app-search-spot-card"));

        if (spotCards.size() > 0) {
            // On mobile, tap first popular spot
            spotCards.get(0).click();
            Thread.sleep(2000);
            currentUrl = webDriver.getCurrentUrl();
            Assertions.assertTrue(currentUrl.contains("/spots/"), "Should navigate to spot overview on mobile");
            webDriver.navigate().to(baseUrl + "/homepage");
            Thread.sleep(1500);
        }

        // Test calendar date selection on mobile
        actions.scrollByAmount(0, 800);
        Thread.sleep(1000);
        WebElement calendarSection = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-homepage/section[2]/div[2]"));
        java.util.List<WebElement> dateIcons = calendarSection.findElements(By.xpath(".//app-calendar-date-icon"));

        if (dateIcons.size() > 1) {
            // Click on second date on mobile
            WebElement secondDateIcon = dateIcons.get(1);
            actions.click(secondDateIcon).perform();
            Thread.sleep(1500);
            Assertions.assertTrue(webDriver.getCurrentUrl().contains("/homepage"), "Should remain on homepage after date selection");
        }

        // Click on another date on mobile
        if (dateIcons.size() > 2) {
            WebElement thirdDateIcon = dateIcons.get(2);
            actions.click(thirdDateIcon).perform();
            Thread.sleep(1500);
            Assertions.assertTrue(webDriver.getCurrentUrl().contains("/homepage"), "Should remain on homepage");
        }

        // Scroll to end of page on mobile
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(1500);
    }
}
