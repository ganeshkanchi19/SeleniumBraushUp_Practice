package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class ToggleButtonDemoTest extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeMethod(alwaysRun = true)
	public void setupTest() {

		// ✅ Get driver initialized by BaseTest
		driver = getDriver();

		js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	@Test
	public void toggleMethodsCheck() {

		driver.get("file:///C:/Users/DELL/OneDrive/Desktop/toggleDemo.html");

		WebElement ageInput = driver.findElement(By.id("ageInput"));
		WebElement toggleBtn = driver.findElement(By.xpath("//span[@class='slider']"));
		WebElement errorMsg = driver.findElement(By.id("message"));

		// -------------------------------------
		// BEFORE ENABLING TOGGLE
		// -------------------------------------
		System.out.println("Step 1: Checking toggle default state");
		ScreenshotUtil.capture(driver, "AgeConsent_BeforeToggle");

		// Toggle ON for the first time (age empty → error expected)
		toggleBtn.click();

		if (errorMsg.isDisplayed()) {
			System.out.println("Displayed: User must be 18+ to enable this setting");
		} else {
			System.out.println("Unexpected: Error message NOT shown");
		}

		ScreenshotUtil.capture(driver, "AgeConsent_ErrorDisplayed");

		// -------------------------------------
		// ENTER VALID AGE: 20
		// -------------------------------------
		ageInput.clear();
		ageInput.sendKeys("20");

		// Click toggle again to re-validate age
		toggleBtn.click();

		WebElement statusMsg =
				driver.findElement(By.xpath("//div[@id='status' and contains(text(),'Current Status: ON')]"));

		String curStatus = statusMsg.getText();
		System.out.println("Status after entering valid age: " + curStatus);

		ScreenshotUtil.capture(driver, "AgeConsent_ValidAge_ON");
	}
}
