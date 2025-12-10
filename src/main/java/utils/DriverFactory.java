package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.Capabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.net.URL;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Map;

/**
 * DriverFactory: creates thread-local WebDriver instances.
 *
 * - If system property "use.remote" is true (default true) it creates a RemoteWebDriver
 *   pointed at system property "remote.url" (default http://localhost:4444).
 * - If "use.remote" is false it creates a local ChromeDriver or FirefoxDriver and
 *   uses WebDriverManager to auto-download the correct driver binary for the local browser.
 *
 * Use system property "ci.headless=true" to force headless mode in CI runs.
 */
public class DriverFactory {

    // thread-local driver container
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DriverFactory.class);

    /**
     * Initialize a browser for the current thread.
     *
     * @param browser "chrome" or "firefox" (case-insensitive)
     */
    public static void initDriver(String browser) {
        if (tlDriver.get() != null) {
            System.out.println("DriverFactory: driver already initialized for thread " + Thread.currentThread().getId());
            return;
        }

        String remoteUrl = System.getProperty("remote.url", "http://localhost:4444");
        boolean useRemote = Boolean.parseBoolean(System.getProperty("use.remote", "true"));
        boolean ciHeadless = Boolean.parseBoolean(System.getProperty("ci.headless", "true")); // default true in CI
        log.debug("initDriver useRemote={}, remoteUrl={}, requestedBrowser={}, thread={}", useRemote, remoteUrl, browser, Thread.currentThread().getId());

        if (useRemote) {
            // existing RemoteWebDriver creation (unchanged)
            int attempts = 0;
            int maxAttempts = 3;
            while (attempts < maxAttempts) {
                attempts++;
                try {
                    URL gridUrl = new URL(remoteUrl);
                    switch (browser.toLowerCase()) {
                        case "chrome": {
                            ChromeOptions chromeOptions = new ChromeOptions();
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                            // Add remote-friendly options if desired
                            tlDriver.set(new RemoteWebDriver(gridUrl, chromeOptions));
                            break;
                        }
                        case "firefox":
                        case "mozilla": {
                            FirefoxOptions ffOptions = new FirefoxOptions();
                            ffOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                            tlDriver.set(new RemoteWebDriver(gridUrl, ffOptions));
                            break;
                        }
                        default:
                            throw new IllegalArgumentException("Unsupported browser: " + browser);
                    }

                    // after creation, log actual capabilities & session id (helpful for debugging)
                    try {
                        WebDriver wd = tlDriver.get();
                        if (wd instanceof RemoteWebDriver) {
                            RemoteWebDriver rd = (RemoteWebDriver) wd;
                            Capabilities caps = rd.getCapabilities();
                            Object sessionId = rd.getSessionId();
                            log.debug("created remote driver: browser={}, version={}, platform={}, session={}",
                                    caps.getBrowserName(), caps.getBrowserVersion(), caps.getPlatformName(), sessionId);
                        } else {
                            System.out.println("DriverFactory: created remote driver (non-RemoteWebDriver) for thread=" + Thread.currentThread().getId() + " requested=" + browser);
                        }
                    } catch (Throwable t) {
                        System.err.println("DriverFactory: couldn't read capabilities after creation: " + t.getMessage());
                    }

                    // set sensible defaults for this driver instance
                    try {
                        WebDriver wd = tlDriver.get();
                        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                        wd.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                    } catch (Exception ignored) {}

                    break; // success -> exit retry loop
                } catch (MalformedURLException mue) {
                    throw new RuntimeException("Invalid remote URL: " + remoteUrl, mue);
                } catch (Exception e) {
                    System.err.println("DriverFactory: attempt " + attempts + " failed to create remote driver: " + e.getMessage());
                    if (attempts >= maxAttempts) {
                        throw new RuntimeException("Failed to create remote driver after " + attempts + " attempts", e);
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ignored) {}
                }
            }
        } else {
            // Local driver fallback (auto-manage driver binaries using WebDriverManager)
            switch (browser.toLowerCase()) {
                case "chrome": {
                    // Download matching chromedriver if needed
                    WebDriverManager.chromedriver().setup();

                    ChromeOptions options = new ChromeOptions();
                    options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

                    // CI-friendly defaults — allow ci.headless property to control
                    if (ciHeadless) {
                        options.addArguments("--headless=new"); // selenium 4 new headless mode
                        options.addArguments("--disable-gpu");
                    }
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--window-size=1366,768");
                    options.addArguments("--disable-extensions");
                    options.addArguments("--disable-background-networking");
                    options.addArguments("--disable-renderer-backgrounding");

                    tlDriver.set(new ChromeDriver(options));
                    break;
                }
                case "firefox": {
                    // Download matching geckodriver if needed
                    WebDriverManager.firefoxdriver().setup();

                    FirefoxOptions options = new FirefoxOptions();
                    options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

                    if (ciHeadless) {
                        options.addArguments("--headless");
                    }
                    // window size is set via capabilities for firefox if needed
                    options.addPreference("intl.accept_languages", "en-US");

                    tlDriver.set(new FirefoxDriver(options));
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            System.out.println("DriverFactory: created local driver for thread=" + Thread.currentThread().getId() + " browser=" + browser);

            // set timeouts
            try {
                WebDriver wd = tlDriver.get();
                wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                wd.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            } catch (Exception ignored) {}
        }
    }

    public static WebDriver getDriver() {
        WebDriver wd = tlDriver.get();
        if (wd == null) {
            System.err.println("DriverFactory.getDriver(): driver is null for thread " + Thread.currentThread().getId() + " — did you call initDriver(browser)?");
        }
        return wd;
    }

    public static void removeDriver() {
        WebDriver wd = tlDriver.get();
        if (wd != null) {
            try {
                wd.quit();
            } catch (Exception e) {
                System.err.println("DriverFactory.removeDriver() - error quitting driver: " + e.getMessage());
            } finally {
                tlDriver.remove();
            }
        }
    }

    // Helper to print capability map concisely
    private static String capabilityMap(Capabilities caps) {
        try {
            Map<String, ?> map = caps.asMap();
            return map.toString();
        } catch (Exception e) {
            try {
                return "browserName=" + caps.getBrowserName();
            } catch (Exception ex) {
                return "unknownCaps";
            }
        }
    }
}
