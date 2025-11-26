package m1;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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

public class AlertDemoTest extends BaseTest {
	WebDriver driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test
	public void alertcheck() throws IOException {
		driver.get("https://demoqa.com/alerts");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		// click the button that opens the JS alert
		driver.findElement(By.id("alertButton")).click();

		// wait for the alert to appear (explicit wait gives more reliability)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.alertIsPresent());

		// now read & accept the alert
		Alert simAlert = driver.switchTo().alert();
		String alertText = simAlert.getText();
		System.out.println("The text displayed in the alert is : " + alertText);
		simAlert.accept();

		// optional: take a regular Selenium screenshot after accepting (page-only)
		ScreenshotUtil.capture(driver, "Alert_afterAccept");

	}

	@AfterTest
	public void browserClose() {
		driver.quit();
	}

}
