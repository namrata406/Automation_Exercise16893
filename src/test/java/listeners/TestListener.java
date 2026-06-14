package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;
import utils.DriverFactory;
import utils.ScreenshotUtil;
import utils.configReader;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestListener ─────────────────────────────────────────────────────────────
 * Implements ITestListener to hook into the TestNG lifecycle.
 *
 * Responsibilities: ✔ Generate ExtentReports HTML report ✔ Log PASS / FAIL /
 * SKIP with timestamps ✔ Capture and embed screenshot on every test FAILURE ✔
 * Flush and save report after the full suite completes
 */

public class TestListener implements ITestListener, ISuiteListener {
	private static final Logger log = LogManager.getLogger(TestListener.class);

	// ThreadLocal so parallel tests each own their ExtentTest node
	private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

	private static ExtentReports extent;

	// ══════════════════════════════════════════════════════════════════════════
	// Suite-level hooks
	// ══════════════════════════════════════════════════════════════════════════

	@Override
	public void onStart(ISuite suite) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String reportPath = configReader.get("report.path") + configReader.get("report.name") + "_" + timestamp
				+ ".html";

		ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
		sparkReporter.config().setTheme(Theme.DARK);
		sparkReporter.config().setDocumentTitle("AutomationExercise Test Report");
		sparkReporter.config().setReportName("Login & Registration Suite");
		sparkReporter.config().setTimeStampFormat("dd MMM yyyy HH:mm:ss");

		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "AutomationExercise.com");
		extent.setSystemInfo("Environment", System.getProperty("env", "QA"));
		extent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
		extent.setSystemInfo("Author", "QA Automation Engineer");

		log.info("ExtentReports initialised → {}", reportPath);
	}

	@Override
	public void onFinish(ISuite suite) {
		if (extent != null) {
			extent.flush();
			log.info("ExtentReports flushed and saved.");
		}
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Test-level hooks
	// ══════════════════════════════════════════════════════════════════════════

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String desc = result.getMethod().getDescription();

		log.info("▶ STARTED  : {}", testName);

		ExtentTest test = extent.createTest(testName, desc);
		extentTestThreadLocal.set(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		log.info("✅ PASSED   : {}", testName);
		getExtentTest().log(Status.PASS, "Test passed.");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		log.error("❌ FAILED   : {} | Reason: {}", testName, result.getThrowable().getMessage());

		ExtentTest test = getExtentTest();
		test.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());

		// Capture screenshot and embed in report
		boolean captureOnFailure = Boolean.parseBoolean(configReader.get("screenshot.on.failure"));
		if (captureOnFailure) {
			try {
				String screenshotPath = ScreenshotUtil.capture(DriverFactory.getDriver(), testName);
				test.addScreenCaptureFromPath(screenshotPath, testName + " - Failure Screenshot");
				log.info("Screenshot attached to report: {}", screenshotPath);
			} catch (Exception e) {
				log.warn("Could not capture screenshot: {}", e.getMessage());
			}
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		log.warn("⚠️  SKIPPED  : {}", testName);
		getExtentTest().log(Status.SKIP, "Test skipped: " + result.getThrowable().getMessage());
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Helper
	// ══════════════════════════════════════════════════════════════════════════

	private ExtentTest getExtentTest() {
		return extentTestThreadLocal.get();
	}

}
