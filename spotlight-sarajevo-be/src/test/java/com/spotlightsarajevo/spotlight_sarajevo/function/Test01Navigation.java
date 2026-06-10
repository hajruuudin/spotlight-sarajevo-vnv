package com.spotlightsarajevo.spotlight_sarajevo.function;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("all")
public class Test01Navigation extends BaseFunctionTest{
    @Test
    @Order(1)
    void testNavbarNavigationWebNotLoggedIn() throws InterruptedException {;
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/homepage", webDriver.getCurrentUrl());

        WebElement browseIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[2]/app-spots-icon"));
        actions.moveToElement(browseIcon).perform();
        Thread.sleep(1000);
        WebElement browseSpotsIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[2]/div/span[1]"));
        actions.click(browseSpotsIcon).perform();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/spots", webDriver.getCurrentUrl());
        actions.moveToElement(browseIcon).perform();
        Thread.sleep(1000);
        WebElement browseEventsIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[2]/div/span[2]"));
        actions.click(browseEventsIcon).perform();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/events", webDriver.getCurrentUrl());

        WebElement discoverIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[1]/span"));
        actions.moveToElement(discoverIcon).perform();
        WebElement discoverNavigateIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[1]/div/span"));
        actions.click(discoverNavigateIcon).perform();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/discover", webDriver.getCurrentUrl());


        WebElement guides = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[4]/span"));
        actions.moveToElement(guides).perform();
        WebElement guidesIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[4]/div/span"));
        actions.click(guidesIcon).perform();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/guide", webDriver.getCurrentUrl());

        WebElement collections = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[5]/app-collection-icon"));
        actions.moveToElement(collections).perform();
        WebElement collectionsIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[5]/div/span[1]"));
        WebElement communityIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[5]/div/span[2]"));
        WebElement transportIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[1]/div[2]/ul/li[5]/div/span[3]"));

        actions.click(collectionsIcon).perform();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/auth-benefits", webDriver.getCurrentUrl());
        actions.click(communityIcon).perform();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/auth-benefits", webDriver.getCurrentUrl());
        actions.click(transportIcon).perform();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/transport", webDriver.getCurrentUrl());
    }

    @Test
    @Order(2)
    void testNavbarNavigationMobileNotLoggedIn() throws InterruptedException {
        webDriver.manage().window().setSize(new Dimension(500, 793));
        Thread.sleep(1000);

        WebElement menuIcon = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/nav[2]/a/app-hamburger-icon"));
        actions.click(menuIcon).perform();
        WebElement navbarList = webDriver.findElement(By.xpath("/html/body/app-root/app-user/app-navbar-user/section/ul"));
        Thread.sleep(1000);

        WebElement homepageIcon = navbarList.findElement(By.xpath("./li[1]"));
        homepageIcon.click();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/homepage", webDriver.getCurrentUrl());

        actions.click(menuIcon).perform();
        Thread.sleep(1000);
        WebElement browseSpotsIcon = navbarList.findElement(By.xpath("./li[3]"));
        browseSpotsIcon.click();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/spots", webDriver.getCurrentUrl());

        actions.click(menuIcon).perform();
        Thread.sleep(1000);
        WebElement browseEventsIcon = navbarList.findElement(By.xpath("./li[4]"));
        browseEventsIcon.click();
        Thread.sleep(1000);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/events", webDriver.getCurrentUrl());

        actions.click(menuIcon).perform();
        Thread.sleep(1000);
        WebElement discoverIcon = navbarList.findElement(By.xpath("./li[5]"));
        discoverIcon.click();
        Thread.sleep(500);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/discover", webDriver.getCurrentUrl());

        actions.click(menuIcon).perform();
        Thread.sleep(1000);
        WebElement guidesIcon = navbarList.findElement(By.xpath("./li[6]"));
        guidesIcon.click();
        Thread.sleep(500);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/guides", webDriver.getCurrentUrl());

        actions.click(menuIcon).perform();
        Thread.sleep(1000);
        WebElement collectionsIcon = navbarList.findElement(By.xpath("./li[8]"));
        collectionsIcon.click();
        Thread.sleep(500);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/auth-benefits", webDriver.getCurrentUrl());

        actions.click(menuIcon).perform();
        Thread.sleep(1000);
        WebElement communityIcon = navbarList.findElement(By.xpath("./li[9]"));
        communityIcon.click();
        Thread.sleep(500);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/auth-benefits", webDriver.getCurrentUrl());

        actions.click(menuIcon).perform();
        Thread.sleep(1000);
        WebElement transportIcon = navbarList.findElement(By.xpath("./li[10]"));
        transportIcon.click();
        Thread.sleep(500);
        Assertions.assertEquals("https://spotlight-sarajevo-fe.vercel.app/transport", webDriver.getCurrentUrl());

    }
}
