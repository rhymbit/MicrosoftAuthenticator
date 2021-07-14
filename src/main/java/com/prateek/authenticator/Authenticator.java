package com.prateek.authenticator;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Authenticator {
    WebDriver driver;
    WebDriverWait wait;
    String baseUrl;

    // Your Credentials
    public String myEmail = "your-work-email";
    String myPassword = "your-password";
    String mySecretKey = "your-secret-key";

    // locators
    By emailBy = By.xpath("//input[@id='i0116']");
    By nextButtonBy = By.xpath("//input[@id='idSIButton9']");
    By passwordBy = By.xpath("//input[@id='i0118']");
    By signAnotherWayBy = By.xpath("//a[@id='signInAnotherWay']");
    By useVerificationCodeBy = By.xpath("//div[contains(text(),'Use a verification code from my mobile app')]");
    By enterCodeBy = By.xpath("//input[@id='idTxtBx_SAOTCC_OTC']");
    By verifyBy = By.xpath("//input[@id='idSubmit_SAOTCC_Continue']");
    By staySignedInNoBy = By.xpath("//input[@id='idBtn_Back']");

    public Authenticator(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        wait = new WebDriverWait(driver, 10);
    }

    public void goToOneCognizant() {
        driver.get(baseUrl);

        // wait for email text-box
        explicitWait(emailBy);

        // enter your email and click next
        driver.findElement(emailBy).sendKeys(myEmail);
        driver.findElement(nextButtonBy).click();

        // wait for password text-box
        explicitWait(passwordBy);

        // enter your password
        driver.findElement(passwordBy).sendKeys(myPassword);

        // Wait for Sign-In Button
        // I had to use a sleep method here
        // It didn't work for me with explicit wait and implicit wait
        // I didn't try fluent wait.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}
        actionPressEnter(driver);

        // // wait for sign another way link and click on it
        explicitWait(signAnotherWayBy);
        driver.findElement(signAnotherWayBy).click();

        // wait for use verification code button and click it
        explicitWait(useVerificationCodeBy);
        driver.findElement(useVerificationCodeBy).click();

        // wait for enter-code text box, get code from authenticator code
        // and enter the code
        explicitWait(enterCodeBy);
        String code = getTOTPCode(mySecretKey);
        driver.findElement(enterCodeBy).sendKeys(code);

        // wait for verify-button and click it
        explicitWait(verifyBy);
        driver.findElement(verifyBy).click();

        // wait for stay-signed-in button and click on `no`
        explicitWait(staySignedInNoBy);
        driver.findElement(staySignedInNoBy).click();
    }

    public void explicitWait(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static String getTOTPCode(String secretKey) {
        secretKey = secretKey.toUpperCase();
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static void actionPressEnter(WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER);
        actions.build().perform();
    }


    public void closeDriver(int sleepTime) {
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {}
        driver.quit();
    }
}
