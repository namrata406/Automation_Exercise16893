package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil ─────────────────────────────────────────────────────────────
 * Captures and saves full-page screenshots during test execution. Typically
 * invoked from TestListener on test failure.
 *
 * Screenshots are stored in: screenshots/<testName>_<timestamp>.png
 */

public class ScreenshotUtil {
	private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);

	private ScreenshotUtil() {
	}

	/**
	 * Capture a screenshot and save it to the screenshots directory.
	 *
	 * @param driver   active WebDriver instance
	 * @param testName test method name (used in file name)
	 * @return absolute file path of the saved screenshot (for report embedding)
	 */
	public static String capture(WebDriver driver, String testName) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String fileName = testName + "_" + timestamp + ".png";
		String dir = configReader.get("screenshot.path");
		String fullPath = dir + fileName;

		try {
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Path dest = Paths.get(fullPath);
			Files.createDirectories(dest.getParent());
			Files.copy(src.toPath(), dest);
			log.info("Screenshot saved → {}", fullPath);
		} catch (IOException e) {
			log.error("Failed to save screenshot for '{}': {}", testName, e.getMessage());
		}

		return new File(fullPath).getAbsolutePath();
	}

}
