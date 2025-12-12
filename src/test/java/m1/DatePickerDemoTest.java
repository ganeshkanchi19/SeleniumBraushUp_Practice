package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import org.testng.annotations.*;

import base.BaseTest;
import utils.ScreenshotUtil;

public class DatePickerDemoTest extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	// -------------------------------------------------------------
	// BEFORE TEST METHOD → Gets driver from BaseTest (DriverFactory)
	// -------------------------------------------------------------
	@BeforeMethod(alwaysRun = true)
	public void setup() {

		driver = getDriver(); // thread-local WebDriver from DriverFactory

		if (driver == null) {
			throw new IllegalStateException("Driver is null — BaseTest failed to initialize WebDriver.");
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		js = (JavascriptExecutor) driver;
	}

	// -------------------------------------------------------------
	// TEST METHOD
	// -------------------------------------------------------------
	@Test
	public void datepickerCheck() {

		driver.get("https://www.tutorialspoint.com/selenium/practice/date-picker.php");

		// Click to open calendar widget
		WebElement cal = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='datetimepicker2']")));
		cal.click();
		ScreenshotUtil.capture(driver, "AfterCalenderClick");

		// --------------------------
		// Select Month → MAY
		// --------------------------
		List<WebElement> monthSelects = driver.findElements(By.xpath("//select[@aria-label='Month']"));
		WebElement visibleMonthSelect = null;

		for (WebElement ms : monthSelects) {
			if (ms.isDisplayed() && ms.isEnabled()) {
				visibleMonthSelect = ms;
				break;
			}
		}
		if (visibleMonthSelect == null) {
			throw new RuntimeException("No visible month select found.");
		}

		Select sel = new Select(visibleMonthSelect);
		sel.selectByVisibleText("May");

		String selectedMonth = sel.getFirstSelectedOption().getText();
		ScreenshotUtil.capture(driver, "AfterSelectingMonth");
		System.out.println("Selected month value is : " + selectedMonth);

		// --------------------------
		// Select Year → 2000
		// --------------------------
		List<WebElement> yearInputs = driver.findElements(By.xpath("//input[@aria-label='Year']"));
		WebElement visibleYear = null;

		for (WebElement yi : yearInputs) {
			if (yi.isDisplayed() && yi.isEnabled()) {
				visibleYear = yi;
				break;
			}
		}

		if (visibleYear == null) {
			throw new RuntimeException("No visible year input found");
		}

		visibleYear.click();
		visibleYear.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		visibleYear.sendKeys(Keys.BACK_SPACE);
		visibleYear.sendKeys("2000");
		ScreenshotUtil.capture(driver, "AfterSelectingYear");

		// --------------------------
		// Select DAY → 12
		// --------------------------
		WebElement day = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[contains(@aria-label,'" + selectedMonth + " 12')]")));
		day.click();

		// --------------------------
		// Switch to AM (if PM)
		// --------------------------
		By amPmLocator = By.xpath(
				"//span[contains(@class,'bootstrap-datetimepicker-widget')]//span[contains(text(),'AM') or contains(text(),'PM')]");

		WebElement amPmToggle;
		try {
			amPmToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(amPmLocator));
		} catch (Exception ex) {
			amPmToggle = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[3]/span[2]")));
		}

		String ampm = amPmToggle.getText().trim();
		System.out.println("AM/PM currently: " + ampm);

		if (!"AM".equalsIgnoreCase(ampm)) {
			try {
				amPmToggle.click();
			} catch (Exception e) {
				js.executeScript("arguments[0].click();", amPmToggle);
			}

			// Wait until AM appears
			wait.until(ExpectedConditions.textToBePresentInElement(amPmToggle, "AM"));
		}

		// --------------------------
		// Enter Hour → 09
		// --------------------------
		WebElement hour = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//input[@aria-label='Hours'] | /html/body/div[3]/div[3]/div[1]/input")));

		hour.clear();
		hour.sendKeys("9");

		// --------------------------
		// Enter Minutes → 05
		// --------------------------
		WebElement minutes = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//input[@aria-label='Minutes'] | /html/body/div[3]/div[3]/div[2]/input")));

		minutes.clear();
		minutes.sendKeys("5");

		// Close calendar → apply date/time
		cal.click();

		String finalValue = cal.getAttribute("value");
		System.out.println("Selected date and time : " + finalValue);

		if ("2000-05-12 09:05".equalsIgnoreCase(finalValue)) {
			System.out.println("✔ Date & Time selection successful");
		} else {
			System.out.println("✘ Date & Time selection FAILED — Got: " + finalValue);
		}

		ScreenshotUtil.capture(driver, "Selecting_DateTimePicker_Calender");
	}

	// No @AfterTest — BaseTest takes care of quitting WebDriver

}
