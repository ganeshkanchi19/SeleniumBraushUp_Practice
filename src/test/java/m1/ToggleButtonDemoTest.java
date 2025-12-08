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

public class ToggleButtonDemoTest extends BaseTest {
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
	public void toggleMethodsCheck() {
		// Navigating to dummy age verification URL for toggle button check
		driver.get("file:///C:/Users/DELL/OneDrive/Desktop/toggleDemo.html");
		WebElement age = driver.findElement(By.xpath("//input[@id='ageInput']"));
		WebElement consenToggle = driver.findElement(By.xpath("//span[@class='slider']"));
		WebElement AgeErrMsg = driver.findElement(By.xpath("//div[@id='message']"));
	
		// Checking whether the Allow 18+ Content: toggle is on or off
		if (!(consenToggle.isEnabled())) {
			System.out.println("Allow 18+ Consent toggle button is not enabled");
		} else {
			System.out.println("Allow 18+ Consent toggle button is already enabled by someone");
		}
		ScreenshotUtil.capture(driver, "AgeConsentButton_BeforeEnable");

		// Enabling the consent button
		consenToggle.click();
		if (AgeErrMsg.isDisplayed()) {
			System.out.println("Candidate age should be 18 or older to enable this setting");
		} else {
			System.out.println("Candidate has verified the age successfully");
		}
		ScreenshotUtil.capture(driver, "AgeConsent_ErrorMessage");

		// Clearing the age field
		age.clear();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		// Entering the valid age field>=18
		age.sendKeys("20");

		// Again clicking on the consent toggle button to check the age verification
		consenToggle.click();
		WebElement curStatus = driver.findElement(By.xpath("//div[@id='status' and text()='Current Status: ON']"));
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		String currentStatus = curStatus.getText();
		System.out.println("Current status is : " + currentStatus);
		if ((consenToggle.isEnabled() && !(AgeErrMsg.isDisplayed()))) {
			System.out.println(currentStatus);
		} else {
			System.out.println("Current status is OFF");
		}
		ScreenshotUtil.capture(driver, "ValidAge_ConsentON");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}
}
