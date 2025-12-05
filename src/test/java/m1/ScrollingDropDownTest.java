package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class ScrollingDropDownTest extends BaseTest {
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
		wait = new WebDriverWait(driver, Duration.ofSeconds(35));
	}

	@Test
	public void selFrmScrolDropDown() {
		driver.get("https://testautomationpractice.blogspot.com/#");
		WebElement scrdrpdown = driver.findElement(By.xpath("//input[@id='comboBox']"));
		wait.until(ExpectedConditions.visibilityOf(scrdrpdown));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", scrdrpdown);
		scrdrpdown.click();
		ScreenshotUtil.capture(driver, "AfterClicking_OnScrollDropdown");
		// Fetch all options inside scrolling dropdown
		List<WebElement> options = driver.findElements(By.xpath("//div[@id='dropdown']//div[@class='option']"));
		// Loop and scroll until "Item 26" is found
		for (WebElement option : options) {
			String text = option.getText().trim();
			if (text.equals("Item 26")) {
				// Scroll this option into view
				js.executeScript("arguments[0].scrollIntoView(true);", option);
				// Click the option
				option.click();
				System.out.println("Selected: " + text);
				// Wait until the combo's value contains the selected text
				wait.until(ExpectedConditions.attributeContains(By.id("comboBox"), "value", "Item 26"));
				// Wait until the dropdown overlay is gone (so screenshot is clean)
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("dropdown")));
				// Ensure combo is centered in viewport before screenshot
				js.executeScript("arguments[0].scrollIntoView({block:'center'});",
						driver.findElement(By.id("comboBox")));
				break;
			}
		}
		ScreenshotUtil.capture(driver, "ScrollingDropdown_Item26");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
