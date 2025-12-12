package m1;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class PaginationDemoTest extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeMethod(alwaysRun = true)
	public void setup() {

		driver = getDriver();
		if (driver == null)
			throw new IllegalStateException("Driver is NULL — BaseTest failed to initialize WebDriver.");

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));

		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		js = (JavascriptExecutor) driver;
	}

	// ---------------------------------------------------------
	// 🔹 Test 1 — Pagination on LambdaTest Playground
	// ---------------------------------------------------------
	@Test
	public void verifyPaginationAndPrintProducts() {

		driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/category&path=25_28");

		By productCard = By.cssSelector(".product-layout");
		By productTitle = By.cssSelector(".caption h4 a");
		By productPrice = By.cssSelector(".price");

		By nextBtnBy = By.xpath("//ul[contains(@class,'pagination')]//a[normalize-space(text())='>']");

		wait.until(ExpectedConditions.visibilityOfElementLocated(productCard));

		int page = 1;
		int maxPages = 5;

		while (page <= maxPages) {

			List<WebElement> products = driver.findElements(productCard);
			System.out.println("=== Page " + page + " — found " + products.size() + " product(s) ===");

			for (WebElement card : products) {
				String title = "";
				String price = "";

				try {
					title = card.findElement(productTitle).getText().trim();
				} catch (Exception ignored) {
				}

				try {
					price = card.findElement(productPrice).getText().trim();
				} catch (Exception ignored) {
				}

				System.out.println("Product → " + title + (price.isEmpty() ? "" : " | Price: " + price));
			}

			// Stop at last page
			if (page == maxPages)
				break;

			// Navigate to NEXT page
			WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextBtnBy));

			WebElement firstBefore = products.isEmpty() ? null : products.get(0);

			try {
				nextBtn.click();
			} catch (ElementClickInterceptedException e) {
				js.executeScript("arguments[0].click();", nextBtn);
			}

			if (firstBefore != null)
				wait.until(ExpectedConditions.stalenessOf(firstBefore));

			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCard));

			page++;
		}

		System.out.println("Pagination Completed!");
		System.out.println("Last Page URL → " + driver.getCurrentUrl());

		ScreenshotUtil.capture(driver, "PaginationDemo");
	}

	// ---------------------------------------------------------
	// 🔹 Test 2 — Pagination Web Table on TestAutomationBlogspot
	// ---------------------------------------------------------
	@Test
	public void webTableValueSel() {

		driver.get("https://testautomationpractice.blogspot.com/#");

		String target = "Portable Charger";

		By rowsLocator = By.xpath("//h2[normalize-space()='Pagination Web Table']/following::table[1]//tbody/tr");

		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowsLocator));

		boolean found = false;

		for (int page = 1; page <= 4; page++) {

			System.out.println("Searching Page: " + page);

			List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowsLocator));

			for (WebElement row : rows) {

				String nameText = "";
				try {
					nameText = row.findElement(By.xpath("./td[2]")).getText().replace('\u00A0', ' ').trim();
				} catch (Exception ignored) {
				}

				if (target.equalsIgnoreCase(nameText)) {

					System.out.println("FOUND → '" + target + "' on Page " + page);

					WebElement checkbox = row.findElement(By.xpath(".//td[4]//input[@type='checkbox']"));

					js.executeScript("arguments[0].scrollIntoView({block:'center'});", checkbox);

					try {
						checkbox.click();
					} catch (ElementClickInterceptedException e) {
						js.executeScript("arguments[0].click();", checkbox);
					}

					ScreenshotUtil.capture(driver, "Pagination_Table_Found");
					found = true;
					break;
				}
			}

			if (found)
				break;

			// Click next page link
			if (page < 4) {

				int nextPage = page + 1;
				By nextPageBy = By
						.xpath("//ul[contains(@class,'pagination')]//a[normalize-space()='" + nextPage + "']");

				WebElement firstBefore = driver.findElements(rowsLocator).get(0);

				WebElement pageLink = wait.until(ExpectedConditions.elementToBeClickable(nextPageBy));

				js.executeScript("arguments[0].scrollIntoView({block:'center'});", pageLink);

				try {
					pageLink.click();
				} catch (ElementClickInterceptedException e) {
					js.executeScript("arguments[0].click();", pageLink);
				}

				wait.until(ExpectedConditions.stalenessOf(firstBefore));
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowsLocator));
			}
		}

		if (!found) {
			System.out.println("Target value NOT FOUND in the paginated table!");
			ScreenshotUtil.capture(driver, "Pagination_Table_NotFound");
		}
	}
}
