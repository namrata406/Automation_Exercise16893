package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader ─────────────────────────────────────────────────────────────
 * Singleton utility class to read key-value pairs from: •
 * src/test/resources/config.properties (browser, URLs, timeouts) •
 * src/test/resources/testdata.properties (credentials, user data)
 *
 * System property (-Dkey=value) always overrides the file value, making CI/CD
 * parameter injection seamless.
 */

public class configReader {
	private static final Logger log = LogManager.getLogger(configReader.class);

	private static final String CONFIG_PATH = "src/test/resources/config.properties";
	private static final String TEST_DATA_PATH = "src/test/resources/testdata.properties";

	private static Properties config;
	private static Properties testData;

	// Static initialiser — load once on class load
	static {
		config = loadFile(CONFIG_PATH);
		testData = loadFile(TEST_DATA_PATH);
	}

	private configReader() {
	}

	// ─── Private helpers ──────────────────────────────────────────────────────

	private static Properties loadFile(String filePath) {
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(filePath)) {
			props.load(fis);
			log.info("Loaded properties file: {}", filePath);
		} catch (IOException e) {
			log.error("Cannot load properties file: {} | Error: {}", filePath, e.getMessage());
			throw new RuntimeException("Failed to load: " + filePath);
		}
		return props;
	}

	// ─── Public API ───────────────────────────────────────────────────────────

	/**
	 * Read a value from config.properties. System property (-Dkey=value) takes
	 * precedence.
	 */
	public static String get(String key) {
		String sysVal = System.getProperty(key);
		if (sysVal != null && !sysVal.trim().isEmpty())
			return sysVal.trim();

		String val = config.getProperty(key);
		if (val == null)
			throw new RuntimeException("Key '" + key + "' not found in config.properties");
		return val.trim();
	}

	/**
	 * Read a value from testdata.properties. System property (-Dkey=value) takes
	 * precedence.
	 */
	public static String getTestData(String key) {
		String sysVal = System.getProperty(key);
		if (sysVal != null && !sysVal.trim().isEmpty())
			return sysVal.trim();

		String val = testData.getProperty(key);
		if (val == null)
			throw new RuntimeException("Key '" + key + "' not found in testdata.properties");
		return val.trim();

	}
}
