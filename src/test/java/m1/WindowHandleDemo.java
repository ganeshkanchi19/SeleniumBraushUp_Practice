package m1;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

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

public class WindowHandleDemo extends BaseTest {
	WebDriver driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test
	public void windowHandleTest() {
		driver.get("https://demoqa.com/browser-windows");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.xpath("//button[@id='tabButton']")).click();
		String ParentWindow = driver.getWindowHandle();
		System.out.println("Parent window handle is : " + ParentWindow);
		Set<String> allWindowHandles = driver.getWindowHandles();
		Iterator<String> ite = allWindowHandles.iterator();

		// Here we will check if child window has other child windows and will fetch the
		// heading of the child window

		while (ite.hasNext()) {
			String chidlwindow = ite.next();
			if (!ParentWindow.equals(chidlwindow)) {
				driver.switchTo().window(chidlwindow);
				WebElement childwindText = driver.findElement(By.xpath("//h1[@id='sampleHeading']"));
				System.out.println("Heading of the child window is : " + childwindText.getText());
				ScreenshotUtil.capture(driver, "Child_Window");
				// Closing the child window
				driver.close();
				// Immediately switch back to parent window
				driver.switchTo().window(ParentWindow);
			}
		}

		System.out.println("Parent window title is : " + driver.getTitle());
		ScreenshotUtil.capture(driver, "Parent_Window");

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
