package utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {
	private WebDriver driver;
	private WebDriverWait wait;

	// Default timeout constructor
	public WaitUtils(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// Custom timeout constructor
	public WaitUtils(WebDriver driver, int timeoutInSeconds) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
	}

	/* -------------------- ELEMENT WAITS -------------------- */

	public WebElement waitForElementVisible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public WebElement waitForElementClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public boolean waitForElementInvisible(By locator) {
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public WebElement waitForPresenceOfElement(By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/* -------------------- URL & TITLE WAITS -------------------- */

	public boolean waitForUrlContains(String fraction) {
		return wait.until(ExpectedConditions.urlContains(fraction));
	}

	public boolean waitForTitleContains(String title) {
		return wait.until(ExpectedConditions.titleContains(title));
	}

	/* -------------------- TEXT WAITS -------------------- */

	public boolean waitForTextToBe(By locator, String text) {
		return wait.until(ExpectedConditions.textToBe(locator, text));
	}

	/* -------------------- ALERT WAITS -------------------- */

	public void waitForAlertAndAccept() {
		wait.until(ExpectedConditions.alertIsPresent()).accept();
	}

	/* -------------------- FLUENT WAIT (ADVANCED) -------------------- */

	public WebElement fluentWaitForElement(By locator, int timeout, int polling) {

		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofSeconds(polling)).ignoring(Exception.class);
		return fluentWait.until(driver -> driver.findElement(locator));
	}
}
