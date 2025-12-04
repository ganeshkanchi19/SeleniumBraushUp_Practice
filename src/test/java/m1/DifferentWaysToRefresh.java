package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class DifferentWaysToRefresh extends BaseTest {
	WebDriver driver;
	WebDriverWait wait;
	JavascriptExecutor js = (JavascriptExecutor) driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	@Test
	public void refreshTestDemo() {
		// Using refresh() method
		driver.get("https://testautomationpractice.blogspot.com/#");
		driver.navigate().refresh();
		System.out.println("Refreshing the web page using normal refresh() method");
		ScreenshotUtil.capture(driver, "Normal_Refresh_Method");

		// Using sendkeys() method by passing the F5 key
		driver.get("https://www.toolsqa.com");
		driver.findElement(By.xpath("//input[@class='navbar__search--input']")).sendKeys(Keys.F5);
		System.out.println("Refreshing the web page using keyboard Keys F5");
		ScreenshotUtil.capture(driver, "UsingF5Key");

		// Using another command as an argument to it
		driver.get("https://www.letskodeit.com/practice");
		driver.get(driver.getCurrentUrl());
		System.out.println("Using another command as an argument to it");
		ScreenshotUtil.capture(driver, "UsingAnotherCommandArgument");

		// navigate().to() is feeding with a page URL and an argument.
		driver.get("https://jqueryui.com/tooltip/");
		driver.navigate().to(driver.getCurrentUrl());
		System.out.println("Navigate().to() is feeding with a page URL and an argument.");
		ScreenshotUtil.capture(driver, "UsingNavigateTo()");

		// Passing ASCII code in the sendkeys() method
		driver.get("https://www.toolsqa.com");
		driver.findElement(By.xpath("//input[@class='navbar__search--input']")).sendKeys("\uE035");
		System.out.println("Refreshing the web page using sendkeys method and passing ASCII code to it");
		ScreenshotUtil.capture(driver, "PassingASCIICode");

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
