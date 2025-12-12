package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import base.BaseTest;
import utils.ScreenshotUtil;

public class FileUploadDemoTest extends BaseTest {
	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(FileUploadDemoTest.class);

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is null — BaseTest did not initialize it.");
		}

		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		} catch (Exception ignored) {
		}

		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		js = (JavascriptExecutor) driver;

		log.info("Obtained driver from BaseTest/DriverFactory");
	}

	@Test
	public void uploadingFileCheck_Single() {
		driver.get("https://testautomationpractice.blogspot.com/#");

		WebElement singleChoose = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("singleFileInput")));
		// scroll into view for consistent behaviour
		try {
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", singleChoose);
		} catch (Exception ignored) {
		}

		WebElement uploadBtn = driver
				.findElement(By.xpath("//button[@type='submit' and normalize-space()='Upload Single File']"));

		log.info("Selecting the file for uploading (single)");
		// adjust path as needed for your environment
		singleChoose.sendKeys("C:/FileDemo/SampleSingleFile.txt");

		wait.until(ExpectedConditions.elementToBeClickable(uploadBtn));
		try {
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", uploadBtn);
		} catch (Exception ignored) {
		}
		uploadBtn.click();

		// verify upload status element
		WebElement singleFileStatus = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("singleFileStatus")));
		if (singleFileStatus.isDisplayed()) {
			log.info("Single file upload completed: " + singleFileStatus.getText());
		} else {
			log.warn("Single file upload status not visible.");
		}

		ScreenshotUtil.capture(driver, "SingleFileUpload");
	}

	@Test
	public void uploadingFileCheck_Multi() {
		driver.get("https://testautomationpractice.blogspot.com/#");

		WebElement multiChoose = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("multipleFilesInput")));
		try {
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", multiChoose);
		} catch (Exception ignored) {
		}

		WebElement uploadMultiBtn = driver
				.findElement(By.xpath("//button[@type='submit' and normalize-space()='Upload Multiple Files']"));

		log.info("Selecting multiple files for uploading");
		// combine multiple paths separated by newline — ensure files exist on the agent
		// running the tests
		multiChoose.sendKeys("C:/FileDemo/Multi1.txt\n" + "C:/FileDemo/Nature1.jpg\n" + "C:/FileDemo/Flower1.jpg");

		wait.until(ExpectedConditions.elementToBeClickable(uploadMultiBtn));
		try {
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", uploadMultiBtn);
		} catch (Exception ignored) {
		}
		uploadMultiBtn.click();

		WebElement multiFileStatus = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("multipleFilesStatus")));
		String statusText = multiFileStatus.getText();
		log.info("Multiple upload status: " + statusText);

		if (multiFileStatus.isDisplayed()) {
			log.info("Multiple files upload appears successful.");
		} else {
			log.warn("Multiple files upload status not visible.");
		}

		ScreenshotUtil.capture(driver, "MultipleFileUpload");
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() {
		// Do NOT quit driver here. BaseTest.tearDown() will quit & remove the
		// thread-local driver.
		log.info("Test finished. BaseTest will handle driver cleanup.");
	}
}
