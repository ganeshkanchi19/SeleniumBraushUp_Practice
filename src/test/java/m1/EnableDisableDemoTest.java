package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import base.BaseTest;
import utils.ScreenshotUtil;

public class EnableDisableDemoTest extends BaseTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private static final org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger(EnableDisableDemoTest.class);

    // ------------------------- SETUP ------------------------------
    @BeforeMethod(alwaysRun = true)
    public void setup() {

        driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver is null — BaseTest did not initialize WebDriver!");
        }

        try { driver.manage().window().maximize(); } catch (Exception ignored) {}

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;

        log.info("Driver successfully initialized for EnableDisableDemoTest");
    }

    // ---------------------------- TEST -----------------------------
    @Test
    public void enabledisableCheck() {

        driver.get("https://www.letskodeit.com/practice");

        WebElement field = driver.findElement(By.id("enabled-example-input"));
        field.sendKeys("Java");

        String beforeDisable = field.getAttribute("value");
        System.out.println("Text before disabling: " + beforeDisable);

        WebElement disableBtn = driver.findElement(By.id("disabled-button"));
        disableBtn.click();

        if (!field.isEnabled()) {
            System.out.println("PASS: Field disabled successfully.");
        } else {
            System.out.println("FAIL: Field did NOT disable.");
        }

        ScreenshotUtil.capture(driver, "EnableDisable_Check");

        // Re-enable the field
        WebElement enableBtn = driver.findElement(By.id("enabled-button"));
        wait.until(ExpectedConditions.elementToBeClickable(enableBtn));
        enableBtn.click();

        // Wait until field becomes enabled again
        wait.until(d -> field.isEnabled());

        // Append "Programming" using Actions to ensure cursor moves to end
        Actions actions = new Actions(driver);
        actions.moveToElement(field).click().sendKeys(Keys.END).perform();
        field.sendKeys("Programming");

        String finalValue = field.getAttribute("value");
        System.out.println("Final text: " + finalValue);

        if ("JavaProgramming".equalsIgnoreCase(finalValue)) {
            System.out.println("PASS: Final text is correct");
        } else {
            System.out.println("FAIL: Expected 'JavaProgramming' but got: " + finalValue);
        }

        ScreenshotUtil.capture(driver, "EnableDisable_Final");
    }

    // -------------------------- CLEANUP ----------------------------
    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        // DO NOT QUIT DRIVER HERE — BaseTest handles cleanup!
    }
}
