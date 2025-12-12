package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class KeyboardActionsDemoTest extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeMethod(alwaysRun = true)
	public void setup() {

		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is NULL — BaseTest failed to initialize it.");
		}

		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		} catch (Exception ignored) {
		}

		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void keyboardEventsCheck() {

		driver.get("https://demoqa.com/text-box");

		Actions act = new Actions(driver);

		// Enter Full Name
		WebElement fullname = driver.findElement(By.id("userName"));
		fullname.sendKeys("Ganesh Kanchi" + Keys.TAB);

		// Enter Email
		WebElement email = driver.findElement(By.id("userEmail"));
		email.sendKeys("ganukanchi@gmail.com" + Keys.TAB);

		// Current Address
		WebElement currentAddress = driver.findElement(By.id("currentAddress"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", currentAddress);
		currentAddress.sendKeys("20 Westin Building London");

		String currAddressValue = currentAddress.getAttribute("value");
		System.out.println("Current Address is : " + currAddressValue);

		// CTRL + A → select all
		act.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();

		// CTRL + C → copy
		act.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();

		// TAB → move to Permanent Address
		act.sendKeys(Keys.TAB).perform();

		// CTRL + V → paste
		act.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();

		// Verification
		WebElement permanentAddress = driver.findElement(By.id("permanentAddress"));

		if (currentAddress.getAttribute("value").equals(permanentAddress.getAttribute("value"))) {
			System.out.println("PASS: Current address copied successfully into permanent address field");
		} else {
			System.out.println("FAIL: Address copy unsuccessful");
		}

		ScreenshotUtil.capture(driver, "Keyboard_Actions_Demo");
	}

	// No @AfterTest → BaseTest handles tearDown()
}
