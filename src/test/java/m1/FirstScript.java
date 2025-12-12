package m1;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class FirstScript extends BaseTest {
	private WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		// get driver created by BaseTest (thread-safe)
		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is null — BaseTest failed to initialize it.");
		}

		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		} catch (Exception ignored) {
		}

		System.out.println("[FirstScript] Browser initialized successfully.");
	}

	@Test
	public void printCurrentURL() {
		driver.get("https://www.letskodeit.com/practice");

		String currentURL = driver.getCurrentUrl();
		String title = driver.getTitle();

		ScreenshotUtil.capture(driver, "PrintingCurrentUrl");

		System.out.println("Current URL of the page is : " + currentURL);
		System.out.println("Title of the webpage is : " + title);
	}

	// ❌ Do NOT quit driver here — BaseTest handles teardown automatically
}
