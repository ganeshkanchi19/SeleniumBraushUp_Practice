package base;

import org.testng.annotations.*;

import java.io.File;

import org.openqa.selenium.WebDriver;
import utils.DriverFactory;

public class BaseTest {
	
	 @BeforeSuite
	    public void clearScreenshotsFolder() {
	        File dir = new File(System.getProperty("user.dir"), "Screenshots");

	        if (dir.exists() && dir.isDirectory()) {
	            for (File f : dir.listFiles()) {
	                try {
	                    f.delete();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	            System.out.println("✔ Screenshots folder cleared before test suite run.");
	        }
	    }

    @BeforeMethod
    public void setup() {
        System.out.println(">>> Launching browser...");
        // Use DriverFactory to init the browser
        DriverFactory.initChromeDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        System.out.println(">>> Closing browser...");
        // Quit & remove the thread-local driver
        DriverFactory.removeDriver();
    }

    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
