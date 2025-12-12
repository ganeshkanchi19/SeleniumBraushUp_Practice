package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class SearchProductAcrossPages extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is NULL — BaseTest failed to initialize WebDriver.");
		}

		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		js = (JavascriptExecutor) driver;
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
					ScreenshotUtil.capture(driver, "SearchProduct_Page" + page);
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
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", nextBtn);

			try {
				nextBtn.click();
			} catch (StaleElementReferenceException e) {
				// Retry fresh lookup if stale
				nextBtn = driver.findElement(nextBtnBy);
				nextBtn.click();
			}

			// Wait for products on the new page to load
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCard));

			page++; // go to next page
		}

		if (!found) {
			System.out.println("Product '" + targetProduct + "' not found across pages.");
			ScreenshotUtil.capture(driver, "SearchProduct_NotFound");
		}
	}

}
