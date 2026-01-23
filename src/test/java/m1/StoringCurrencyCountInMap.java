package m1;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.CurrencyTablePage;

public class StoringCurrencyCountInMap extends BaseTest {

    @Test
    public void printCurrencyCount() {

        CurrencyTablePage currencyPage = new CurrencyTablePage(getDriver());
        currencyPage.openPage();

        Map<String, Integer> currencyMap = currencyPage.getCurrencyCountMap();

        System.out.println("Currency count map:");
        System.out.println(currencyMap);

        //  Find highest count
        int highestCount = currencyMap.values()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);

        //  Find second highest count
        int secondHighestCount = currencyMap.values()
                .stream()
                .filter(count -> count < highestCount)
                .max(Integer::compareTo)
                .orElse(0);

        // Get ALL highest currencies
        List<String> highestCurrencies = currencyMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == highestCount)
                .map(Map.Entry::getKey)
                .toList();

        //  Get ALL second highest currencies
        List<String> secondHighestCurrencies = currencyMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == secondHighestCount)
                .map(Map.Entry::getKey)
                .toList();

        // Print results
        System.out.println("\nHighest currency count: " + highestCount);
        highestCurrencies.forEach(currency ->
                System.out.println(currency + " = " + highestCount)
        );

        System.out.println("\nSecond highest currency count: " + secondHighestCount);
        secondHighestCurrencies.forEach(currency ->
                System.out.println(currency + " = " + secondHighestCount)
        );
    }
}
