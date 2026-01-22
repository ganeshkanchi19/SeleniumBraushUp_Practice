package m1;

import org.testng.Assert;
import org.testng.annotations.Test;



import base.BaseTest;
import pages.CurrencyTablePage;

public class PrintingEuroCurrencyNames extends BaseTest {

	@Test
	public void printEuroNames() {

		CurrencyTablePage currencyPage = new CurrencyTablePage(getDriver());

		currencyPage.openPage();
		currencyPage.printEuroCurrencyNames();

		System.out.println("Count of Euro words is : " + currencyPage.getEuroCurrencyCount());
		Assert.assertEquals(currencyPage.getEuroCurrencyCount(), 21);
	}

}
