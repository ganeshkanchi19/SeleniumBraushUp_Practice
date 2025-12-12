package m1;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.ScreenshotUtil;

public class WindowHandleDemo extends BaseTest {

	private WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	public void init() {
		// driver created inside BaseTest → DriverFactory
		driver = getDriver();

		if (driver == null) {
			throw new RuntimeException("Driver is NULL — BaseTest did not initialize it!");
		}

		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}

		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		} catch (Exception ignored) {
		}
	}

	@Test
	public void windowHandleTest() {
		driver.get("https://demoqa.com/browser-windows");

		driver.findElement(By.id("tabButton")).click();

		String parentWindow = driver.getWindowHandle();
		System.out.println("Parent window handle is : " + parentWindow);

		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> it = allWindows.iterator();

		while (it.hasNext()) {
			String childWindow = it.next();

			if (!parentWindow.equals(childWindow)) {
				driver.switchTo().window(childWindow);

				WebElement childHeading = driver.findElement(By.id("sampleHeading"));
				System.out.println("Heading of child window: " + childHeading.getText());

				ScreenshotUtil.capture(driver, "Child_Window");

				// Close child window
				driver.close();

				// Switch back to main window
				driver.switchTo().window(parentWindow);
			}
		}

		System.out.println("Parent window title is : " + driver.getTitle());
		ScreenshotUtil.capture(driver, "Parent_Window");
	}

	// ❌ No @AfterTest — BaseTest handles driver.quit()
}
