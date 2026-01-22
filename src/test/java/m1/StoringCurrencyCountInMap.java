package m1;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import listeners.RetryAnalyzer;
import pages.CurrencyTablePage;

public class StoringCurrencyCountInMap extends BaseTest {

	@Test
	public void printCurrencyCount() {

		CurrencyTablePage currencyPage = new CurrencyTablePage(getDriver());

		currencyPage.openPage();

		Map<String, Integer> currencyMap = currencyPage.getCurrencyCountMap();

		System.out.println("Currency count map:");
		System.out.println(currencyMap);
	}
}
