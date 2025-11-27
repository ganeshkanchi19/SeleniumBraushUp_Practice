package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ScreenshotUtil;
import utils.ScrollUtil;

public class IFrameTestDemo extends BaseTest {
	WebDriver driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test
	public void iframeCheck() {
		// navigate to url
		driver.get("https://demoqa.com/frames");
		
		// Maximize the window
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

		// Switch to Frame using Index
		driver.switchTo().frame("frame1");

		// Identifying the heading in webelement
		WebElement frame1head = driver.findElement(By.xpath("//h1[@id='sampleHeading']"));
		
		//Scrolling to the specific webelement
		ScrollUtil.scrollToElement(driver, frame1head);

		// Finding the text of the heading
		String frame1Text = frame1head.getText();
		
        //Printing the text of the frame 
		System.out.println("Text of the frame is : " + frame1Text);
		
		//Finally taking the screenshots
		ScreenshotUtil.capture(driver, "Frame1Demo");
	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
