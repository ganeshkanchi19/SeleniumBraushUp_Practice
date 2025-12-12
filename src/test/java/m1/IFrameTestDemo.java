package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;
import utils.ScrollUtil;

public class IFrameTestDemo extends BaseTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setup() {

        driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver is null — BaseTest failed to initialize driver.");
        }

        try { driver.manage().window().maximize(); } catch (Exception ignored) {}
        try { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); } catch (Exception ignored) {}

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void iframeCheck() {

        // Navigate to URL
        driver.get("https://demoqa.com/frames");

        // Switch to iframe using ID
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame1"));

        // Locate the heading inside iframe
        WebElement frame1head = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@id='sampleHeading']"))
        );

        // Scroll to the element (your custom utility)
        ScrollUtil.scrollToElement(driver, frame1head);

        // Get text
        String frame1Text = frame1head.getText();
        System.out.println("Text of the frame is : " + frame1Text);

        // Screenshot
        ScreenshotUtil.capture(driver, "Frame1Demo");

        // IMPORTANT: Switch back to main content after iframe usage
        driver.switchTo().defaultContent();
    }

    // No @AfterTest → BaseTest handles quitting the browser
}
