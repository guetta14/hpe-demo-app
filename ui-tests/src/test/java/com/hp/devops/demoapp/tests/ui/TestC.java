package com.hp.devops.demoapp.tests.ui;/**
 * Created by belozovs on 2/24/2015.
 */

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


public class TestC {

    static final boolean isMusicApp = false;

    static private WebDriver driver;
    static private boolean isBehindProxy = false;
    static private String testProxy;
    static private String appUrl;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        driver=new SetUp().setup_ui_tests(isMusicApp);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        driver.quit();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testUIcaseE() {
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

    @Category(SpecialTests.class)
    @Test
    public void testUIcaseF() {
        System.out.println("Proudly running test " + Thread.currentThread().getStackTrace()[1]);
        WebElement query;
        if (isMusicApp) {
            query = driver.findElement(By.id("totalVotes"));
            Assert.assertEquals(query.getTagName(), "div");
        }
        else {
            query = driver.findElement(By.id("jenkins"));
            Assert.assertEquals(query.getTagName(), "body");
        }
        Assert.assertEquals(query.isDisplayed(), true);
    }

    @Test
    public void testUIcaseAlwaysPass() {
        System.out.println("Proudly running test " + Thread.currentThread().getStackTrace()[1]);
        Assert.assertTrue(true);
    }

}
