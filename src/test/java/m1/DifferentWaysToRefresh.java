package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import base.BaseTest;
import utils.ScreenshotUtil;

/**
 * DifferentWaysToRefresh — now uses BaseTest/DriverFactory (no local driver
 * creation) Works safely with parallel execution / CI.
 */
public class DifferentWaysToRefresh extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeMethod(alwaysRun = true)
	public void setup() {

		driver = getDriver(); // get thread-local WebDriver from BaseTest

		if (driver == null) {
			throw new IllegalStateException("Driver is null — BaseTest failed to initialize it.");
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void refreshTestDemo() {
		// 1️⃣ Using driver.navigate().refresh()
		driver.get("https://testautomationpractice.blogspot.com/#");
		driver.navigate().refresh();
		System.out.println("Refreshing the web page using normal refresh() method");
		ScreenshotUtil.capture(driver, "Normal_Refresh_Method");

		// 2️⃣ Using SendKeys → F5
		driver.get("https://www.toolsqa.com");
		driver.findElement(By.xpath("//input[@class='navbar__search--input']")).sendKeys(Keys.F5);
		System.out.println("Refreshing the web page using keyboard Keys F5");
		ScreenshotUtil.capture(driver, "UsingF5Key");

		// 3️⃣ Using get(currentUrl)
		driver.get("https://www.letskodeit.com/practice");
		driver.get(driver.getCurrentUrl());
		System.out.println("Using another command as an argument to it");
		ScreenshotUtil.capture(driver, "UsingAnotherCommandArgument");

		// 4️⃣ Using navigate().to(currentUrl)
		driver.get("https://jqueryui.com/tooltip/");
		driver.navigate().to(driver.getCurrentUrl());
		System.out.println("Navigate().to() is feeding with a page URL and an argument.");
		ScreenshotUtil.capture(driver, "UsingNavigateTo()");

		// 5️⃣ Using ASCII equivalent of F5 → "\uE035"
		driver.get("https://www.toolsqa.com");
		driver.findElement(By.xpath("//input[@class='navbar__search--input']")).sendKeys("\uE035");
		System.out.println("Refreshing using ASCII code via sendKeys()");
		ScreenshotUtil.capture(driver, "PassingASCIICode");
	}

	// ❌ Do NOT quit driver here — BaseTest handles quitting
}
