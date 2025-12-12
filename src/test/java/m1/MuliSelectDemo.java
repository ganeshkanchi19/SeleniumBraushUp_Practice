package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class MuliSelectDemo extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		driver = getDriver();

		if (driver == null) {
			throw new IllegalStateException("Driver is NULL — BaseTest failed to initialize it.");
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	@Test
	public void multiSelCheck() {

		driver.get("https://www.letskodeit.com/practice");

		// Locate the multi-select dropdown
		WebElement multiSelect = driver.findElement(By.id("multiple-select-example"));

		// Select multiple values
		Select sel = new Select(multiSelect);

		sel.selectByIndex(1); // Apple
		sel.selectByVisibleText("Peach"); // Peach

		// Capture screenshot
		ScreenshotUtil.capture(driver, "MultiSelect_Check");

		// Print selected options
		List<WebElement> selectedOptions = sel.getAllSelectedOptions();

		System.out.println("\nSelected values:");
		for (WebElement option : selectedOptions) {
			System.out.println("✓ " + option.getText());
		}
	}

	// ❌ Do NOT close driver here — BaseTest handles teardown automatically
}
