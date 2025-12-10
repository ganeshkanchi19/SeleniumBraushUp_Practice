package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Screenshot utility for parallel tests. Produces unique timestamped filenames
 * (no stable copies) and a safe capture method.
 */
public class ScreenshotUtil {

	private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

	/**
	 * Capture a screenshot and always produce a timestamped filename inside
	 * <project-root>/Screenshots/<file>.png. Returns the absolute path or null on
	 * failure.
	 *
	 * This method will NOT produce any stable filename - only timestamped ones.
	 *
	 * @param driver   WebDriver instance
	 * @param baseName a short name describing the step (will be sanitized)
	 * @return absolute path to saved screenshot or null
	 */
	public static String capture(WebDriver driver, String baseName) {
	    try {
	        if (driver == null) {
	            System.err.println("[SCREENSHOT] driver is null, cannot capture.");
	            return null;
	        }

	        File targetDir = new File(System.getProperty("user.dir"), "Screenshots");
	        if (!targetDir.exists()) {
	            targetDir.mkdirs();
	        }

	        String safeBase = (baseName == null || baseName.isBlank())
	                ? "screenshot"
	                : baseName.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");

	        // timestamp
	        String ts = LocalDateTime.now().format(TF);

	        // detect browser only
	        String browser = "browser";
	        try {
	            if (driver instanceof RemoteWebDriver) {
	                browser = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
	            }
	        } catch (Exception ignored) {}

	        // final clean file name
	        String fileName = String.format("%s_%s_%s.png", safeBase, browser, ts);

	        File dest = new File(targetDir, fileName);

	        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

	        System.out.println("[SCREENSHOT] Saved: " + dest.getAbsolutePath());
	        return dest.getAbsolutePath();

	    } catch (Exception e) {
	        System.err.println("[SCREENSHOT] Failed: " + e.getMessage());
	        return null;
	    }
	}
	/**
	 * Safe capture that waits for page readiness, ensures reasonable window size,
	 * includes browser/session info where possible, and uses the timestamped
	 * capture.
	 *
	 * Returns absolute path or null.
	 */
	public static String captureSafe(WebDriver driver, String baseName) {
		try {
			if (driver == null)
				driver = DriverFactory.getDriver();
			if (driver == null)
				return null;

			try {
				new WebDriverWait(driver, Duration.ofSeconds(10)).until(
						wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
			} catch (Exception ignored) {
			}

			Thread.sleep(200);

			try {
				driver.manage().window().setSize(new Dimension(1366, 768));
				driver.manage().window().maximize();
			} catch (Exception ignored) {
			}

			String safeBase = (baseName == null || baseName.isBlank()) ? "screenshot" : baseName;

			// detect browser name only
			String browser = "browser";
			try {
				if (driver instanceof RemoteWebDriver) {
					browser = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
				}
			} catch (Exception ignored) {
			}

			// combined simple base
			String composedBase = safeBase + "_" + browser;

			return capture(driver, composedBase);

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Clear screenshots folder (non-recursive). Keeps behavior the same: deletes
	 * all files in Screenshots directory. Call this in @BeforeSuite if you want to
	 * purge older timestamped files.
	 */
	public static void clearScreenshotFolder() {
		File folder = new File(System.getProperty("user.dir"), "Screenshots");
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					try {
						if (!file.delete()) {
							System.err.println("[SCREENSHOT] Failed to delete: " + file.getAbsolutePath());
						}
					} catch (Exception ignored) {
					}
				}
			}
			System.out.println("[SCREENSHOT] Old screenshots deleted.");
		} else {
			System.out.println("[SCREENSHOT] No screenshots folder to clear.");
		}
	}
}
