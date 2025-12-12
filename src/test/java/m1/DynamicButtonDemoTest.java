package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import base.BaseTest;
import utils.ScreenshotUtil;

/**
 * DynamicButtonDemoTest — refactored to use BaseTest/DriverFactory-provided
 * WebDriver. Do not create local ChromeDriver instances here; BaseTest is
 * responsible for lifecycle.
 */
public class DynamicButtonDemoTest extends BaseTest {
	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;
	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(DynamicButtonDemoTest.class);

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		// obtain driver provided by BaseTest
		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is null — make sure BaseTest initializes it before tests run.");
		}

		// safe maximize / timeouts
		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		} catch (Exception ignored) {
		}

		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		js = (JavascriptExecutor) driver;
		log.info("Obtained driver from BaseTest/DriverFactory");
	}

	@Test
	public void printTextafterClick() {
		driver.get("https://testautomationpractice.blogspot.com/#");

		By dynamicBtnLocator = By.xpath("//button[@onclick='toggleButton(this)']");
		WebElement dynamicBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicBtnLocator));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", dynamicBtn);

		String beforeClick = dynamicBtn.getText();
		System.out.println("Text before clicking on the dynamic button is : " + beforeClick);
		ScreenshotUtil.capture(driver, "TextBeforeClick");

		// click and wait until the text changes
		dynamicBtn.click();

		// wait until the text is different than beforeClick
		wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(dynamicBtnLocator, beforeClick)));

		// re-find element (stale element risk) and read updated text
		dynamicBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicBtnLocator));
		String afterClick = dynamicBtn.getText();
		System.out.println("After Click, Button Text: " + afterClick);
		ScreenshotUtil.capture(driver, "TextAfterClick");
	}

	@AfterMethod(alwaysRun = true)
	public void cleanup() {
		// Do not quit the driver here; BaseTest.removeDriver() will be called by
		// BaseTest.@AfterMethod
		// Keep this method for symmetry / future cleanup if needed.
	}
}
