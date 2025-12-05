package m1;

import java.time.Duration;
import java.util.List;

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

public class DymamicWebTable extends BaseTest {
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
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	@Test
	public void extractCellValue() {
		driver.get("https://testautomationpractice.blogspot.com/#");
		// 1. Find all column headers
		List<WebElement> headers = driver.findElements(By.xpath("//table[@id='taskTable']//th"));
		int memoryColIndex = -1;

		// 2. Loop through headers to find the index of "Memory (MB)"
		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i).getText().trim().equalsIgnoreCase("Memory (MB)")) {
				memoryColIndex = i + 1;
				break;

			}
		}

		if (memoryColIndex == -1) {
			throw new RuntimeException("Memory (MB) column not found!");
		}

		WebElement memoryCell = driver.findElement(
				By.xpath("//table[@id='taskTable']//tr[td[1]='Internet Explorer']/td[" + memoryColIndex + "]"));
		wait.until(ExpectedConditions.visibilityOf(memoryCell));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", memoryCell);
		String memoryVal = memoryCell.getText();
		System.out.println("Memory(MB) for Internet Explorer: " + memoryVal);
		ScreenshotUtil.capture(driver, "ExtractCellValue_DynamicWebTable");

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
