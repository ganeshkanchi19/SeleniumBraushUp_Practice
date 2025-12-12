package m1;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class verifyPageTitle extends BaseTest {

    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void init() {
        // Get driver provided by BaseTest (DriverFactory)
        driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver is null — BaseTest did not initialize it.");
        }

        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {}

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void verifyPageTitleTest() {
        driver.get("https://www.letskodeit.com/practice");

        String expTitle = "Practice Page";
        String actTitle = driver.getTitle();

        // Take screenshot
        ScreenshotUtil.capture(driver, "PageTitleVerification");

        System.out.println("Actual Title: " + actTitle);

        Assert.assertEquals(actTitle, expTitle, "Page title validation failed!");
    }
}
