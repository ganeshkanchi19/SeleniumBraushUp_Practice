package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.TakesScreenshot;

/* other imports kept */

public class ScreenshotUtil {
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    public static void capture(WebDriver driver, String fileName) {
        try {
            File targetDir = new File(System.getProperty("user.dir"), "Screenshots");
            if (!targetDir.exists()) targetDir.mkdirs();

            String safeBase = (fileName == null || fileName.isBlank()) ? "screenshot" :
                    fileName.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");

            String ts = LocalDateTime.now().format(TF);

            String browser = "browser";
            try {
                if (driver instanceof RemoteWebDriver) {
                    browser = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
                }
            } catch (Exception ignored) {}

            String file = String.format("%s_%s_%s.png", safeBase, browser, ts);
            File dest = new File(targetDir, file);

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved at: " + dest.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    public static void clearScreenshotFolder() {
        File folder = new File(System.getProperty("user.dir"), "Screenshots");
        if (!folder.exists()) {
            System.out.println("✔ Screenshots folder does not exist (nothing to clear).");
            return;
        }
        if (!folder.isDirectory()) {
            System.err.println("Screenshots path exists but is not a directory: " + folder.getAbsolutePath());
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("✔ Screenshots folder is already empty.");
            return;
        }

        int deleted = 0;
        for (File f : files) {
            try {
                if (f.isFile()) {
                    if (f.delete()) deleted++;
                    else System.err.println("Could not delete screenshot: " + f.getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("Error deleting file " + f.getAbsolutePath() + ": " + e.getMessage());
            }
        }
        System.out.println("✔ Old screenshots deleted. Count: " + deleted);
    }
}
