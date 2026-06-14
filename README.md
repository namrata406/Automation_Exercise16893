# AutomationExercise Selenium POM Framework

> **Selenium 4 · TestNG · Maven · Java 11 · Page Object Model**

A production-grade UI test automation framework built for [AutomationExercise.com](https://automationexercise.com), demonstrating 2+ years of QA automation engineering experience.

---

## Tech Stack

| Tool / Library | Version | Purpose |
|---|---|---|
| Java | 11 | Core language |
| Selenium WebDriver | 4.18.1 | Browser automation |
| TestNG | 7.9.0 | Test runner & assertions |
| Maven | 3.x | Build & dependency management |
| WebDriverManager | 5.7.0 | Auto browser driver setup |
| ExtentReports | 5.1.1 | HTML test reporting |
| Log4j2 | 2.22.1 | Structured logging |
| JavaFaker | 1.0.2 | Dynamic test data |

---

## Project Structure

```
automationexercise-selenium-pom-framework
│
├── src
│   ├── main
│   │   └── java
│   │       ├── base
│   │       │   └── BasePage.java          # Selenium wrapper methods (click, type, wait…)
│   │       ├── pages
│   │       │   ├── HomePage.java          # Home page POM
│   │       │   └── LoginPage.java         # Login & Signup page POM
│   │       └── utils
│   │           ├── ConfigReader.java      # Reads config.properties & testdata.properties
│   │           ├── DriverFactory.java     # ThreadLocal WebDriver — parallel safe
│   │           └── ScreenshotUtil.java    # Captures failure screenshots
│   │
│   └── test
│       └── java
│           ├── tests
│           │   ├── LoginTest.java         # Valid login scenarios (smoke + regression)
│           │   └── InvalidLoginTest.java  # Negative login scenarios + DataProvider
│           └── listeners
│               └── TestListener.java      # ExtentReports + auto screenshot on failure
│
├── src/test/resources
│   ├── config.properties                  # URLs, browser, timeouts
│   └── testdata.properties               # Credentials & user data
│
├── screenshots/                           # Failure screenshots (auto-generated)
├── reports/                               # ExtentReports HTML (auto-generated)
├── test-output/                           # TestNG default output
│
├── pom.xml                                # Maven dependencies & plugins
├── testng.xml                             # Suite definition — smoke + regression
├── README.md
└── .gitignore
```

---

## Key Framework Features

- **Page Object Model** — clean separation of locators, actions, and test logic
- **ThreadLocal DriverFactory** — parallel-safe WebDriver management
- **Cross-browser support** — Chrome, Firefox, Edge via `WebDriverManager`
- **Dual property files** — `config.properties` (infra) + `testdata.properties` (data)
- **System property override** — `mvn test -Dbrowser=firefox -Denv=staging`
- **ExtentReports** — rich HTML reports with screenshots embedded on failure
- **Auto screenshot capture** — triggered by `TestListener` on any test failure
- **Data-Driven testing** — `@DataProvider` in `InvalidLoginTest`
- **Group-based execution** — `smoke` and `regression` groups via TestNG
- **Log4j2 logging** — structured logs per class for full traceability

---

## Running the Tests

### Prerequisites
- Java 11+
- Maven 3.6+
- Chrome / Firefox / Edge browser installed

### Run all tests (default browser: Chrome)
```bash
mvn test
```

### Run smoke tests only
```bash
mvn test -Dgroups=smoke
```

### Run on Firefox
```bash
mvn test -Dbrowser=firefox
```

### Run headless (CI/CD)
```bash
mvn test -Dheadless=true
```

### Run specific test class
```bash
mvn test -Dtest=LoginTest
```

---

## Test Cases

### LoginTest (7 test cases)
| ID | Description | Group |
|---|---|---|
| TC_LGN_001 | Verify login page URL | smoke |
| TC_LGN_002 | Verify Login & Signup headings displayed | smoke |
| TC_LGN_003 | Login with valid credentials | smoke, regression |
| TC_LGN_004 | Logged-in username matches expected | regression |
| TC_LGN_005 | Logout redirects to login page | smoke, regression |
| TC_LGN_006 | Login with blank email stays on page | regression |
| TC_LGN_007 | Login with blank password stays on page | regression |

### InvalidLoginTest (5 test cases + data-driven)
| ID | Description | Group |
|---|---|---|
| TC_INV_001 | Invalid email + invalid password → error shown | smoke, regression |
| TC_INV_002 | Valid email + wrong password → error shown | regression |
| TC_INV_003 | Unregistered email → error shown | regression |
| TC_INV_004 | Invalid email format → stays on page | regression |
| TC_INV_005 | Data-driven: 5 invalid combinations via @DataProvider | regression |

---

## Reports & Screenshots

After test execution:
- **HTML Report** → `reports/AutomationExercise_Report_<timestamp>.html`
- **Screenshots** → `screenshots/<testName>_<timestamp>.png` (failures only)

---

## Author

**QA Automation Engineer**  
2+ years of experience in Selenium, Java, TestNG, and Maven-based test frameworks.
