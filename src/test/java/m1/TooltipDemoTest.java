package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.reactivex.rxjava3.functions.Action;
import utils.ScreenshotUtil;

public class TooltipDemoTest extends BaseTest {
	WebDriver driver;
	WebDriverWait wait;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void tooltipHandlecheck() {
		// getting the URL
		driver.get("https://jqueryui.com/tooltip/");

		// Instantiate Action Class
		Actions actions = new Actions(driver);

		String expTooltip = "We ask for your age only for statistical purposes.";

		// Switching ton the frame first
		driver.switchTo().frame(0);

		// Find the age field
		WebElement urage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='age']")));
		actions.moveToElement(urage).perform();
		By tooltipContent = By.cssSelector(".ui-tooltip .ui-tooltip-content");

		// wait for the tooltip to be visible then read its text
	    WebElement tooltip = wait.until(ExpectedConditions.visibilityOfElementLocated(tooltipContent));
		String toolTipText = tooltip.getText().trim();
		System.out.println("toolTipText-->" + toolTipText);

		// Printing the actual tooltip value
		System.out.println("Actual title of the tooltip is : " + toolTipText);

		// Comparing both expected and actual values
		if (toolTipText.equalsIgnoreCase(expTooltip)) {
			System.out.println("Tooltip test case is passed");
		} else {
			System.out.println("Tooltip test case is failed");
		}

		// Taking the screenshot
		ScreenshotUtil.capture(driver, "TooltTipCheck");

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
