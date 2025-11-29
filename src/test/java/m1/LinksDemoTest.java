package m1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LinksDemoTest extends BaseTest {
	WebDriver driver;
	WebDriverWait wait;

	@BeforeTest
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		// short implicit is fine but prefer explicit waits below
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void linkscheck() {
		driver.get("https://testautomationpractice.blogspot.com/#");
		List<WebElement> alllinks = driver.findElements(By.tagName("a"));
		int count = alllinks.size();
		System.out.println("Total no of links are : " + count);
		List<String> validLinks = new ArrayList<>();

		for (WebElement link : alllinks) {
			String href = link.getAttribute("href");

			if (href != null && !href.trim().isEmpty()) {
				// Optional: ignore javascript:, #, mailto:
				if (!href.startsWith("javascript:") && !href.equals("#") && !href.startsWith("mailto:")) {

					validLinks.add(href);
				}
			}
		}

		System.out.println("Links having href: " + validLinks.size());
		for (String url : validLinks) {
			System.out.println(url);
		}

	}

	@Test
	public void checkBrokenLinks() throws IOException {
		System.out.println("Starting the broken links section");
		driver.get("https://testautomationpractice.blogspot.com/#");
		// Storing the links in a list and traversing through the links
		List<WebElement> links = driver.findElements(By.tagName("a"));

		// This line will print the number of links and the count of links.
		System.out.println("No of links found on web page  are : " + links.size());

		// Checking the links fetched
		for (int i = 0; i < links.size(); i++) {
			WebElement fetclink = links.get(i);
			String url = fetclink.getAttribute("href");
			verifyLinks(url);
		}
	}

	public static void verifyLinks(String linkUrl) throws IOException {
		try {
			URL url = new URL(linkUrl);
			// Now we will be creating url connection and getting the response code
			HttpURLConnection httpUrlConnect = (HttpURLConnection) url.openConnection();
			httpUrlConnect.setConnectTimeout(5000);
			httpUrlConnect.connect();
			if (httpUrlConnect.getResponseCode() >= 400) {
				System.out.println(linkUrl + " - " + httpUrlConnect.getResponseMessage() + " is a broken link");
			}

			// Fetching and Printing the response code obtained
			else {
				System.out.println(linkUrl + " - " + httpUrlConnect.getResponseCode());
			}
		} catch (Exception e) {
			 System.out.println("Error checking URL: " + linkUrl + " -> " + e.getMessage());
		}

	}

	@AfterTest
	public void browserClose() {
		if (driver != null) {
			driver.quit();
		}
	}

}
