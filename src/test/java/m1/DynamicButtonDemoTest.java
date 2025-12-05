package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class DynamicButtonDemoTest extends BaseTest {
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
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	@Test
	public void printTextafterClick() {
		driver.get("https://testautomationpractice.blogspot.com/#");
		WebElement dynamicBtn = driver.findElement(By.xpath("//button[@onclick='toggleButton(this)']"));
		wait.until(ExpectedConditions.visibilityOf(dynamicBtn));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", dynamicBtn);
		String beforeClick = dynamicBtn.getText();
		System.out.println("Text before clicking on the dynamic button is : " + beforeClick);
		ScreenshotUtil.capture(driver, "TextBeforeClick");
		dynamicBtn.click();
		// Wait until the button's text changes
		wait.until(ExpectedConditions
				.not(ExpectedConditions.textToBe(By.xpath("//button[@onclick='toggleButton(this)']"), beforeClick)));
		// Get updated text
		String afterClick = dynamicBtn.getText();
		System.out.println("After Click, Button Text: " + afterClick);
		ScreenshotUtil.capture(driver, "TextAfterClick");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}
}
