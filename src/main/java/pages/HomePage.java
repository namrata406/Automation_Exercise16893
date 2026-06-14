package pages;

import base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * HomePage ───────────────────────────────────────────────────────────── Page
 * Object for: https://automationexercise.com
 *
 * Used to verify post-login state and perform top-level navigation.
 */

public class HomePage extends BasePage {
	private static final Logger log = LogManager.getLogger(HomePage.class);

	// ══════════════════════════════════════════════════════════════════════════
	// Locators
	// ══════════════════════════════════════════════════════════════════════════

	@FindBy(xpath = "//a[contains(text(),' Logged in as')]")
	private WebElement loggedInAsText;

	@FindBy(xpath = "//a[@href='/logout']")
	private WebElement logoutLink;

	@FindBy(xpath = "//a[@href='/login']")
	private WebElement signupLoginNavLink;

	@FindBy(xpath = "//a[@href='/delete_account']")
	private WebElement deleteAccountLink;

	@FindBy(xpath = "//h2[@data-qa='account-deleted']")
	private WebElement accountDeletedMessage;

	@FindBy(xpath = "//a[@data-qa='continue-button']")
	private WebElement continueButton;

	@FindBy(id = "slider-carousel")
	private WebElement heroCarousel;

	// ══════════════════════════════════════════════════════════════════════════
	// Constructor
	// ══════════════════════════════════════════════════════════════════════════

	public HomePage(WebDriver driver) {
		super(driver);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Actions
	// ══════════════════════════════════════════════════════════════════════════

	public LoginPage goToLogin() {
		log.info("Clicking Signup / Login nav link.");
		click(signupLoginNavLink);
		return new LoginPage(driver);
	}

	public void clickLogout() {
		log.info("Logging out from HomePage.");
		click(logoutLink);
	}

	public void deleteAccount() {
		log.info("Clicking Delete Account.");
		click(deleteAccountLink);
	}

	public void clickContinueAfterDelete() {
		click(continueButton);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Verifications
	// ══════════════════════════════════════════════════════════════════════════

	public boolean isHomePageDisplayed() {
		return isDisplayed(heroCarousel);
	}

	public boolean isUserLoggedIn() {
		return isDisplayed(loggedInAsText);
	}

	public String getLoggedInUsername() {
		String full = getText(loggedInAsText);
		return full.contains("as ") ? full.split("as ")[1].trim() : full;
	}

	public boolean isAccountDeletedMessageDisplayed() {
		return isDisplayed(accountDeletedMessage);
	}

	public String getAccountDeletedMessage() {
		return getText(accountDeletedMessage);
	}

}
