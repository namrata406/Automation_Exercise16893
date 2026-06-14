package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.DriverFactory;
import utils.configReader;

/**
 * LoginTest ─────────────────────────────────────────────────────────────
 * Covers all VALID login scenarios for AutomationExercise.com.
 *
 * Test Cases: TC_LGN_001 — Verify login page URL TC_LGN_002 — Verify Login and
 * Signup headings are visible TC_LGN_003 — Login with valid credentials → user
 * is logged in TC_LGN_004 — Logged-in username matches expected value
 * TC_LGN_005 — Logout redirects back to login page TC_LGN_006 — Login with
 * blank email stays on login page TC_LGN_007 — Login with blank password stays
 * on login page
 */

public class LoginTest {
	private LoginPage loginPage;

	// ══════════════════════════════════════════════════════════════════════════
	// Setup / Teardown (DriverFactory manages ThreadLocal WebDriver)
	// ══════════════════════════════════════════════════════════════════════════

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		String browser = System.getProperty("browser", configReader.get("browser"));
		DriverFactory.initDriver(browser);
		DriverFactory.getDriver().get(configReader.get("login.url"));
		loginPage = new LoginPage(DriverFactory.getDriver());
	}

	@org.testng.annotations.AfterMethod(alwaysRun = true)
	public void tearDown() {
		DriverFactory.quitDriver();
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_LGN_001
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "smoke" }, description = "Verify login page URL contains 'login'")
	public void TC_LGN_001_verifyLoginPageURL() {
		String url = loginPage.getCurrentUrl();
		Assert.assertTrue(url.contains("login"), "Expected URL to contain 'login'. Actual: " + url);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_LGN_002
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "smoke" }, description = "Verify Login and New User Signup headings are displayed")
	public void TC_LGN_002_verifyPageHeadingsDisplayed() {
		Assert.assertTrue(loginPage.isLoginHeadingDisplayed(), "'Login to your account' heading not visible.");
		Assert.assertTrue(loginPage.isSignupHeadingDisplayed(), "'New User Signup!' heading not visible.");
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_LGN_003
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "smoke", "regression" }, description = "Login with valid registered credentials succeeds")
	public void TC_LGN_003_loginWithValidCredentials() {
		loginPage.login(configReader.getTestData("valid.email"), configReader.getTestData("valid.password"));

		Assert.assertTrue(loginPage.isUserLoggedIn(), "User is NOT logged in after valid credentials.");
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_LGN_004
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "regression" }, description = "Logged-in username in navbar matches expected name")
	public void TC_LGN_004_verifyLoggedInUsername() {
		loginPage.login(configReader.getTestData("valid.email"), configReader.getTestData("valid.password"));

		String actual = loginPage.getLoggedInUsername();
		String expected = configReader.getTestData("valid.username");

		Assert.assertEquals(actual, expected,
				"Logged-in username mismatch. Expected: " + expected + " | Actual: " + actual);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_LGN_005
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "smoke", "regression" }, description = "Clicking Logout redirects user back to the login page")
	public void TC_LGN_005_logoutRedirectsToLoginPage() {
		loginPage.login(configReader.getTestData("valid.email"), configReader.getTestData("valid.password"));
		Assert.assertTrue(loginPage.isUserLoggedIn(), "Pre-condition: user must be logged in first.");

		loginPage.clickLogout();

		Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
				"After logout, URL should redirect to login page.");
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_LGN_006
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "regression" }, description = "Login with blank email — browser validation keeps user on page")
	public void TC_LGN_006_loginWithBlankEmail() {
		loginPage.login("", configReader.getTestData("valid.password"));

		Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
				"Should remain on login page when email is blank.");
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_LGN_007
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "regression" }, description = "Login with blank password — browser validation keeps user on page")
	public void TC_LGN_007_loginWithBlankPassword() {
		loginPage.login(configReader.getTestData("valid.email"), "");

		Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
				"Should remain on login page when password is blank.");
	}

}
