package com.prateek;

import com.prateek.authenticator.Authenticator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        String oneCognizant = "https://onecognizant.cognizant.com/";

        Authenticator auth = new Authenticator(driver, oneCognizant);

        // checking to see if you entered your credentials
        if (!auth.myEmail.equals("your-work-email")) {
            // this guy would run everything
            auth.goToOneCognizant();
            // app would sleep for 5 seconds before closing the browser window
            auth.closeDriver(5);
        } else {
            System.out.println("Enter your proper credentials to make this work.");
            // app would sleep for 0 seconds before closing the browser window
            auth.closeDriver(0);
        }
    }
}
