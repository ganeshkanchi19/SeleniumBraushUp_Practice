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
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SelectDemo {
	WebDriver driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test
	public void selectTestCheck() throws IOException {
		driver.get("https://www.letskodeit.com/practice");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		WebElement carSel = driver.findElement(By.xpath("//select[@id='carselect']"));
		Select s = new Select(carSel);
		s.selectByVisibleText("Honda");
		String selcText = s.getFirstSelectedOption().getText();
		System.out.println("Selected value is: " + selcText);
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
