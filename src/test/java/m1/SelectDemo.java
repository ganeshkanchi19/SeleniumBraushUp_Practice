package m1;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class SelectDemo extends BaseTest {

	private WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is NULL — BaseTest failed to initialize WebDriver.");
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@Test
	public void selectTestCheck() throws IOException {

		driver.get("https://www.letskodeit.com/practice");

		WebElement carSel = driver.findElement(By.id("carselect"));
		Select s = new Select(carSel);

		// Select Benz
		s.selectByVisibleText("Benz");
		String selectedText = s.getFirstSelectedOption().getText();
		System.out.println("Selected value is: " + selectedText);

		// List all dropdown options
		List<WebElement> allOptions = s.getOptions();
		for (WebElement option : allOptions) {
			System.out.println("Available dropdown option: " + option.getText());
		}

		// Capture screenshot
		ScreenshotUtil.capture(driver, "Select_From_Dropdown");
	}
}