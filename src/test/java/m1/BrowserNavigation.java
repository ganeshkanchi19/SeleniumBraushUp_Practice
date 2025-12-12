package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod; // optional if you want per-method cleanup; removed here
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

/**
 * BrowserNavigation test rewritten to use BaseTest/DriverFactory.
 */
public class BrowserNavigation extends BaseTest {
	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is null — BaseTest did not initialize it!");
		}

		// safe maximize (remote environments may not support maximize)
		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}

		// explicit wait used throughout
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void browserNavigateCheck() {
		driver.get("https://demoqa.com/text-box");

		By fullNameBy = By.xpath("//input[@id='userName']");
		By emailBy = By.xpath("//input[@id='userEmail']");
		By curAddrBy = By.xpath("//textarea[@id='currentAddress']");
		By permAddrBy = By.xpath("//textarea[@id='permanentAddress']");
		By submitBtnBy = By.xpath("//button[@id='submit']");
		By outputNameBy = By.id("name"); // <p id="name">Name: ...</p>
		By pageTitleBy = By.xpath("//h1[text()='Text Box']");

		// wait for title visible to ensure page loaded
		wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitleBy));

		WebElement fullname = wait.until(ExpectedConditions.elementToBeClickable(fullNameBy));
		WebElement email = driver.findElement(emailBy);
		WebElement curAddr = driver.findElement(curAddrBy);
		WebElement permAddr = driver.findElement(permAddrBy);
		WebElement submitBtn = driver.findElement(submitBtnBy);

		// scroll submit button roughly to center, then shift a bit to avoid overlays
		try {
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", submitBtn);
			js.executeScript("window.scrollBy(0, -120);"); // move up to avoid footer overlay
		} catch (Exception ignored) {
		}

		// Fill fields
		fullname.clear();
		fullname.sendKeys("Ganesh Kanchi");

		email.clear();
		email.sendKeys("ganukanchi2018@gmail.com");

		curAddr.clear();
		curAddr.sendKeys("Bachelors PG, Hinjewadi");

		permAddr.clear();
		permAddr.sendKeys("Flat No 809, 2nd floor, Near BOSS PG, Bachelors PG, Hinjewadi");

		// Try to click the submit button; if intercepted use JS click as fallback
		try {
			wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
			submitBtn.click();
		} catch (ElementClickInterceptedException ex) {
			System.out.println("Normal click intercepted; clicking with JS instead.");
			try {
				js.executeScript("arguments[0].click();", submitBtn);
			} catch (Exception jsEx) {
				System.err.println("JS click also failed: " + jsEx.getMessage());
				throw jsEx;
			}
		} catch (Exception ex) {
			// Last-chance attempt: JS click
			try {
				js.executeScript("arguments[0].click();", submitBtn);
			} catch (Exception jsEx) {
				System.err.println("Failed to click submit button: " + jsEx.getMessage());
				throw new RuntimeException(jsEx);
			}
		}

		// Wait for the output block to be visible and read the text
		WebElement nameOutput = wait.until(ExpectedConditions.visibilityOfElementLocated(outputNameBy));
		String rawNameText = nameOutput.getText(); // e.g. "Name:Ganesh Kanchi"
		System.out.println("Raw displayed output: " + rawNameText);

		// Extract name after "Name:" prefix
		String actualName = rawNameText;
		if (rawNameText != null && rawNameText.contains(":")) {
			actualName = rawNameText.substring(rawNameText.indexOf(":") + 1).trim();
		}

		System.out.println("Submitted Full Name is : " + actualName);

		String expectedName = "Ganesh Kanchi";
		if (expectedName.equals(actualName)) {
			System.out.println("Test case is passed");
		} else {
			System.out.println("Test case is failed - expected: '" + expectedName + "' actual: '" + actualName + "'");
		}

		// capture screenshot for debugging
		ScreenshotUtil.capture(driver, "BrowserNavigation_AfterSubmit");
	}

	// No @AfterMethod here: BaseTest handles driver.quit() in tearDown
}
