package m1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class ActionsDemoTest extends BaseTest {
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeMethod(alwaysRun = true)
	public void init() {
		// get driver initialized by BaseTest -> DriverFactory
		driver = getDriver();
		if (driver == null) {
			throw new RuntimeException("Driver is NULL — BaseTest did not initialize it!");
		}

		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		} catch (Exception ignored) {
		}

		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}

		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void actionMethodsCheck() {
		// Launching the URL
		driver.get("https://demoqa.com/buttons");

		// Retrieve WebElement to perform right click
		WebElement rightclk = driver.findElement(By.xpath("//button[@id='rightClickBtn']"));

		// Waiting for the element to be visible
		wait.until(ExpectedConditions.visibilityOf(rightclk));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				rightclk);

		// Instantiate Action Class
		Actions act = new Actions(driver);

		// Right Click the button to display the text
		act.contextClick(rightclk).perform();

		// Locate the message displayed after right click
		WebElement msgrightclk = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='rightClickMessage']")));

		// Get the text
		String msgText = msgrightclk.getText();
		System.out.println("Message after right click: " + msgText);
		ScreenshotUtil.capture(driver, "RightClickText");

		// Launching the url for Automation Testing Practice
		driver.get("https://testautomationpractice.blogspot.com/");

		WebElement field1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("field1")));
		WebElement field2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("field2")));
		WebElement copyTextBtn = driver.findElement(By.xpath("//button[@ondblclick='myFunction1()']"));

		// Waiting for the element to be visible
		wait.until(ExpectedConditions.visibilityOf(copyTextBtn));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				copyTextBtn);

		act.doubleClick(copyTextBtn).perform();

		// move cursor away from the element
		act.moveByOffset(200, 0).click().perform(); // click on blank area

		// Get values after the double click
		String textField1 = field1.getAttribute("value");
		String textField2 = field2.getAttribute("value");

		// Asserting the text for Field1 and Field2
		Assert.assertEquals(textField1, textField2, "Field2 text is mismatching with Field1 text");

		System.out.println("Field2 copied text is : " + textField2);
		ScreenshotUtil.capture(driver, "Double_Click");

		// Handling the drag and drop action
		WebElement drag1 = driver.findElement(By.xpath("//div[@id='draggable']"));
		WebElement drop1 = driver.findElement(By.xpath("//div[@id='droppable']"));

		// Performing the drag and drop action
		act.clickAndHold(drag1).moveToElement(drop1).release().perform();

		WebElement droppedText = driver
				.findElement(By.xpath("//div[@id='droppable']//p[normalize-space()='Dropped!']"));

		if (droppedText.isDisplayed()) {
			System.out.println("Drag and Drop action is successful");
		} else {
			System.out.println("Drag and Drop action failed");
		}
		// Taking the screenshot
		ScreenshotUtil.capture(driver, "Drag_Drop");

		// Hover on "Point Me" button
		WebElement pointMe = driver.findElement(By.xpath("//button[text()='Point Me']"));
		act.moveToElement(pointMe).perform();

		// Wait for dropdown options to appear
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dropdown-content']/a")));

		// Get all dropdown elements
		List<WebElement> options = driver.findElements(By.xpath("//div[@class='dropdown-content']/a"));

		System.out.println("Elements present inside the dropdown after mouse hover are : ");
		// Print the text of each item
		for (WebElement opt : options) {
			System.out.println(opt.getText());
		}
		// Taking the screenshot
		ScreenshotUtil.capture(driver, "Mouse_Hover");

		// Handling the slider action
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement rightHandle = driver
				.findElement(By.xpath("//div[@id='slider-range']//span[contains(@class,'ui-slider-handle')][2]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				rightHandle);
		wait.until(ExpectedConditions.visibilityOf(rightHandle));

		// locate price label and read current value
		WebElement priceRange = driver.findElement(By.id("amount"));
		String before = priceRange.getAttribute("value");
		System.out.println("Before sliding to the right: " + before);
		ScreenshotUtil.capture(driver, "Slider_PriceRange_Before");

		// sliding the right handle
		act.clickAndHold(rightHandle).moveByOffset(50, 0).pause(Duration.ofMillis(200)).release().perform();

		// Get the updated price range text
		String updatedValue = priceRange.getAttribute("value");
		System.out.println("Updated price range: " + updatedValue);
		ScreenshotUtil.capture(driver, "Slider_PriceRange_After_right_handle");

		// Sliding the left handle to the extreme left point i.e $0
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement leftHandle = driver
				.findElement(By.xpath("//div[@id='slider-range']//span[contains(@class,'ui-slider-handle')][1]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				rightHandle);
		wait.until(ExpectedConditions.visibilityOf(leftHandle));
		act.clickAndHold(leftHandle).moveByOffset(-200, 0) // enough to hit the far left
				.release().perform();

		// Getting the updated price range value after performing left handle slide
		// activity
		String valuelefthandle = priceRange.getAttribute("value");
		System.out.println("Updated price range after performing left handle slide: " + valuelefthandle);
		ScreenshotUtil.capture(driver, "Slider_PriceRange_After_left_handle");
	}

	// No @AfterTest here – BaseTest handles driver cleanup
}
