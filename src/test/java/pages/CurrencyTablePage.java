package pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.ScreenshotUtil;
import utils.WaitUtils;

public class CurrencyTablePage {
	private WebDriver driver;
	private WaitUtils wait;

	// ================== LOCATORS ==================
	private By euroCurrencyNames = By.xpath("//td[normalize-space()='Euro']");
	private By allCurrencyCells = By.xpath("//td[4]");
	private By tableRows = By.xpath("//table//tr");

	// ================== CONSTRUCTOR ==================
	public CurrencyTablePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WaitUtils(driver);// custom util initialised
	}

	// ================== ACTION METHODS ==================

	public void openPage() {
		driver.get("https://cosmocode.io/automation-practice-webtable/");
		wait.waitForElementVisible(tableRows); // ensure table is loaded
		ScreenshotUtil.capture(driver, "Checking for table visibility");
	}

	public List<WebElement> getEuroCurrencyElements() {
		wait.waitForElementVisible(euroCurrencyNames); //safety wait
		return driver.findElements(euroCurrencyNames);
	}

	public int getEuroCurrencyCount() {
		return getEuroCurrencyElements().size();
	}

	public void printEuroCurrencyNames() {
		for (WebElement euro : getEuroCurrencyElements()) {
			System.out.println(euro.getText());
		}
	}

	public Map<String, Integer> getCurrencyCountMap() {
		List<WebElement> currencies = driver.findElements(allCurrencyCells);
		Map<String, Integer> currencyMap = new HashMap<>();

		for (WebElement currency : currencies) {
			String currencyText = currency.getText();
			currencyMap.put(currencyText, currencyMap.getOrDefault(currencyText, 0) + 1);
		}
		return currencyMap;
	}

}
