package utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import org.apache.commons.io.FileUtils;
import javax.imageio.ImageIO;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.HasFullPageScreenshot;

public class ScreenshotUtil {
	public static void capture(WebDriver driver, String fileName) {
		try {
			// Build destination file path
			File dest = new File(System.getProperty("user.dir") + "/Screenshots/" + fileName + ".png");

			// Create folder if missing
			dest.getParentFile().mkdirs();

			// Take screenshot
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			// Save file
			FileUtils.copyFile(src, dest);

			System.out.println("Screenshot saved at: " + dest.getAbsolutePath());

		} catch (Exception e) {
			System.err.println("Failed to capture screenshot: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
