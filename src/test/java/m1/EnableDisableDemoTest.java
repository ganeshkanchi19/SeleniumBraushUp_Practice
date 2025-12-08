package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class EnableDisableDemoTest extends BaseTest {
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
	public void enabledisableCheck() {
		driver.get("https://www.letskodeit.com/practice");
		WebElement enadisbField = driver.findElement(By.xpath("//input[@id='enabled-example-input']"));
		enadisbField.sendKeys("Java");
		String beforedis = enadisbField.getAttribute("value");
		System.out.println("Text before disabling the field is : " + beforedis);
		WebElement disablebtn = driver.findElement(By.xpath("//input[@id='disabled-button']"));
		disablebtn.click();
		if (!(enadisbField.isEnabled())) {
			System.out.println("Enabled/Disabled field is disabled successfully");
		} else {
			System.out.println("Error while disabling the Enabled/Disabled field");
		}
		ScreenshotUtil.capture(driver, "EnableDisable_Check");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		// Click Enable to enable the input again
		WebElement enablebtn = driver.findElement(By.xpath("//input[@id='enabled-button']"));
		wait.until(ExpectedConditions.visibilityOf(enablebtn));
		enablebtn.click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		Actions actions = new Actions(driver);
		if (enadisbField.isEnabled()) {
			// Place caret at the end and type "Programming"
			// Using Actions to move focus and press END before typing ensures append
			// behavior.
			actions.moveToElement(enadisbField).click().sendKeys(Keys.END).perform();
			// now append the text
			enadisbField.sendKeys("Programming");
		} else {
			System.out.println("Error while enabling the Enabled/Disabled field");
		}
		// Read final text and print
		String finalText = enadisbField.getAttribute("value");
		System.out.println("Final text in field is : " + finalText);

		// Optional assertion
		if ("JavaProgramming".equalsIgnoreCase(finalText)) {
			System.out.println("PASS: Final text matches expected JavaProgramming");
		} else {
			System.out.println("FAIL: Final text did not match. Actual: " + finalText);
		}
		// Final screenshot
		ScreenshotUtil.capture(driver, "EnableDisable_Final");

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
