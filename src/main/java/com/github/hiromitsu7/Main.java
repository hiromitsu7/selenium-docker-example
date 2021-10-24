package com.github.hiromitsu7;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

public class Main {
    public static final void main(String[] args) throws InterruptedException {

        WebDriver driver = DriverFactory.createRemoteFirefox(Platform.MAC);
        // WebDriver driver = DriverFactory.createLocalSafari();

        long wait = 3000;

        driver.get("https://www.selenium.dev/ja/");
        Thread.sleep(wait);
        System.out.println(driver.getTitle());

        driver.quit();
    }
}