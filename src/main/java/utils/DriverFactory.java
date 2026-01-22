package utils;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

	private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

	private DriverFactory() {
		// prevent object creation
	}

	public static WebDriver getDriver() {
		return TL_DRIVER.get();
	}

	private static void setDriver(WebDriver driver) {
		TL_DRIVER.set(driver);
	}

	public static void initChromeDriver() {
		try {
			boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));

			boolean useRemote = Boolean.parseBoolean(System.getProperty("use.remote", "false"));

//			System.out.println("Running Chrome in headless mode = " + isHeadless);
//			System.out.println("Running tests on Remote Grid = " + useRemote);

			ChromeOptions options = new ChromeOptions();

			// Required for Docker / Kubernetes
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");

			if (isHeadless) {
				options.addArguments("--headless=new");
				options.addArguments("--window-size=1920,1080");
			} else {
				options.addArguments("--start-maximized");
			}

			WebDriver driver;

			if (useRemote) {
				String gridUrl = System.getProperty("grid.url",
						"http://selenium-grid-selenium-hub.default.svc:4444/wd/hub");
				driver = new RemoteWebDriver(new URL(gridUrl), options);
			} else {
				driver = new ChromeDriver(options);
			}

			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

			setDriver(driver);

		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize WebDriver", e);
		}
	}

	public static void removeDriver() {
		WebDriver driver = TL_DRIVER.get();
		if (driver != null) {
			driver.quit();
			TL_DRIVER.remove();
		}
	}
}
