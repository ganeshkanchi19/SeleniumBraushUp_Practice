package m1;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class AlertDemoTest extends BaseTest {
    WebDriver driver;

    @BeforeTest
    public void launchBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void alertcheck() throws IOException {
        driver.get("https://demoqa.com/alerts");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement alertBtn = driver.findElement(By.xpath("//button[@id='alertButton']"));

        // wait until clickable
        wait.until(ExpectedConditions.elementToBeClickable(alertBtn));

        // scroll into view (center) so layout is predictable
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", alertBtn);

        // debug screenshot before click (helps when running in Jenkins)
        ScreenshotUtil.capture(driver, "BeforeClick_alertButton");

        // Try to click. If blocked by overlay/iframe, hide the iframe and retry; fallback to JS click.
        try {
            alertBtn.click();
        } catch (ElementClickInterceptedException ex) {
            // capture screenshot to understand overlay
            ScreenshotUtil.capture(driver, "ClickIntercepted_beforeFix");

            // attempt to hide Google ads iframe(s) that often intercept clicks
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "var frames = document.querySelectorAll('iframe[id^=\"google_ads_iframe_\"]');"
                                + "for(var i=0;i<frames.length;i++){ frames[i].style.display='none'; }");
                // small pause to let layout update
                try { Thread.sleep(400); } catch (InterruptedException ie) { /* ignore */ }
            } catch (Exception jsEx) {
                // ignore: if we cannot remove the iframe via JS, we'll still try JS click fallback
            }

            // try clicking again normally
            try {
                alertBtn.click();
            } catch (Exception ex2) {
                // final fallback: JS click
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", alertBtn);
            }
        }

        // Wait for the alert to appear
        wait.until(ExpectedConditions.alertIsPresent());

        // now read & accept the alert
        Alert simAlert = driver.switchTo().alert();
        String alertText = simAlert.getText();
        System.out.println("The text displayed in the alert is : " + alertText);
        simAlert.accept();

        // optional: take a regular Selenium screenshot after accepting (page-only)
        ScreenshotUtil.capture(driver, "Alert_afterAccept");
    }

    @AfterTest
    public void browserClose() {
        if (driver != null) {
            driver.quit();
        }
    }

}
