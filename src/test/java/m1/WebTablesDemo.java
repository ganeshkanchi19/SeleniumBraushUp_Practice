package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class WebTablesDemo extends BaseTest {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeMethod(alwaysRun = true)
	public void init() {
		// get the driver created by BaseTest -> DriverFactory
		driver = getDriver();
		if (driver == null) {
			throw new RuntimeException("Driver is null — BaseTest did not initialize it!");
		}

		// safe post-creation setup (timeouts / window size)
		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		} catch (Exception ignored) {
		}

		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void webTablesTest() {
		driver.get("https://www.letskodeit.com/practice");

		// wait for the table to be present & visible (use locator version)
		By tableLocator = By.xpath("//table[@id='product']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
		WebElement prodTable = driver.findElement(tableLocator);

		// scroll into view and read text
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				prodTable);

		String fullTableText = prodTable.getText();
		System.out.println("Full Table Text:");
		System.out.println(fullTableText);

		ScreenshotUtil.capture(driver, "Capturing_FullTableText");

		// safer extraction using explicit waits for the specific cells
		String courseHeader = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='product']/tbody/tr[1]/th[2]")))
				.getText();
		System.out.println("Extracted header text for course is : " + courseHeader);

		String pythonCourse = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='product']/tbody/tr[3]/td[2]")))
				.getText();
		System.out.println("Extracted specific cell text is : " + pythonCourse);
	}
}
