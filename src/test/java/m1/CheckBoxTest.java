package m1;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CheckBoxTest {
	WebDriver driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test
	public void verifyCheckBox() throws IOException {
		driver.get("https://www.letskodeit.com/practice");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		/**
		 * Validate isSelected and click
		 */

		WebElement chkBoxSelcted = driver.findElement(By.xpath("//input[@id='bmwcheck']"));
		boolean isSelected = chkBoxSelcted.isSelected();

		// performing click operation if element is not selected
		if (isSelected == false) {
			chkBoxSelcted.click();
		}

		/**
		 * Validate isDisplayed and click
		 */
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		WebElement chkBoxDisplayed = driver.findElement(By.xpath("//input[@id='benzcheck']"));
		boolean isDisplayed = chkBoxDisplayed.isDisplayed();

		// performing click operation if element is displayed
		if (isDisplayed == true) {
			chkBoxDisplayed.click();
		}

		/**
		 * Validate isEnabled and click
		 */
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		WebElement chkBobEnabled = driver.findElement(By.xpath("//input[@id='hondacheck']"));
		boolean isEnabled = chkBobEnabled.isEnabled();

		// performing click operation if element is enabled
		if (isEnabled == true) {
			chkBobEnabled.click();
		}
		//Taking screenshot
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destPath = System.getProperty("user.dir") + "/Screenshots/SeelctFromDrpDwn.png";
		File dest = new File(destPath);
		dest.getParentFile().mkdirs(); // create folder if missing
		FileUtils.copyFile(src, dest);

	}

	@AfterTest
	public void browserClose() {
		driver.quit();
	}

}
