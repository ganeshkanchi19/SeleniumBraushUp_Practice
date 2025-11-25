package m1;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

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
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MuliSelectDemo {
	WebDriver driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test
	public void multiSelCheck() throws IOException {
		driver.get("https://www.letskodeit.com/practice");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		WebElement mulSel = driver.findElement(By.xpath("//select[@id='multiple-select-example']"));
		Select sel = new Select(mulSel);
		sel.selectByIndex(1);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		sel.selectByVisibleText("Peach");
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destPath = System.getProperty("user.dir") + "/Screenshots/MultiSelChk.png";
		File dest = new File(destPath);
		dest.getParentFile().mkdirs(); // create folder if missing
		FileUtils.copyFile(src, dest);

		// Print selected options
		List<WebElement> selected = sel.getAllSelectedOptions();

		for (WebElement options : selected) {
			System.out.println("Selected values are : " + options.getText());

		}

	}

	@AfterTest
	public void browserClose() {
		driver.quit();
	}

}
