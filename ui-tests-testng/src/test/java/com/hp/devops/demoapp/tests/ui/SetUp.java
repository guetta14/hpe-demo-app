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

    public WebDriver setUp(boolean isMusicApp ) {

        if (isMusicApp) {
            autHost = System.getProperty("app.host");
            if (autHost == null || autHost.compareTo("") == 0) {
                autHost = "http://localhost";
            }
            autPort = System.getProperty("app.port");
            if (autPort == null || autPort.compareTo("") == 0) {
                autPort = "9999";
            }

            proxyHost = System.getProperty("proxy.host");
            proxyPort = System.getProperty("proxy.port");
            appUrl = autHost + ":" + autPort;
        }
        else {
            proxyHost = "";
            proxyPort = "";
            autHost = "http://myd-vm10629.hpeswlab.net";
            autPort = "8080";
            appUrl = autHost + ":" + autPort ;
        }
        if (proxyHost == null || proxyPort == null || proxyHost.compareTo("") == 0 || proxyPort.compareTo("") == 0) {
            driver = new HtmlUnitDriver();
        }
        else {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyHost + ":" + proxyPort);
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability(CapabilityType.PROXY, proxy);
            driver = new ChromeDriver(cap);
        }

        driver.get(appUrl);
        return driver;
    }

}
