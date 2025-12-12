package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class TooltipDemoTest extends BaseTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void init() {
        driver = getDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void tooltipHandlecheck() {

        driver.get("https://jqueryui.com/tooltip/");

        Actions actions = new Actions(driver);
        String expectedTooltip = "We ask for your age only for statistical purposes.";

        // Switch to the example frame
        driver.switchTo().frame(0);

        WebElement ageField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("age"))
        );
        actions.moveToElement(ageField).perform();

        WebElement tooltip = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".ui-tooltip .ui-tooltip-content")
                )
        );

        String actualTooltip = tooltip.getText().trim();
        System.out.println("Tooltip Text: " + actualTooltip);

        if (actualTooltip.equals(expectedTooltip)) {
            System.out.println("Tooltip test PASSED");
        } else {
            System.out.println("Tooltip test FAILED");
        }

        ScreenshotUtil.capture(driver, "TooltipCheck");
    }
}
