package com.hp.devops.demoapp.tests.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: gullery
 * Date: 03/12/14
 * Time: 15:50
 * To change this template use File | Settings | File Templates
 */
public class TestSuiteB {

    final static private boolean isMusicApp = false;

    private WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        driver= new SetUp().setUp(isMusicApp);
    }

    @Test(groups = {"Group_B"})
    public void testCaseA() {
        System.out.println("Proudly running test " + Thread.currentThread().getStackTrace()[1]);
        WebElement query;
        if (isMusicApp) {
            query = driver.findElement(By.id("bandsList"));
            Assert.assertEquals(query.getTagName(), "div");
        }
        else {
            query = driver.findElement(By.id("jenkins"));
            Assert.assertEquals(query.getTagName(), "body");
        }
        Assert.assertEquals(query.isDisplayed(), true);
    }

    @Test
    public void testCaseB() {
        System.out.println("Proudly running test " + Thread.currentThread().getStackTrace()[1]);
        WebElement query;
        if(isMusicApp){
            query = driver.findElement(By.id("totalVotes"));
            Assert.assertEquals(query.getTagName(), "div");
        }
        else {
            query = driver.findElement(By.id("jenkins"));
            Assert.assertEquals(query.getTagName(), "body");
        }
        Assert.assertEquals(query.isDisplayed(), true);
    }

    @Test(groups = {"Group_B"})
    public void testCaseC() {
        System.out.println("Proudly running test " + Thread.currentThread().getStackTrace()[1]);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        driver.quit();
    }
}