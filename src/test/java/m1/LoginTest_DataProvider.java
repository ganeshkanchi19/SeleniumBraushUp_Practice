package m1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import base.BaseTest;
import utils.ScreenshotUtil;

public class LoginTest_DataProvider extends BaseTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;
	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(CheckBoxTest.class);

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		// obtain the driver created by BaseTest/DriverFactory
		driver = getDriver();
		if (driver == null) {
			throw new IllegalStateException("Driver is null — BaseTest did not initialize it!");
		}

		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		} catch (Exception ignored) {
		}

		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}

		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	@Test(dataProvider = "loginData", dataProviderClass = LoginDataProvider.class)
	public void loginTest(String username, String password) {
		log.info("Starting the test for parameterization using dataprovider");
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);

		// Navigating to URL
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

		WebElement userNameField = driver.findElement(By.name("username"));
		WebElement passwordField = driver.findElement(By.name("password"));
		WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit']"));

		userNameField.sendKeys(username);
		passwordField.sendKeys(password);
		loginBtn.click();
		if (username.equals("Admin") && password.equals("admin123")) {
			// ✅ WAIT FOR DASHBOARD (CRITICAL FIX)
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='oxd-userdropdown-name']")));

			ScreenshotUtil.capture(driver, "Valid_Login_" + username);
			Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"), "Valid login failed!");

			// Screenshot for every iteration
			ScreenshotUtil.capture(driver, "Login_" + username);
		} else {
			WebElement errorMsg = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//p[contains(@class,'oxd-alert-content-text')]")));

			ScreenshotUtil.capture(driver, "Invalid_Login_" + username);
			Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed for invalid login!");
		}

	}
}
