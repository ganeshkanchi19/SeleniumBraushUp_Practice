package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
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

public class SearchProductAcrossPages extends BaseTest {
	WebDriver driver;
	WebDriverWait wait;
	JavascriptExecutor js = (JavascriptExecutor) driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	@Test
	public void searchProd() {
		driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/category&path=25_28");
		String targetProduct = "Samsung Galaxy Tab 10.1";

		By productTitles = By.cssSelector(".caption h4 a");
		By nextBtnBy = By.xpath("//ul[@class='pagination']//a[normalize-space(text())='>']");
		By productCard = By.cssSelector("div.product-layout");

		boolean found = false;
		int page = 1;

		while (true) {
			// Wait until product cards appear on the page
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCard));
			System.out.println("\n Searching on Page " + page + "...");

			List<WebElement> titles = driver.findElements(productTitles);

			for (WebElement t : titles) {
				String name = t.getText().trim();

				if (name.equalsIgnoreCase(targetProduct)) {
					System.out.println("Product found on Page " + page + ": " + name);
					ScreenshotUtil.capture(driver, "SearchProduct");
					found = true;
					return; // Exit the test IMMEDIATELY
				}
			}

			// If NEXT button does NOT exist → reached last page
			List<WebElement> nextBtnList = driver.findElements(nextBtnBy);
			if (nextBtnList.isEmpty()) {
				System.out.println("Reached last page. Next button not found.");
				break;
			}
			WebElement nextBtn = nextBtnList.get(0);

			// Scroll into view and click safely
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", nextBtn);

			try {
				nextBtn.click();
			} catch (StaleElementReferenceException e) {
				// Retry fresh lookup if stale
				nextBtn = driver.findElement(nextBtnBy);
				nextBtn.click();
			}
			// Wait for old page to refresh
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCard));

			page++; // go to next page
		}

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
