package pages;

import base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
	private static final Logger log = LogManager.getLogger(LoginPage.class);

	// ══════════════════════════════════════════════════════════════════════════
	// Login Form Locators
	// ══════════════════════════════════════════════════════════════════════════

	@FindBy(xpath = "//div[@class='login-form']//h2")
	private WebElement loginHeading;

	@FindBy(xpath = "//input[@data-qa='login-email']")
	private WebElement emailInput;

	@FindBy(xpath = "//input[@data-qa='login-password']")
	private WebElement passwordInput;

	@FindBy(xpath = "//button[@data-qa='login-button']")
	private WebElement loginButton;

	@FindBy(xpath = "//p[contains(text(),'Your email or password is incorrect')]")
	private WebElement invalidCredentialsError;

	// ══════════════════════════════════════════════════════════════════════════
	// Signup Form Locators
	// ══════════════════════════════════════════════════════════════════════════

	@FindBy(xpath = "//div[@class='signup-form']//h2")
	private WebElement signupHeading;

	@FindBy(xpath = "//input[@data-qa='signup-name']")
	private WebElement signupNameInput;

	@FindBy(xpath = "//input[@data-qa='signup-email']")
	private WebElement signupEmailInput;

	@FindBy(xpath = "//button[@data-qa='signup-button']")
	private WebElement signupButton;

	@FindBy(xpath = "//p[contains(text(),'Email Address already exist')]")
	private WebElement emailAlreadyExistsError;

	// ══════════════════════════════════════════════════════════════════════════
	// Navbar Locators (post-login verification)
	// ══════════════════════════════════════════════════════════════════════════

	@FindBy(xpath = "//a[contains(text(),' Logged in as')]")
	private WebElement loggedInAsText;

	@FindBy(xpath = "//a[@href='/logout']")
	private WebElement logoutLink;

	// ══════════════════════════════════════════════════════════════════════════
	// Constructor
	// ══════════════════════════════════════════════════════════════════════════

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Actions — Login Form
	// ══════════════════════════════════════════════════════════════════════════

	/**
	 * Enter credentials and submit the login form.
	 *
	 * @param email    user email address
	 * @param password user password
	 * @return this LoginPage (for fluent assertions or chained calls)
	 */
	public LoginPage enterEmail(String email) {
		log.info("Entering login email: {}", email);
		type(emailInput, email);
		return this;
	}

	public LoginPage enterPassword(String password) {
		log.info("Entering login password.");
		type(passwordInput, password);
		return this;
	}

	public LoginPage clickLoginButton() {
		log.info("Clicking Login button.");
		click(loginButton);
		return this;
	}

	/**
	 * One-step login helper — enters email + password then clicks Login.
	 */
	public LoginPage login(String email, String password) {
		return enterEmail(email).enterPassword(password).clickLoginButton();
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Actions — Signup Form
	// ══════════════════════════════════════════════════════════════════════════

	/**
	 * Enter name + email and click Signup → navigates to account registration form.
	 *
	 * @param name  display name for the new account
	 * @param email email address for the new account
	 * @return new HomePage instance (signup redirects to home when successful)
	 */
	public LoginPage enterSignupName(String name) {
		log.info("Entering signup name: {}", name);
		type(signupNameInput, name);
		return this;
	}

	public LoginPage enterSignupEmail(String email) {
		log.info("Entering signup email: {}", email);
		type(signupEmailInput, email);
		return this;
	}

	public void clickSignupButton() {
		log.info("Clicking Signup button.");
		click(signupButton);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Actions — Navbar
	// ══════════════════════════════════════════════════════════════════════════

	public void clickLogout() {
		log.info("Clicking Logout.");
		click(logoutLink);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Verifications / Getters
	// ══════════════════════════════════════════════════════════════════════════

	public boolean isLoginHeadingDisplayed() {
		return isDisplayed(loginHeading);
	}

	public boolean isSignupHeadingDisplayed() {
		return isDisplayed(signupHeading);
	}

	public boolean isInvalidCredentialsErrorShown() {
		return isDisplayed(invalidCredentialsError);
	}

	public boolean isEmailAlreadyExistsErrorShown() {
		return isDisplayed(emailAlreadyExistsError);
	}

	public boolean isUserLoggedIn() {
		return isDisplayed(loggedInAsText);
	}

	/**
	 * Extract the username from navbar text " Logged in as <name>".
	 */
	public String getLoggedInUsername() {
		String full = getText(loggedInAsText);
		return full.contains("as ") ? full.split("as ")[1].trim() : full;
	}

	public String getLoginHeadingText() {
		return getText(loginHeading);
	}

	public String getSignupHeadingText() {
		return getText(signupHeading);
	}

}
