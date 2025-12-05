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

public class AutoSuggestionDemoTest extends BaseTest {
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
	public void autoSuggestionCheck() throws InterruptedException {
		// Enter the url or open the Webpage
		driver.get("https://www.google.com");

		// 1) Type query
		WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
		searchInput.clear();
		searchInput.sendKeys("lambdatest");
		Thread.sleep(5000);
		ScreenshotUtil.capture(driver, "AutoSuggestion_BeforeSelecting");
		// Find all the auto-suggestion and print the count of auto suggestions
		List<WebElement> autoSugg = driver.findElements(By.xpath("//span[contains(text(),'lambdatest')]"));
		int count = autoSugg.size();
		System.out.println("The auto suggestion count is : " + count);
		// Print all the text of the auto suggestions in the console
		for (int i = 0; i < count; i++) {
			String autoSuggText = autoSugg.get(i).getText();
			System.out.println(autoSuggText);
		}
		// Select the first auto suggestion
		autoSugg.get(0).click();
		ScreenshotUtil.capture(driver, "AutoSuggestion_AfterSelecting");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
