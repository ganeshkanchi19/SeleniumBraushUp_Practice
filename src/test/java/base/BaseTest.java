package base;

import org.testng.annotations.*;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import utils.DriverFactory;
import utils.ScreenshotUtil; // <-- added

public class BaseTest {

	@BeforeSuite(alwaysRun = true)
	public void clearReportsAndScreenshots() {

		// clear screenshots
		ScreenshotUtil.clearScreenshotFolder();
		System.out.println("Screenshots folder cleared before test suite run.");

		// clear reports folder (non-recursive)
		Path reportsDir = Paths.get(System.getProperty("user.dir"), "reports");
		File reportsFolder = reportsDir.toFile();

		if (reportsFolder.exists() && reportsFolder.isDirectory()) {
			File[] files = reportsFolder.listFiles();
			if (files != null) {
				for (File f : files) {
					try {
						if (!f.delete()) {
							System.err.println("Could not delete file: " + f.getAbsolutePath());
						}
					} catch (Exception ignored) {
					}
				}
			}
			System.out.println("Reports folder cleared before test suite run.");
		} else {
			System.out.println("No reports folder to clear (reports/).");
		}
	}

	// receive "browser" parameter from testng-parallel.xml
	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	public void setup(@Optional("chrome") String browser) throws MalformedURLException {
		System.out.println("Launching browser: " + browser);
		// Use DriverFactory to init the correct browser for the current thread
		DriverFactory.initDriver(browser);
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		System.out.println("Closing browser...");
		// Quit & remove the thread-local driver
		DriverFactory.removeDriver();
	}

	public WebDriver getDriver() {
		return DriverFactory.getDriver();
	}
}
