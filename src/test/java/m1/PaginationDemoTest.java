package m1;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
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

public class PaginationDemoTest extends BaseTest {
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

	@Test()
	public void verifyPaginationAndPrintProducts() {
		driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=product/category&path=25_28");
		WebElement productItemsLocator = driver.findElement(By.cssSelector(".product-layout"));
		wait.until(ExpectedConditions.visibilityOf(productItemsLocator));
		WebElement pagesText = driver.findElement(By.xpath("//div[text()='Showing 1 to 15 of 75 (5 Pages)']"));
		String paginationText = pagesText.getText().trim();
		System.out.println("The complete pagination text is : " + paginationText);

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'bottom'});",
				pagesText);

		if (paginationText.contains("5 Pages")) {
			System.out.println("The page contains the text 5 pages");
			// nextBtn.click();
		} else {
			System.out.println("The 5 pages text is absent");
		}

		// Product title / price locators
		By productCard = By.cssSelector(".product-layout");
		By productTitle = By.cssSelector(".caption h4 a");
		By productPrice = By.cssSelector(".price");

		// NEXT button locator (we will re-find this each iteration)
		By nextBtnBy = By.xpath("//ul[contains(@class,'pagination')]//a[normalize-space(text())='>']");

		int page = 1;
		int maxPages = 10; // safety cap (we expect 5)
		int safety = 0;

		while (page <= 5 && safety++ < maxPages) {
			List<WebElement> products = driver.findElements(productCard);
			System.out.println("=== Page " + page + " — found " + products.size() + " product(s) ===");

			// print product info
			for (int i = 0; i < products.size(); i++) {
				WebElement card = products.get(i);
				String title = "";
				String price = "";
				try {
					WebElement t = card.findElement(productTitle);
					title = t.getText().trim();
				} catch (NoSuchElementException ignored) {
				}
				try {
					WebElement p = card.findElement(productPrice);
					price = p.getText().trim();
				} catch (NoSuchElementException ignored) {
				}
				System.out.printf("Product %d: %s %s%n", i + 1, title, price.isEmpty() ? "" : (" | Price: " + price));
			}

			if (page == 5)
				break;

			// Remember first product element on page to wait for its staleness after click
			List<WebElement> productsNow = driver.findElements(productCard);
			WebElement firstBefore = products.isEmpty() ? null : products.get(0);
			try {
				WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextBtnBy));
				nextBtn.click();
			} catch (ElementClickInterceptedException e) {
			}

			// Wait for the products area to refresh
			if (firstBefore != null) {
				wait.until(ExpectedConditions.stalenessOf(firstBefore));
			}
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCard));

			page++;

		}

		System.out.println("Finished iterating pages.");
		String lastPageUrl = driver.getCurrentUrl();
		System.out.println("Last page url is : " + lastPageUrl);
		ScreenshotUtil.capture(driver, "PaginationDemo");

	}

	@Test
	public void webTableValueSel() {
		driver.get("https://testautomationpractice.blogspot.com/#");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		final String target = "Portable Charger";

		// robust locator: find the table after the "Pagination Web Table" heading
		By rowsLocator = By.xpath("//h2[normalize-space()='Pagination Web Table']/following::table[1]//tbody/tr");

		// Pagination page link template for numbers 1..4
		String pageLinkXpathTemplate = "//ul[contains(@class,'pagination')]//a[normalize-space()='%d']";

		boolean found = false;

		// ensure table present
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowsLocator));

		for (int page = 1; page <= 4; page++) {
			System.out.println("Searching page: " + page);

			// re-find rows each iteration to avoid stale references
			List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowsLocator));

			// iterate rows and use normalize-space in XPath to match the cell text robustly
			for (int i = 1; i <= rows.size(); i++) {
				// xpath to the name cell normalized
				By nameCellBy = By.xpath("(" + rowsLocator.toString().replace("By.xpath: ", "") + ")[" + i + "]/td[2]");
				// but simpler: find row then get td[2]
				WebElement row = rows.get(i - 1);
				String nameText = "";
				try {
					WebElement nameCell = row.findElement(By.xpath("./td[2]"));
					nameText = nameCell.getText().replace('\u00A0', ' ').trim(); // replace NBSP then trim
				} catch (Exception e) {
					continue; // skip malformed rows
				}

				if (target.equalsIgnoreCase(nameText)) {
					System.out.println("Value found in the pagination web table (page " + page + ")");
					// click the checkbox in column 4
					try {
						WebElement checkbox = row.findElement(By.xpath(".//td[4]//input[@type='checkbox']"));
						if (!checkbox.isSelected()) {
							js.executeScript("arguments[0].scrollIntoView({block:'center'});", checkbox);
							try {
								checkbox.click();
							} catch (ElementClickInterceptedException ex) {
								js.executeScript("arguments[0].click();", checkbox);
							}
						}
					} catch (NoSuchElementException ex) {
						System.out.println("Checkbox not found for this row — DOM unexpected.");
					}
					ScreenshotUtil.capture(driver, "Pagination_Table_Found");
					found = true;
					break;
				}
			}

			if (found)
				break;

			// go to next page by clicking page number (page+1)
			if (page < 4) {
				int nextPage = page + 1;
				By nextPageBy = By.xpath(String.format(pageLinkXpathTemplate, nextPage));

				// remember first row to wait for staleness
				List<WebElement> currentRows = driver.findElements(rowsLocator);
				WebElement firstBefore = currentRows.isEmpty() ? null : currentRows.get(0);

				WebElement pageLink = wait.until(ExpectedConditions.elementToBeClickable(nextPageBy));
				js.executeScript("arguments[0].scrollIntoView({block:'center'});", pageLink);
				try {
					pageLink.click();
				} catch (ElementClickInterceptedException eie) {
					js.executeScript("arguments[0].click();", pageLink);
				}

				// wait for change
				if (firstBefore != null) {
					wait.until(ExpectedConditions.stalenessOf(firstBefore));
				} else {
					wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowsLocator));
				}
			}
		}

		if (!found) {
			System.out.println("Value is absent in the pagination web table");
			ScreenshotUtil.capture(driver, "Pagination_Table_NotFound");
		}
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
