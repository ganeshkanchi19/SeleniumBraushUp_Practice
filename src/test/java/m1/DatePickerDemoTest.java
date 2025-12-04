package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class DatePickerDemoTest extends BaseTest {
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
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	@Test
	public void datepickerCheck() {
		// URL launch for accessing calendar
		driver.get("https://www.tutorialspoint.com/selenium/practice/date-picker.php");

		// Identify element to get calendar
		WebElement cal = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='datetimepicker2']")));
		System.out.println("Before clicking on calender");
		cal.click();
		System.out.println("Calendar is  clicked");
		ScreenshotUtil.capture(driver, "AfterCalenderClick");

		// find all month selects and pick the visible one
		List<WebElement> monthSelects = driver.findElements(By.xpath("//select[@aria-label='Month']"));
		WebElement visibleMonthSelect = null;
		for (WebElement ms : monthSelects) {
			if (ms.isDisplayed() && ms.isEnabled()) {
				visibleMonthSelect = ms;
				break;
			}
		}

		if (visibleMonthSelect == null) {
			throw new RuntimeException("No visible month select found");
		}

		// use Select on the visible element
		Select sel = new Select(visibleMonthSelect);
		sel.selectByVisibleText("May");

		// Getting selected month
		String selectedMonth = sel.getFirstSelectedOption().getText();
		// Printing the selected month
		System.out.println("Selected month value is : " + selectedMonth);
		ScreenshotUtil.capture(driver, "AfterSelectingMonth");

		// 1) find visible year input
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

		// 2) clear it reliably (Ctrl+A then Backspace) and type new year
		visibleYear.click();
		visibleYear.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		visibleYear.sendKeys(Keys.BACK_SPACE);
		visibleYear.sendKeys("2000");
		ScreenshotUtil.capture(driver, "AfterSelectingYear");
		

		// selecting day 12
		WebElement day = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[contains(@aria-label,'" + selectedMonth + " 12')]")));
		day.click();

		// 1) Find the AM/PM toggle (better to locate by visible text instead of absolute xpath)
		By amPmLocator = By.xpath("/html/body/div[3]/div[3]/span[2]");
		WebElement amPmToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(amPmLocator));

		// Read current state and ensure it is AM
		String ampm = amPmToggle.getText().trim();
		System.out.println("AM/PM currently: " + ampm);

		// Click only if it's not AM
		if (!"AM".equalsIgnoreCase(ampm)) {
		    try {
		        amPmToggle.click();                // normal click
		    } catch (Exception e) {
		        // fallback to JS click if overlay/animation blocks native click
		        js.executeScript("arguments[0].click();", amPmToggle);
		    }
		    // small wait for widget to update
		    wait.until(d -> "AM".equalsIgnoreCase(amPmToggle.getText().trim()));
		}
		
		// selecting hour
		WebElement hour = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[3]/div[1]/input")));

		// removing existing hour then entering
		hour.clear();
		hour.sendKeys("9");

		// selecting minutes
		WebElement minutes = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[3]/div[2]/input")));

		// removing existing minutes then entering
		minutes.clear();
		minutes.sendKeys("5");

		// reflecting both date and time
		cal.click();

		// get date and time selected
		String dattime = cal.getAttribute("value");
		System.out.println("Selected date and time value is : " + dattime);

		// check date and time selected
		if (dattime.equalsIgnoreCase("2000-05-12 09:05")) {
			System.out.print("Date and Time selected successfully");
		} else {
			System.out.print("Date and Time selected unsuccessfully");
		}
		ScreenshotUtil.capture(driver, "Selecting_DateTimePicker_Calender");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
