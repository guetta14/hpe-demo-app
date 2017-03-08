package com.hp.devops.demoapp.tests.ui;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by zhelezny on 3/7/2017.
 */
public class SetUp {
    private WebDriver driver;
    private String autHost;
    private String autPort;
    private String proxyHost;   //  web-proxy.bbn.hp.com
    private String proxyPort;   //  8080
    private String appUrl = "";
    private String testProxy;
    private boolean isBehindProxy = false;

    public WebDriver setup_ui_tests(boolean isMusicApp ) {
        if(isMusicApp){
            testProxy = "web-proxy.bbn.hp.com:8081";
            // ???
           appUrl = "http://54.146.140.70:9001";
        } else {
            testProxy = "";
            appUrl = "http://myd-vm10629.hpeswlab.net:8080";
        }

        if ("true".equals(System.getProperty("proxy"))) {
            isBehindProxy = true;
            System.out.println("isBehindProxy is true!");
            if (System.getenv("testproxy") != null) {
                testProxy = System.getenv("testproxy");
            }
            System.out.println("testProxy is " + testProxy + "; can be modified via environment variable, i.e., 'export testproxy=web-proxy.bbn.hp.com:8080'");
        }
        else {
            System.out.println("We do not use proxy.");
        }

        if (isBehindProxy) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(testProxy);
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability(CapabilityType.PROXY, proxy);
            driver = new HtmlUnitDriver(cap);
        }
        else {
            driver = new HtmlUnitDriver();
        }
        if (System.getProperty("appUrl") != null) {
            appUrl = System.getProperty("appUrl");
        }
        System.out.println("App URL is " + appUrl + "; can be modifed via system property, i.e., '-DappUrl=\"http://54.146.140.70:9000\"'");

        driver.get(appUrl);
        return driver;
    }


}
