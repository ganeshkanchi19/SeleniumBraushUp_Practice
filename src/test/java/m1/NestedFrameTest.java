package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;
import utils.ScrollUtil;

public class NestedFrameTest extends BaseTest {
	WebDriver driver;
	WebDriverWait wait;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void nestedFrameCheck() throws InterruptedException {
		driver.get("https://demoqa.com/nestedframes");

		// top-level frames count
		int totalFrames = driver.findElements(By.tagName("iframe")).size();
		System.out.println("Total number of frames are : " + totalFrames);

		// wait for parent frame element and switch to it using
		// frameToBeAvailableAndSwitchToIt
		// This will wait until the frame is available and then switch context into it.
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("frame1")));
		System.out.println("Switched to parent frame (frame1).");

		// Now we are inside the parent frame context.
		// Wait until the child iframe is present inside this parent frame.
		boolean childPresent = wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver drv) {
				List<WebElement> frames = drv.findElements(By.tagName("iframe"));
				return frames != null && frames.size() > 0;
			}
		});

		if (!childPresent) {
			throw new RuntimeException("Child iframe not found inside parent frame within timeout");
		}

		// find the child iframe element (first one) and switch into it
		
		WebElement childFrame = driver.findElement(By.tagName("iframe"));
		driver.switchTo().frame(childFrame);
		System.out.println("Switched to child frame inside frame1.");

		// Wait for the <p> element inside child frame and scroll to it
		
		WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("p")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				p);
		Thread.sleep(250);

		// take screenshot and print text
		ScreenshotUtil.capture(driver, "Child_Frame");
		String childText = p.getText();
		System.out.println("Child Frame Text: " + childText);

		// switch one level up to parent frame
		driver.switchTo().parentFrame();
		System.out.println("Switched back to parent frame (frame1).");

		// get the body text inside parent frame
		WebElement frame1Body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", frame1Body);
		Thread.sleep(250);

		ScreenshotUtil.capture(driver, "Parent_Frame");
		String parentText = frame1Body.getText().trim();
		System.out.println("Text inside the parent frame is  : " + parentText);

		// back to top-level DOM
		driver.switchTo().defaultContent();

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
