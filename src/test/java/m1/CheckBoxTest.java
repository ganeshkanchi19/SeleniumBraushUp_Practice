package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.DriverFactory;
import utils.ScreenshotUtil;

public class CheckBoxTest extends BaseTest {
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
	public void verifyCheckBox() {
		log.info("Navigating to letskodeit practice page.");
		driver.get("https://www.letskodeit.com/practice");

		// Elements
		WebElement chkBoxSelected = driver.findElement(By.xpath("//input[@id='bmwcheck']"));
		boolean isSelected = chkBoxSelected.isSelected();

		// performing click operation if element is not selected
		if (!isSelected) {
			log.info("BMW checkbox not selected — clicking it.");
			chkBoxSelected.click();
		}

		// Benz checkbox - displayed check
		log.info("Checking if Benz checkbox is displayed.");
		WebElement chkBoxDisplayed = driver.findElement(By.xpath("//input[@id='benzcheck']"));
		boolean isDisplayed = chkBoxDisplayed.isDisplayed();
		if (isDisplayed) {
			log.info("Benz checkbox displayed — clicking it.");
			chkBoxDisplayed.click();
		}

		// Honda checkbox - enabled check
		log.info("Checking if Honda checkbox is enabled.");
		WebElement chkBoxEnabled = driver.findElement(By.xpath("//input[@id='hondacheck']"));
		boolean isEnabled = chkBoxEnabled.isEnabled();
		if (isEnabled) {
			log.info("Honda checkbox enabled — clicking it.");
			chkBoxEnabled.click();
		}

		// Taking screenshot (using your ScreenshotUtil)
		log.info("Taking screenshot...");
		ScreenshotUtil.capture(driver, "CheckBoxTesting");
	}

	// No teardown here — BaseTest handles quitting/removing the driver.
}
