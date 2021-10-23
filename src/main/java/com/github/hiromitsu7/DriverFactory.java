package com.github.hiromitsu7;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class DriverFactory {
    public static WebDriver createLocalSafari() {
        SafariOptions options = new SafariOptions();
        return new SafariDriver(options);
    }

    public static WebDriver createLocalChrome() {
        System.setProperty("webdriver.chrome.driver", "/Users/hiro/Downloads/chromedriver_95");
        ChromeOptions options = new ChromeOptions();
        return new ChromeDriver(options);
    }

    public static WebDriver createRemoteChrome() {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        dc.setBrowserName("chrome");
        dc.setPlatform(Platform.LINUX);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1024,768");
        dc.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL("http://docker-machine:4444"), dc);
            return driver;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static WebDriver createRemoteFirefox() {
        DesiredCapabilities dc = DesiredCapabilities.firefox();
        dc.setBrowserName("firefox");
        dc.setPlatform(Platform.LINUX);
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1024,768");
        dc.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL("http://docker-machine:4444"), dc);
            return driver;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static WebDriver createRemoteEdge() {
        DesiredCapabilities dc = DesiredCapabilities.edge();
        dc.setBrowserName("edge");
        dc.setPlatform(Platform.LINUX);
        EdgeOptions options = new EdgeOptions();
        dc.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL("http://docker-machine:4444"), dc);
            return driver;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
