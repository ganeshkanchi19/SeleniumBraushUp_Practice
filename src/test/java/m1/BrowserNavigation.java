package m1;

import java.io.Console;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserNavigation extends BaseTest {
	WebDriver driver;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test
	public void browserNavigateCheck() {
		driver.get("https://demoqa.com/text-box");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebElement fullname = driver.findElement(By.xpath("//input[@id='userName']"));
		WebElement email = driver.findElement(By.xpath("//input[@id='userEmail']"));
		WebElement curAddr = driver.findElement(By.xpath("//textarea[@id='currentAddress']"));
		WebElement PermAddr = driver.findElement(By.xpath("//textarea[@id='permanentAddress']"));
		WebElement SubBtn = driver.findElement(By.xpath("//button[@id='submit']"));
		WebElement Nameline = driver.findElement(By.xpath("//p[@id='name']/text()[2]"));
		WebElement TextBoxTitle = driver.findElement(By.xpath("//h1[text()='Text Box']"));

		// scroll to center, then a bit up so ad doesn't cover button
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// 1) Scroll submit button into view (roughly middle)
		js.executeScript("arguments[0].scrollIntoView({block: 'center'});", SubBtn);

		// 2) Move slightly up so the bottom ad banner doesn't overlap
		js.executeScript("window.scrollBy(0, -120);");

		// 3) Click using JavaScript (bypasses overlay)
		js.executeScript("arguments[0].click();", SubBtn);

//		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", SubBtn);
//		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
//		
//		// explicit wait for clickability
//		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//		wait.until(ExpectedConditions.elementToBeClickable(SubBtn));
//		
//		try {
//		    SubBtn.click();
//		} catch (ElementClickInterceptedException e) {
//		    System.out.println("Normal click intercepted, clicking with JS instead");
//		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", SubBtn);
//		}

		System.out.println("Starting to filling the full name  value");
		String FNameVal = fullname.getText();
		fullname.sendKeys("Ganesh Kanchi");
		// System.out.println("Full Name filled is : " + FNameVal);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		email.sendKeys("ganukanchi2018@gmail.com");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		curAddr.sendKeys("Bachelors PG, Hinjewadi");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		PermAddr.sendKeys("Flat No 809, 2nd floor, Near BOSS PG, Bachelors PG, Hinjewadi");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		SubBtn.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		// Get displayed output
		String NameText = Nameline.getText();
		System.out.println("Submitted Full Name is : " + NameText);
		// Extract only name part
		// String ActName = outputText.replace("Name:", "").trim();
		System.out.println("Actual extracted value is : " + NameText);
		// Expected value
		String ExpName = "Ganesh Kanchi";
		if (NameText == ExpName) {
			System.out.println("Test case is passed");
		} else {
			System.out.println("Test case is failed");
		}

	}

	@AfterTest
	public void browserClose() {
		driver.quit();
	}

}
