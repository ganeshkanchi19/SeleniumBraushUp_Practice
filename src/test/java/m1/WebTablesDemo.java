package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class WebTablesDemo extends BaseTest{
	WebDriver driver;
	WebDriverWait wait;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void webTablesTest() {
		driver.get("https://www.letskodeit.com/practice");
		WebElement prodTable = driver.findElement(By.xpath("//table[@id='product']"));
		wait.until(ExpectedConditions.visibilityOf(prodTable));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				prodTable);
		String fullTableText = prodTable.getText();
		// Printing full table text
		System.out.println("Full Table Text:");
		System.out.println(fullTableText);
		ScreenshotUtil.capture(driver, "Capturing_FullTableText");
		String courseText = driver.findElement(By.xpath("//table[@id='product']/tbody/tr[1]/th[2]")).getText();
		System.out.println("Extracted text for course is : " + courseText);
		String pythoncourse = driver.findElement(By.xpath("//table[@id='product']/tbody/tr[3]/td[2]")).getText();
		System.out.println("Extracted specific cell text is : " + pythoncourse);

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}
}
