package m1;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class ProgressBarDemoTest extends BaseTest {
	WebDriver driver;
	WebDriverWait wait;
	JavascriptExecutor js;
	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(CheckBoxTest.class);

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	@Test
	public void progressBarCheck() {
		driver.get("https://www.tutorialspoint.com/selenium/practice/progress-bar.php");
		WebElement startBtn = driver.findElement(By.xpath("//button[@id='startProgressTimer']"));
		startBtn.click();
//		WebElement bar = driver.findElement(By.xpath("//div[@class='progress-bar progress-bar-success']"));
//		String progressComp = bar.getAttribute("style");
//		System.out.println("Progess comepletion is : " + progressComp);
//		// Extract only the number (e.g., 100.46)
//		String numeric = progressComp.replace("width:", "").replace("%", "").replace(";", "").trim();
//		// Convert to double
//		double comValue = Double.parseDouble(numeric);
//		System.out.println("Double value = " + comValue);
//		if (comValue >= 100) {
//			System.out.println("Progress bar is reached till the end successfully");
//		} else {
//			System.out.println("Progress bar has stopped due to some reasons");
//		}

		By bar = By.cssSelector("div.progress-bar");

		wait = new WebDriverWait(driver, Duration.ofSeconds(40));

		AtomicBoolean printedDanger = new AtomicBoolean(false);
		// ✅ Wait until class changes to "progress-bar progress-bar-success"

		wait.until(d -> {
			String classValue = d.findElement(bar).getAttribute("class");
			// Print danger only once
			if (classValue.contains("progress-bar-danger") && !printedDanger.get()) {
				System.out.println("Current class = " + classValue);
				printedDanger.set(true);
			}

			// Print success once and stop waiting
			if (classValue.contains("progress-bar-success")) {
				System.out.println("Current class = " + classValue);
				return true;
			}

			return false;
		});

		// Final class check
		WebElement progressBar = driver.findElement(bar);
		String finalClass = progressBar.getAttribute("class");

		if (finalClass.contains("progress-bar-success")) {
			System.out.println("PASS — Progress bar reached completion!");
		} else {
			System.out.println("FAIL — Progress bar did not complete.");
		}
		ScreenshotUtil.capture(driver, "ProgressBar");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
