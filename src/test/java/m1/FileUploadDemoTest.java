package m1;

import static org.testng.Assert.assertTrue;

import java.time.Duration;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;

public class FileUploadDemoTest extends BaseTest {
	WebDriver driver;
	WebDriverWait wait;
	JavascriptExecutor js = (JavascriptExecutor) driver;
	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(CheckBoxTest.class);

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
	public void uploadingFileCheck_Single() {
		driver.get("https://testautomationpractice.blogspot.com/#");
		WebElement singChoose = driver.findElement(By.xpath("//input[@id='singleFileInput']"));
		wait.until(ExpectedConditions.visibilityOf(singChoose));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				singChoose);
		WebElement uplSinFile = driver
				.findElement(By.xpath("//button[@type='submit' and text()='Upload Single File']"));
		log.info("Selecting the file for uploading");
		singChoose.sendKeys("C:/FileDemo/SampleSingleFile.txt");
		wait.until(ExpectedConditions.visibilityOf(uplSinFile));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				uplSinFile);
		uplSinFile.click();
		WebElement singfileText = driver.findElement(By.xpath("//p[@id='singleFileStatus']"));
		if (singfileText.isDisplayed()) {
			System.out.println("Single file is uploaded successfully");
		} else {
			System.out.println("Error while uploading single file");
		}
		ScreenshotUtil.capture(driver, "SingleFileUpload");

	}

	@Test
	public void uploadingFileCheck_Multi() {
		driver.get("https://testautomationpractice.blogspot.com/#");
		WebElement multiChoose = driver.findElement(By.xpath("//input[@id='multipleFilesInput']"));
		wait.until(ExpectedConditions.visibilityOf(multiChoose));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				multiChoose);
		WebElement uplMultFile = driver
				.findElement(By.xpath("//button[@type='submit' and text()='Upload Multiple Files']"));
		log.info("Selecting the multiple files for uploading");
		multiChoose.sendKeys("C:/FileDemo/Multi1.txt\n" + "C:/FileDemo/Nature1.jpg\n" + "C:/FileDemo/Flower1.jpg");
		wait.until(ExpectedConditions.visibilityOf(uplMultFile));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});",
				uplMultFile);
		uplMultFile.click();
		WebElement multifileText = driver.findElement(By.xpath("//p[@id='multipleFilesStatus']"));
		String mulText = multifileText.getText();
		System.out.println("The text for uploaded multiple files is : ");
		System.out.println(mulText);
		if (multifileText.isDisplayed()) {
			System.out.println("Single file is uploaded successfully");
		} else {
			System.out.println("Error while uploading single file");
		}
		ScreenshotUtil.capture(driver, "MultipleFileUpload"); 
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
