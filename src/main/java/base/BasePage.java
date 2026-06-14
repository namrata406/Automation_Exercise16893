package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.configReader;
import java.time.Duration;

/**
 * BasePage ───────────────────────────────────────────────────────────── Parent
 * class for all Page Object classes.
 *
 * Provides: • Explicit-wait wrappers (click, type, getText, …) • Select /
 * dropdown helpers • JavaScript utilities (scroll, jsClick) • Visibility &
 * state checks • Alert handling
 *
 * Every page class extends BasePage and calls super(driver) which triggers
 * PageFactory.initElements().
 */

public class BasePage {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected Actions actions;

	private static final Logger log = LogManager.getLogger(BasePage.class);

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.get("explicit.wait"))));
		this.actions = new Actions(driver);
		PageFactory.initElements(driver, this);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Navigation
	// ══════════════════════════════════════════════════════════════════════════

	public void navigateTo(String url) {
		log.info("Navigating to: {}", url);
		driver.get(url);
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Core Element Interactions
	// ══════════════════════════════════════════════════════════════════════════

	public void click(WebElement element) {
		waitForClickable(element);
		log.debug("Clicking: {}", element);
		element.click();
	}

	public void jsClick(WebElement element) {
		log.debug("JS click: {}", element);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public void type(WebElement element, String text) {
		waitForVisible(element);
		log.debug("Typing '{}' into: {}", text, element);
		element.clear();
		element.sendKeys(text);
	}

	public String getText(WebElement element) {
		waitForVisible(element);
		return element.getText().trim();
	}

	public String getAttribute(WebElement element, String attr) {
		waitForVisible(element);
		return element.getAttribute(attr);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Dropdown / Select
	// ══════════════════════════════════════════════════════════════════════════

	public void selectByVisibleText(WebElement element, String text) {
		waitForVisible(element);
		new Select(element).selectByVisibleText(text);
	}

	public void selectByValue(WebElement element, String value) {
		waitForVisible(element);
		new Select(element).selectByValue(value);
	}

	public void selectByIndex(WebElement element, int index) {
		waitForVisible(element);
		new Select(element).selectByIndex(index);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Waits
	// ══════════════════════════════════════════════════════════════════════════

	public WebElement waitForVisible(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	public WebElement waitForClickable(WebElement element) {
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public boolean waitForUrlContains(String fragment) {
		return wait.until(ExpectedConditions.urlContains(fragment));
	}

	public boolean waitForTextInElement(WebElement element, String text) {
		return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Visibility / State Checks
	// ══════════════════════════════════════════════════════════════════════════

	public boolean isDisplayed(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (NoSuchElementException | StaleElementReferenceException e) {
			return false;
		}
	}

	public boolean isEnabled(WebElement element) {
		try {
			return element.isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	// ══════════════════════════════════════════════════════════════════════════
	// JavaScript Utilities
	// ══════════════════════════════════════════════════════════════════════════

	public void scrollIntoView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});",
				element);
	}

	public void scrollToBottom() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	public void highlightElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].style.border='3px solid red'", element);
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Hover
	// ══════════════════════════════════════════════════════════════════════════

	public void hoverOver(WebElement element) {
		waitForVisible(element);
		actions.moveToElement(element).perform();
	}

	// ══════════════════════════════════════════════════════════════════════════
	// Alert Handling
	// ══════════════════════════════════════════════════════════════════════════

	public String getAlertText() {
		wait.until(ExpectedConditions.alertIsPresent());
		return driver.switchTo().alert().getText();
	}

	public void acceptAlert() {
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();
	}

	public void dismissAlert() {
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().dismiss();
	}

}
