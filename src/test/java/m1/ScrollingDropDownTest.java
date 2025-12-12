package m1;

import java.time.Duration;
import java.util.List;

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

public class ScrollingDropDownTest extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeMethod(alwaysRun = true)
	public void setup() {

		driver = getDriver();
		if (driver == null)
			throw new IllegalStateException("Driver is NULL — BaseTest failed to initialize WebDriver.");

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		wait = new WebDriverWait(driver, Duration.ofSeconds(35));
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void selFrmScrolDropDown() {

		driver.get("https://testautomationpractice.blogspot.com/#");

		WebElement scrdrpdown = driver.findElement(By.id("comboBox"));
		wait.until(ExpectedConditions.visibilityOf(scrdrpdown));

		js.executeScript("arguments[0].scrollIntoView({block:'center'});", scrdrpdown);
		scrdrpdown.click();

		ScreenshotUtil.capture(driver, "AfterClicking_OnScrollDropdown");

		// Fetch all scrollable options
		List<WebElement> options = driver.findElements(By.xpath("//div[@id='dropdown']//div[@class='option']"));

		for (WebElement option : options) {

			String text = option.getText().trim();

			if (text.equals("Item 26")) {

				js.executeScript("arguments[0].scrollIntoView(true);", option);
				option.click();
				System.out.println("Selected: " + text);

				// Wait until input shows selected value
				wait.until(ExpectedConditions.attributeContains(By.id("comboBox"), "value", "Item 26"));

				// Wait until dropdown overlay hides
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("dropdown")));

				// Bring selected comboBox back to center
				js.executeScript("arguments[0].scrollIntoView({block:'center'});",
						driver.findElement(By.id("comboBox")));
				break;
			}
		}

		ScreenshotUtil.capture(driver, "ScrollingDropdown_Item26");
	}
}
