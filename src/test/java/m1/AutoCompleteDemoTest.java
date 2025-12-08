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

public class AutoCompleteDemoTest extends BaseTest {
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
	public void autocompleteCheck() {
		driver.get("https://jqueryui.com/autocomplete/");
		driver.switchTo().frame(0);
		// Wait for suggestions to show
		WebElement tagBox = driver.findElement(By.xpath("//input[@id='tags']"));
		tagBox.sendKeys("ja");
		log.info("Waiting for suggetsions to appear");
		List<WebElement> suggetsions = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("ul.ui-menu li div")));

		// Select "JavaScript"
		for (WebElement option : suggetsions) {
			if (option.getText().equals("JavaScript")) {
				option.click();
				log.info("Selected the targeted option successfully");
				break;
			}
		}
		ScreenshotUtil.capture(driver, "AutoComplete");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
