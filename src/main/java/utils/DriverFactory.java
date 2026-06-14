package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory ─────────────────────────────────────────────────────────────
 * Manages WebDriver creation and lifecycle using ThreadLocal so parallel test
 * execution remains fully thread-safe.
 *
 * Usage: DriverFactory.initDriver("chrome"); WebDriver driver =
 * DriverFactory.getDriver(); DriverFactory.quitDriver();
 */

public class DriverFactory {
	private static final Logger log = LogManager.getLogger(DriverFactory.class);

	// ThreadLocal ensures each thread (parallel test) gets its own driver
	private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

	private DriverFactory() {
		// Utility class — no instantiation
	}

	/**
	 * Initialise and store a new WebDriver for the current thread.
	 *
	 * @param browser browser name: chrome | firefox | edge
	 */
	public static void initDriver(String browser) {
		String resolvedBrowser = System.getProperty("browser", browser).toLowerCase();
		log.info("Initialising browser: {}", resolvedBrowser);

		boolean headless = Boolean.parseBoolean(configReader.get("headless"));
		WebDriver driver;

		switch (resolvedBrowser) {

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions ffOptions = new FirefoxOptions();
			if (headless)
				ffOptions.addArguments("--headless");
			driver = new FirefoxDriver(ffOptions);
			break;

		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;

		case "chrome":
		default:
			WebDriverManager.chromedriver().setup();
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--disable-notifications");
			chromeOptions.addArguments("--disable-popup-blocking");
			if (headless) {
				chromeOptions.addArguments("--headless=new");
				chromeOptions.addArguments("--no-sandbox");
				chromeOptions.addArguments("--disable-dev-shm-usage");
			}
			driver = new ChromeDriver(chromeOptions);
			break;
		}

		// Global timeouts
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Long.parseLong(configReader.get("implicit.wait"))));
		driver.manage().timeouts()
				.pageLoadTimeout(Duration.ofSeconds(Long.parseLong(configReader.get("page.load.timeout"))));

		driverThreadLocal.set(driver);
		log.info("WebDriver initialised successfully for thread: {}", Thread.currentThread().getId());
	}

	/**
	 * Retrieve the WebDriver for the current thread.
	 *
	 * @return WebDriver instance
	 */
	public static WebDriver getDriver() {
		WebDriver driver = driverThreadLocal.get();
		if (driver == null) {
			throw new IllegalStateException("WebDriver is null. Call DriverFactory.initDriver() before getDriver().");
		}
		return driver;
	}

	/**
	 * Quit the browser and remove the driver from ThreadLocal.
	 */
	public static void quitDriver() {
		WebDriver driver = driverThreadLocal.get();
		if (driver != null) {
			driver.quit();
			driverThreadLocal.remove();
			log.info("WebDriver quit and removed from ThreadLocal.");
		}
	}

}
