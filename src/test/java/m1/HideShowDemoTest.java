package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class HideShowDemoTest extends BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
            .getLogger(HideShowDemoTest.class);

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        // obtain driver from BaseTest (DriverFactory)
        driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver is null — make sure BaseTest initializes it before tests run.");
        }

        try { driver.manage().window().maximize(); } catch (Exception ignored) {}
        try { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); } catch (Exception ignored) {}

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
        log.info("Obtaining driver from BaseTest/DriverFactory");
    }

    @Test
    public void hideshowCheckTest() {
        driver.get("https://www.letskodeit.com/practice");

        // locate elements with waits to avoid stale/visibility issues
        WebElement hideshowBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='displayed-text']")));
        WebElement hideBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='hide-textbox']")));
        WebElement showBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='show-textbox']")));

        // enter text then hide / show and verify
        hideshowBox.clear();
        hideshowBox.sendKeys("Selenium");

        // scroll hide button into view then click
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", hideBtn);
        } catch (Exception ignored) {}
        hideBtn.click();
        ScreenshotUtil.capture(driver, "AfterClick_HideButton");

        // Click show and verify the textbox is displayed again
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", showBtn);
        } catch (Exception ignored) {}
        showBtn.click();

        // small wait to ensure DOM updates
        wait.until(ExpectedConditions.visibilityOf(hideshowBox));

        if (hideshowBox.isDisplayed()) {
            System.out.println("Hide/Show test case is passed successfully");
        } else {
            System.out.println("Hide/Show test case is failed");
        }

        ScreenshotUtil.capture(driver, "AfterClick_ShowButton");
    }

    // DO NOT quit the driver here; BaseTest.tearDown() will handle it
}
