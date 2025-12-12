package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import base.BaseTest;
import utils.ScreenshotUtil;

/**
 * DymamicWebTable (refactored)
 *
 * - Uses BaseTest.getDriver() instead of creating a local ChromeDriver.
 * - Initializes wait/js in @BeforeMethod so it works in parallel/grid runs.
 * - Does not quit the driver locally; BaseTest.tearDown handles that.
 */
public class DymamicWebTable extends BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private static final org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger(DymamicWebTable.class);

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        // get the driver supplied by BaseTest/DriverFactory
        driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver is null — BaseTest failed to initialize it.");
        }

        // safe setup
        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {}

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        } catch (Exception ignored) {}

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
        log.info("Obtained driver from BaseTest/DriverFactory");
    }

    @Test
    public void extractCellValue() {
        driver.get("https://testautomationpractice.blogspot.com/#");

        // 1. Find all column headers
        List<WebElement> headers = driver.findElements(By.xpath("//table[@id='taskTable']//th"));
        int memoryColIndex = -1;

        // 2. Loop through headers to find the index of "Memory (MB)"
        for (int i = 0; i < headers.size(); i++) {
            String text = headers.get(i).getText().trim();
            if ("Memory (MB)".equalsIgnoreCase(text)) {
                memoryColIndex = i + 1; // XPath indexes are 1-based
                break;
            }
        }

        if (memoryColIndex == -1) {
            throw new RuntimeException("Memory (MB) column not found!");
        }

        // Build a robust locator for the cell in the row where the first column equals "Internet Explorer"
        String xpath = String.format("//table[@id='taskTable']//tr[td[1][normalize-space() = 'Internet Explorer']]/td[%d]", memoryColIndex);
        WebElement memoryCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

        // ensure visible
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", memoryCell);

        String memoryVal = memoryCell.getText().trim();
        System.out.println("Memory(MB) for Internet Explorer: " + memoryVal);

        ScreenshotUtil.capture(driver, "ExtractCellValue_DynamicWebTable");
    }

    // No @AfterMethod/@AfterTest quitting — BaseTest.tearDown() will remove the driver
}
