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
import org.openqa.selenium.Dimension;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.net.URL;
import java.net.MalformedURLException;
import java.time.Duration;

/**
 * DriverFactory: creates thread-local WebDriver instances optimized for local
 * and CI/Grid runs.
 *
 * Properties:
 *  - use.remote (boolean, default true)
 *  - remote.url  (string, default http://localhost:4444)
 *  - ci.headless (boolean, default true)   -> CI default
 *  - headless    (boolean, overrides ci.headless if present)
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DriverFactory.class);

    public static void initDriver(String browser) {
        if (tlDriver.get() != null) {
            log.debug("DriverFactory: driver already initialized for thread {}", Thread.currentThread().getId());
            return;
        }

        String remoteUrl = System.getProperty("remote.url", "http://localhost:4444");
        boolean useRemote = Boolean.parseBoolean(System.getProperty("use.remote", "true"));

        // Headless: allow -Dheadless to override -Dci.headless
        boolean ciHeadless = Boolean.parseBoolean(System.getProperty("ci.headless", "true"));
        boolean overrideHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        boolean headless = overrideHeadless || ciHeadless;

        log.debug("initDriver useRemote={}, remoteUrl={}, requestedBrowser={}, headless={}, thread={}",
                useRemote, remoteUrl, browser, headless, Thread.currentThread().getId());

        if (useRemote) {
            // Remote: try a few times (useful if Grid starts slowly)
            int attempts = 0;
            int maxAttempts = 3;
            while (attempts < maxAttempts) {
                attempts++;
                try {
                    URL gridUrl = new URL(remoteUrl);
                    switch (browser.toLowerCase()) {
                        case "chrome": {
                            ChromeOptions chromeOptions = createCiChromeOptions(headless);
                            chromeOptions.setCapability("browserName", "chrome");
                            tlDriver.set(new RemoteWebDriver(gridUrl, chromeOptions));
                            break;
                        }
                        case "firefox": {
                            FirefoxOptions ffOptions = createCiFirefoxOptions(headless);
                            ffOptions.setCapability("browserName", "firefox");
                            tlDriver.set(new RemoteWebDriver(gridUrl, ffOptions));
                            break;
                        }
                        default:
                            throw new IllegalArgumentException("Unsupported browser: " + browser);
                    }

                    // post-creation setup: timeouts and safe maximize
                    postCreationSetup();

                    // log capabilities (very useful to debug grid)
                    try {
                        WebDriver wd = tlDriver.get();
                        if (wd instanceof RemoteWebDriver) {
                            RemoteWebDriver rd = (RemoteWebDriver) wd;
                            Capabilities caps = rd.getCapabilities();
                            log.debug("created remote driver: browser={}, version={}, platform={}, session={}",
                                    caps.getBrowserName(), caps.getBrowserVersion(), caps.getPlatformName(),
                                    rd.getSessionId());
                        }
                    } catch (Throwable t) {
                        log.warn("DriverFactory: couldn't read capabilities after creation: {}", t.getMessage());
                    }

                    break; // success
                } catch (MalformedURLException mue) {
                    throw new RuntimeException("Invalid remote URL: " + remoteUrl, mue);
                } catch (Exception e) {
                    log.error("DriverFactory: attempt {} failed to create remote driver: {}", attempts, e.getMessage());
                    if (attempts >= maxAttempts) {
                        throw new RuntimeException("Failed to create remote driver after " + attempts + " attempts", e);
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        } else {
            // Local: auto-manage binaries and start local browsers
            switch (browser.toLowerCase()) {
                case "chrome": {
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = createCiChromeOptions(headless);
                    tlDriver.set(new ChromeDriver(options));
                    break;
                }
                case "firefox": {
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions options = createCiFirefoxOptions(headless);
                    tlDriver.set(new FirefoxDriver(options));
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            log.debug("DriverFactory: created local driver for thread={} browser={}", Thread.currentThread().getId(),
                    browser);

            // post-creation setup: timeouts and safe maximize
            postCreationSetup();
        }
    }

    private static void postCreationSetup() {
        try {
            WebDriver wd = tlDriver.get();
            if (wd == null) return;

            // small delay to let browser process start (reduces maximize timing issues)
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}

            // safe maximize with fallback to setSize
            try {
                wd.manage().window().maximize();
            } catch (Exception e) {
                try { wd.manage().window().setSize(new Dimension(1920, 1080)); } catch (Exception ignored) {}
            }

            // timeouts
            try {
                wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                wd.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            } catch (Exception ignored) {}

        } catch (Exception e) {
            log.warn("DriverFactory.postCreationSetup() failed: {}", e.getMessage());
        }
    }

    public static WebDriver getDriver() {
        WebDriver wd = tlDriver.get();
        if (wd == null) {
            log.error("DriverFactory.getDriver(): driver is null for thread {} — did you call initDriver(browser)?",
                    Thread.currentThread().getId());
        }
        return wd;
    }

    public static void removeDriver() {
        WebDriver wd = tlDriver.get();
        if (wd != null) {
            try {
                wd.quit();
            } catch (Exception e) {
                log.warn("DriverFactory.removeDriver() - error quitting driver: {}", e.getMessage());
            } finally {
                tlDriver.remove();
            }
        }
    }

    // ---------- helper methods for options ----------
    private static ChromeOptions createCiChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        // essential CI flags
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--remote-allow-origins=*"); // common for Chrome100+ remote
        options.addArguments("--window-size=1920,1080");

        if (headless) {
            // new headless when supported
            options.addArguments("--headless=new");
        }

        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        return options;
    }

    private static FirefoxOptions createCiFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        if (headless) {
            options.addArguments("--headless");
        }
        options.addPreference("intl.accept_languages", "en-US");
        return options;
    }
}
