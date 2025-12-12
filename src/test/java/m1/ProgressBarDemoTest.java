package m1;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class ProgressBarDemoTest extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeMethod(alwaysRun = true)
	public void setup() {

		driver = getDriver();
		if (driver == null)
			throw new IllegalStateException("Driver is NULL — BaseTest failed to initialize WebDriver.");

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void progressBarCheck() {

		driver.get("https://www.tutorialspoint.com/selenium/practice/progress-bar.php");

		WebElement startBtn = driver.findElement(By.id("startProgressTimer"));
		startBtn.click();

		By bar = By.cssSelector("div.progress-bar");

		AtomicBoolean printedDanger = new AtomicBoolean(false);

		// Wait until the progress bar becomes SUCCESS (green)
		wait.until(d -> {

			WebElement progress = d.findElement(bar);
			String cls = progress.getAttribute("class");

			// Print interim "danger" state (orange) only once
			if (cls.contains("progress-bar-danger") && !printedDanger.get()) {
				System.out.println("Progress Status → " + cls);
				printedDanger.set(true);
			}

			// When success class appears, stop waiting
			if (cls.contains("progress-bar-success")) {
				System.out.println("Progress Status → " + cls);
				return true;
			}

			return false;
		});

		// Final verification
		String finalClass = driver.findElement(bar).getAttribute("class");

		if (finalClass.contains("progress-bar-success")) {
			System.out.println("PASS — Progress bar reached completion!");
		} else {
			System.out.println("FAIL — Progress bar did not complete.");
		}

		ScreenshotUtil.capture(driver, "ProgressBar");
	}
}
