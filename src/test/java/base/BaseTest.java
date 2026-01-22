package base;

import org.testng.annotations.*;
import org.testng.ITestContext;
import utils.DriverFactory;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import utils.ScreenshotUtil;


public class BaseTest {

	/**
	 * Clear screenshots & reports before suite run.
	 */
	@BeforeSuite(alwaysRun = true)
	public void clearReportsAndScreenshots() {
		// clear screenshots folder
		ScreenshotUtil.clearScreenshotFolder();
		System.out.println("✔ Screenshots folder cleared before test suite run.");

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
			System.out.println("✔ Reports folder cleared before test suite run.");
		} else {
			System.out.println("No reports folder to clear (reports/).");
		}
	}

	/**
	 * This method initializes the WebDriver before every test method.
	 *
	 * TestNG order guarantee: parent class config methods run before child class
	 * config methods when the annotation is the same (both @BeforeMethod).
	 *
	 * It supports: - testng.xml parameter:
	 * <parameter name="browser" value="chrome" /> - CLI override: -Dbrowser=firefox
	 */
	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	public void baseSetup(@Optional("chrome") String browser, ITestContext ctx) {
		// CLI override: -Dbrowser=firefox
		String sysBrowser = System.getProperty("browser");
		if (sysBrowser != null && !sysBrowser.trim().isEmpty()) {
			browser = sysBrowser.trim();
		}

		System.out.printf(">>> BaseTest.baseSetup(): initialising browser='%s' thread=%d context=%s%n", browser,
				Thread.currentThread().getId(), ctx == null ? "null" : ctx.getName());

		// initialize thread-local driver (DriverFactory must create the driver)
		DriverFactory.initChromeDriver();

		// verify driver created
		WebDriver wd = DriverFactory.getDriver();
		if (wd == null) {
			// critical: if this happens log and fail fast so tests can't proceed with null
			// driver
			throw new IllegalStateException(
					"BaseTest.baseSetup(): failed to initialize WebDriver for browser=" + browser);
		}
	}

	/**
	 * Quit driver after each test method.
	 */
	@AfterMethod(alwaysRun = true)
	public void baseTearDown() {
		System.out.printf(">>> BaseTest.baseTearDown() - quitting driver thread=%d%n", Thread.currentThread().getId());
		DriverFactory.removeDriver();
	}

	/**
	 * Utility getter for subclasses.
	 */
	protected WebDriver getDriver() {
		return DriverFactory.getDriver();
	}
}
