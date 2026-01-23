package m1;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CurrencyTablePage;

public class PrintingLangNames extends BaseTest {

	@Test
	public void printEuroNames() {

		CurrencyTablePage currencyPage = new CurrencyTablePage(getDriver());

		currencyPage.openPage();
		currencyPage.printGermanLangNames();

		System.out.println("Count of German words is : " + currencyPage.getGermanLangCount());
		System.out.println("Master branch change");
		Assert.assertEquals(currencyPage.getGermanLangCount(), 3);
		System.out.println("Feature branch change");
	}

}
