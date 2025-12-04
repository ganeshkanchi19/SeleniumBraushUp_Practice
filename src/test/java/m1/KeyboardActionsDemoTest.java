package m1;

import static org.testng.Assert.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class KeyboardActionsDemoTest extends BaseTest {
	WebDriver driver;
	WebDriverWait wait;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	@Test
	public void keyboardEventsCheck() {
		// getting the url
		driver.get("https://demoqa.com/text-box");

		// Creating the object of action class
		Actions act = new Actions(driver);

		// Entering the full name
		WebElement fullname = driver.findElement(By.xpath("//input[@id='userName']"));
		fullname.sendKeys("Ganesh Kanchi" + Keys.TAB);

		// Entering the email
		WebElement email = driver.findElement(By.xpath("//input[@id='userEmail']"));
		email.sendKeys("ganukanchi@gmail.com" + Keys.TAB);

		// Enter the Current Address
		WebElement currentAddress = driver.findElement(By.xpath("//textarea[@id='currentAddress']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				currentAddress);
		currentAddress.sendKeys("20 Westin Building London");
		// Printing the current address
		String currAdress = currentAddress.getAttribute("value");
		System.out.println("Current address is : " + currAdress);

		// Select the Current Address using CTRL + A
		act.keyDown(Keys.CONTROL);
		act.sendKeys("a");
		act.keyUp(Keys.CONTROL);
		act.build().perform();

		// Copy the Current Address using CTRL + C
		act.keyDown(Keys.CONTROL);
		act.sendKeys("c");
		act.keyUp(Keys.CONTROL);
		act.build().perform();

		// Press the TAB Key to Switch Focus to Permanent Address
		act.sendKeys(Keys.TAB);
		act.build().perform();

		// Paste the Current Address using CTRL + V
		act.keyDown(Keys.CONTROL);
		act.sendKeys("v");
		act.keyUp(Keys.CONTROL);
		act.build().perform();

		// Compare Text of current Address and Permanent Address
		WebElement permanentAddress = driver.findElement(By.id("permanentAddress"));
		if (currentAddress.getAttribute("value").equals(permanentAddress.getAttribute("value"))) {
			System.out.println("Current address is copied successfully into permanent address field");
		} else {
			System.out.println("Failed while copying the address into  permanent address field");
		}

		// Taking the screenshots
		ScreenshotUtil.capture(driver, "Keyboard_Actions_Demo");

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
