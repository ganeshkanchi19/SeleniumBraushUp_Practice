package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScrollUtil {
	 /**
     * Scrolls down by given pixels
     */
    
    public static boolean scrollDown(WebDriver driver, int pixels) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long before = (Long) js.executeScript("return window.pageYOffset;");
        js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
        Long after = (Long) js.executeScript("return window.pageYOffset;");
        return !before.equals(after);
    }

    /**
     * Scrolls up by given pixels
     */
    public static void scrollUp(WebDriver driver, int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", -pixels);
    }

    /**
     * Scroll to a specific WebElement
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scroll to the bottom of the page
     */
    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Scroll to the top of the page
     */
    public static void scrollToTop(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Smooth scrolling effect - useful for demos
     */
    public static void smoothScroll(WebDriver driver, int pixels) {
        ((JavascriptExecutor) driver).executeScript(
            "window.scrollBy({ top: arguments[0], behavior: 'smooth' });", 
            pixels
        );
    }
}
