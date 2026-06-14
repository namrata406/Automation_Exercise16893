package tests;

import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import utils.DriverFactory;
import utils.configReader;

/**
 * InvalidLoginTest
 * ───────────────────────────────────────────────────────────── Covers all
 * NEGATIVE / boundary login scenarios.
 *
 * Test Cases: TC_INV_001 — Login with invalid email + invalid password
 * TC_INV_002 — Login with valid email + wrong password TC_INV_003 — Login with
 * unregistered email TC_INV_004 — Login with invalid email format (no @)
 * TC_INV_005 — Data-driven: multiple invalid combinations via @DataProvider
 */

public class InvalidLoginTest {
	private LoginPage loginPage;

	// ══════════════════════════════════════════════════════════════════════════
	// Setup / Teardown
	// ══════════════════════════════════════════════════════════════════════════

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		String browser = System.getProperty("browser", configReader.get("browser"));
		DriverFactory.initDriver(browser);
		DriverFactory.getDriver().get(configReader.get("login.url"));
		loginPage = new LoginPage(DriverFactory.getDriver());
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		DriverFactory.quitDriver();
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_INV_001
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "smoke",
			"regression" }, description = "Login with completely invalid email and password shows error")
	public void TC_INV_001_loginWithInvalidEmailAndPassword() {
		loginPage.login(configReader.getTestData("invalid.email"), configReader.getTestData("invalid.password"));

		Assert.assertTrue(loginPage.isInvalidCredentialsErrorShown(),
				"Error message NOT shown for invalid email + password.");
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_INV_002
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "regression" }, description = "Login with valid email but wrong password shows error")
	public void TC_INV_002_loginWithValidEmailWrongPassword() {
		loginPage.login(configReader.getTestData("valid.email"), configReader.getTestData("invalid.password"));

		Assert.assertTrue(loginPage.isInvalidCredentialsErrorShown(),
				"Error message NOT shown when valid email + wrong password is used.");
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_INV_003
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "regression" }, description = "Login with unregistered email shows error")
	public void TC_INV_003_loginWithUnregisteredEmail() {
		loginPage.login("notregistered@notexist.com", "AnyPassword@123");

		Assert.assertTrue(loginPage.isInvalidCredentialsErrorShown(),
				"Error message NOT shown for unregistered email.");
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_INV_004
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = { "regression" }, description = "Login with invalid email format (missing @) stays on page")
	public void TC_INV_004_loginWithInvalidEmailFormat() {
		loginPage.login(configReader.getTestData("wrong.email.format"), configReader.getTestData("valid.password"));

		// Browser-level HTML5 validation prevents form submission
		Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
				"Should stay on login page with malformed email format.");
	}

	// ══════════════════════════════════════════════════════════════════════════
	// TC_INV_005 — Data-Driven
	// ══════════════════════════════════════════════════════════════════════════

	@Test(groups = {
			"regression" }, description = "Data-driven: multiple invalid credential combinations", dataProvider = "invalidCredentials")
	public void TC_INV_005_dataDrivenInvalidLogins(String email, String password, String scenario) {
		loginPage.login(email, password);

		Assert.assertTrue(loginPage.isInvalidCredentialsErrorShown(),
				"Error message NOT shown for scenario → " + scenario);
	}

	/**
	 * DataProvider feeding invalid login combinations. Column order: email |
	 * password | scenario description
	 */
	@DataProvider(name = "invalidCredentials", parallel = false)
	public Object[][] invalidCredentials() {
		return new Object[][] { { "fakeuser1@test.com", "WrongPass@1", "Non-existent user #1" },
				{ "fakeuser2@test.com", "WrongPass@2", "Non-existent user #2" },
				{ "testuser@example.com", "BadPassword1", "Valid email, wrong password (lower case)" },
				{ "testuser@example.com", "TEST@1234", "Valid email, wrong password (upper case)" },
				{ "another@random.io", "P@ssw0rd99", "Completely random non-existent account" }, };
	}

}
