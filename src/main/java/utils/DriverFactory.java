package utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

	public static WebDriver getDriver() {
		return TL_DRIVER.get();
	}

	public static void setDriver(WebDriver driver) {
		TL_DRIVER.set(driver);
	}

	public static void removeDriver() {
		WebDriver drv = TL_DRIVER.get();
		if (drv != null) {
			try {
				drv.quit();
			} catch (Exception e) {
				/* ignore */ }
			TL_DRIVER.remove();
		}
	}

	/**
	 * Initialize a ChromeDriver for this thread. You can switch to other browsers
	 * by adding similar methods.
	 */
	public static void initChromeDriver() {
		// Setup chromedriver binary
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();

		// If you want headless on CI, set system property -Dheadless=true
		String headless = System.getProperty("headless", "false");
		if (headless.equalsIgnoreCase("true")) {
			// newer chrome headless flag
			options.addArguments("--headless=new");
			options.addArguments("--disable-gpu");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
		}

		// Common options
		options.addArguments("--window-size=1920,1080");

		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize(); // <-- IMPORTANT FIX
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		setDriver(driver);
	}

}