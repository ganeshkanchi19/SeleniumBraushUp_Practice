package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class NestedFrameTest extends BaseTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver is NULL — BaseTest did not initialize it.");
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void nestedFrameCheck() {

        driver.get("https://demoqa.com/nestedframes");

        // Count total frames in top-level DOM
        int totalFrames = driver.findElements(By.tagName("iframe")).size();
        System.out.println("Total number of frames on page: " + totalFrames);

        // Switch to parent frame using a reliable condition
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("frame1")));
        System.out.println("Switched to Parent Frame: frame1");

        // Check if child iframe exists inside parent
        boolean childPresent = wait.until((ExpectedCondition<Boolean>) drv -> {
            List<WebElement> childFrames = drv.findElements(By.tagName("iframe"));
            return childFrames != null && !childFrames.isEmpty();
        });

        if (!childPresent) {
            throw new RuntimeException("No child iframe found inside parent frame!");
        }

        // Switch into the child frame
        WebElement childFrame = driver.findElement(By.tagName("iframe"));
        driver.switchTo().frame(childFrame);
        System.out.println("Switched to Child Frame inside Parent Frame");

        // Locate <p> inside child frame
        WebElement childTextElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("p")));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", childTextElem);

        ScreenshotUtil.capture(driver, "NestedFrame_ChildText");

        String childText = childTextElem.getText();
        System.out.println("Child Frame Text: " + childText);

        // Return to Parent Frame
        driver.switchTo().parentFrame();
        System.out.println("Returned to Parent Frame");

        // Locate parent frame body text
        WebElement parentBody = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", parentBody);

        ScreenshotUtil.capture(driver, "NestedFrame_ParentText");

        String parentText = parentBody.getText().trim();
        System.out.println("Parent Frame Text: " + parentText);

        // Switch back to main page
        driver.switchTo().defaultContent();
        System.out.println("Returned to Main Page (Default Content)");
    }
}
