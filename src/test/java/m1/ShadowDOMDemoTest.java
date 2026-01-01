package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.DriverFactory;
import utils.ScreenshotUtil;

public class ShadowDOMDemoTest extends BaseTest {
	private WebDriver driver;
	private WebDriverWait wait;
	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(CheckBoxTest.class);

	@BeforeMethod(alwaysRun = true)
	public void setupTest() {
		// try to get driver from BaseTest (should have been created by BaseTest.setup)
		driver = getDriver();

		// Defensive fallback if BaseTest did not initialize for some reason
		if (driver == null) {
			log.warn(
					"Driver from BaseTest was null in CheckBoxTest.setup — creating a local chrome driver as fallback.");
			// DriverFactory.initDriver("chrome") could also be used
			DriverFactory.initChromeDriver();
			driver = getDriver();
			if (driver == null) {
				throw new IllegalStateException("Failed to initialize driver for test");
			}
		}

		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void FindShadowElement() {
		log.info("Navigating to testautomationpractice website page for finding the shadow DOM element.");
		driver.get("https://testautomationpractice.blogspot.com/");

		// Step 1: Locate shadow host
		WebElement shadowHost = driver.findElement(By.id("shadow_host"));

		// Step 2: Get shadow root
		SearchContext shadowRoot = shadowHost.getShadowRoot();

		// Step 3: Locate element inside shadow DOM
		WebElement mobiles = shadowRoot.findElement(By.cssSelector("span.info"));

		// Waiting for the element to be visible
		wait.until(ExpectedConditions.visibilityOf(mobiles));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				mobiles);

		ScreenshotUtil.capture(driver, "Finding Shadow DOM Element");

		// Step 4: Action
		System.out.println("Extracted text of the shadow element is : " + mobiles.getText());
		log.info("Shadow element found successfully.");
	}
}
