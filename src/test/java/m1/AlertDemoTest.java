package m1;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class AlertDemoTest extends BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;

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

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void alertcheck() {
        driver.get("https://demoqa.com/alerts");

        // explicit wait helper
        WebElement alertBtn = driver.findElement(By.xpath("//button[@id='alertButton']"));

        // wait until clickable
        wait.until(ExpectedConditions.elementToBeClickable(alertBtn));

        // scroll into view (center) so layout is predictable
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", alertBtn);

        // debug screenshot before click (helps when running in CI)
        ScreenshotUtil.capture(driver, "BeforeClick_alertButton");

        // Try to click. If blocked by overlay/iframe, hide the iframe and retry; fallback to JS click.
        try {
            alertBtn.click();
        } catch (ElementClickInterceptedException ex) {
            ScreenshotUtil.capture(driver, "ClickIntercepted_beforeFix");

            // attempt to hide common ad iframes that intercept clicks
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "var frames = document.querySelectorAll('iframe[id^=\"google_ads_iframe_\"]');"
                                + "for(var i=0;i<frames.length;i++){ frames[i].style.display='none'; }");
                try { Thread.sleep(400); } catch (InterruptedException ie) { /* ignore */ }
            } catch (Exception jsEx) {
                // ignore - we'll fall back to JS click
            }

            // try clicking again normally, else JS click
            try {
                alertBtn.click();
            } catch (Exception ex2) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", alertBtn);
            }
        }

        // Wait for the alert to appear
        wait.until(ExpectedConditions.alertIsPresent());

        // read & accept the alert
        Alert simAlert = driver.switchTo().alert();
        String alertText = simAlert.getText();
        System.out.println("The text displayed in the alert is : " + alertText);
        simAlert.accept();

        // post-alert screenshot
        ScreenshotUtil.capture(driver, "Alert_afterAccept");
    }

    // No @AfterMethod/@AfterTest here — BaseTest handles quitting the driver.
}
