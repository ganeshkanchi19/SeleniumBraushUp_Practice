package m1;

import java.time.Duration;
import java.util.List;

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

public class AutoSuggestionDemoTest extends BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
            .getLogger(AutoSuggestionDemoTest.class);

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        // obtain the driver created by BaseTest/DriverFactory
        driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver is null — BaseTest did not initialize it!");
        }

        // safe operations (some remote environments may not support maximize)
        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {
        }

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        } catch (Exception ignored) {
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void autoSuggestionCheck() throws InterruptedException {
        // Enter the url or open the Webpage
        driver.get("https://www.google.com");

        // 1) Type query
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
        searchInput.clear();
        searchInput.sendKeys("lambdatest");

        // small pause to let suggestions populate (explicit wait isn't reliable for some dynamic suggestion lists)
        Thread.sleep(2000);

        ScreenshotUtil.capture(driver, "AutoSuggestion_BeforeSelecting");

        // Find all the auto-suggestion elements
        List<WebElement> autoSugg = driver.findElements(By.xpath("//span[contains(text(),'lambdatest')]"));
        int count = autoSugg.size();
        System.out.println("The auto suggestion count is : " + count);

        // Print all the text of the auto suggestions in the console
        for (int i = 0; i < count; i++) {
            String autoSuggText = autoSugg.get(i).getText();
            System.out.println(autoSuggText);
        }

        // Select the first auto suggestion if present
        if (count > 0) {
            autoSugg.get(0).click();
        } else {
            log.warn("No auto-suggestions found for query 'lambdatest'");
        }

        ScreenshotUtil.capture(driver, "AutoSuggestion_AfterSelecting");
    }

    // No @AfterTest/@AfterMethod here — BaseTest handles closing/quitting the driver.
}
