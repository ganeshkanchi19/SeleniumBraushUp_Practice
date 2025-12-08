package m1;

import java.time.Duration;

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

public class HideShowDemoTest extends BaseTest {
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
	public void hideshowCheckTest() {
		driver.get("https://www.letskodeit.com/practice");
		WebElement hideshowBox = driver.findElement(By.xpath("//input[@id='displayed-text']"));
		hideshowBox.sendKeys("Selenium");
		WebElement hideBtn = driver.findElement(By.xpath("//input[@id='hide-textbox']"));
		WebElement showBtn = driver.findElement(By.xpath("//input[@id='show-textbox']"));
		hideBtn.click();
		ScreenshotUtil.capture(driver, "AfterClick_HideButton");
		showBtn.click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		if (hideshowBox.isDisplayed()) {
			System.out.println("Hide/Show test case is passed successfully");
		} else {
			System.out.println("Hide/Show test case is failed");
		}
		ScreenshotUtil.capture(driver, "AfterClick_ShowButton");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}
}
