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

public class AutoCompleteDemoTest extends BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
            .getLogger(AutoCompleteDemoTest.class);

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        // obtain the driver created by BaseTest/DriverFactory
        driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver is null — BaseTest did not initialize it!");
        }

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        } catch (Exception ignored) {}

        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {}

        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void autocompleteCheck() {
        driver.get("https://jqueryui.com/autocomplete/");

        // switch to demo frame
        driver.switchTo().frame(0);

        // Wait for the tag input to be visible and type
        WebElement tagBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='tags']")));
        tagBox.sendKeys("ja");

        log.info("Waiting for suggestions to appear");
        List<WebElement> suggestions = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("ul.ui-menu li div")));

        // Select "JavaScript"
        for (WebElement option : suggestions) {
            if ("JavaScript".equals(option.getText().trim())) {
                option.click();
                log.info("Selected the targeted option successfully");
                break;
            }
        }

        // screenshot
        ScreenshotUtil.capture(driver, "AutoComplete");
    }

    // No @AfterMethod/@AfterTest here — BaseTest handles quitting the driver.
}
