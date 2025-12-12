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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;

public class LinksDemoTest extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is NULL — BaseTest failed to initialize it.");
		}

		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		} catch (Exception ignored) {
		}

		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// -------------------------------
	// Test 1: Extract and print all valid links
	// -------------------------------
	@Test
	public void linkscheck() {

		driver.get("https://testautomationpractice.blogspot.com/#");

		List<WebElement> allLinks = driver.findElements(By.tagName("a"));
		System.out.println("Total no of links found: " + allLinks.size());

		List<String> validLinks = new ArrayList<>();

		for (WebElement link : allLinks) {
			String href = link.getAttribute("href");

			if (href == null || href.trim().isEmpty())
				continue;

			// Skip non-navigable links
			if (href.startsWith("javascript:") || href.equals("#") || href.startsWith("mailto:"))
				continue;

			validLinks.add(href);
		}

		System.out.println("Valid HTTP links count: " + validLinks.size());

		for (String url : validLinks) {
			System.out.println(url);
		}
	}

	// -------------------------------
	// Test 2: Check which links are broken
	// -------------------------------
	@Test
	public void checkBrokenLinks() throws IOException {

		System.out.println("\n------ Broken Links Check Start ------");

		driver.get("https://testautomationpractice.blogspot.com/#");

		List<WebElement> links = driver.findElements(By.tagName("a"));
		System.out.println("Total links found: " + links.size());

		for (WebElement link : links) {
			String href = link.getAttribute("href");

			if (href == null || href.trim().isEmpty())
				continue;
			if (href.startsWith("javascript:") || href.equals("#") || href.startsWith("mailto:"))
				continue;

			verifyLinks(href);
		}

		System.out.println("------ Broken Links Check Completed ------\n");
	}

	// -------------------------------
	// Helper: Verify a single link
	// -------------------------------
	public static void verifyLinks(String linkUrl) {

		try {
			URL url = new URL(linkUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setConnectTimeout(5000);
			http.connect();

			int statusCode = http.getResponseCode();

			if (statusCode >= 400) {
				System.out.println(linkUrl + " --> BROKEN (" + statusCode + ")");
			} else {
				System.out.println(linkUrl + " --> OK (" + statusCode + ")");
			}

		} catch (Exception e) {
			System.out.println(linkUrl + " --> ERROR (" + e.getMessage() + ")");
		}
	}

	// ❌ NOTE: Do NOT quit driver here
	// BaseTest.tearDown() handles browser cleanup
}
