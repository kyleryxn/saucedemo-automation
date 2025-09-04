# SauceDemo Automation Suite

This project is a JUnit 5 + Selenium automation suite for [saucedemo.com](https://www.saucedemo.com/).  
It demonstrates UI test automation practices using the Page Object Model (POM), parameterized tests, and browser configurability.

## ✅ Features
- **Positive flow**: Standard user can log in, add items, and successfully checkout.
- **Negative flow**: Invalid/locked out users cannot log in.
- **Buggy personas**: Covers SauceDemo’s special usernames (`problem_user`, `error_user`, etc.) to validate how the app behaves under known quirks.
- **Cross-browser support**: Run on Chrome, Firefox, or Edge via a simple `-Dbrowser` flag.
- **Headless support**: Toggle headless mode via `-Dheadless=true`.
- **Screenshots + console logs** are automatically saved when a test fails (`target/artifacts/...`).
- **HTML test report** is generated with Maven Surefire (`target/site/surefire-report.html`).


## 🧑‍💻 Project Structure
```
src/main/java/nuarch/saucedmeo/interview
 ├── model/           # Data models (Browser, ExpectedBug, ConfigData, LoginData)
 ├── pages/           # Page Object Model classes
 └── util/            # Utilities (TestDataLoader)

src/test/java/nuarch/saucedmeo/interview
 ├── tests/           # JUnit test classes
 └── testinfra/       # Test infrastructure (Screenshot extension)

src/test/resources/
 ├── login-credentials.json
 └── config.json
```

## 🚀 How to Run

### Run all tests (default Chrome)
```bash
mvn -q test
```

### Run in Firefox or Edge
```bash
mvn -q test -Dbrowser=firefox
mvn -q test -Dbrowser=edge
```

### Run headless
```bash
mvn -q test -Dheadless=true
```

### Generate and view HTML report
```bash
mvn -q test surefire-report:report
# Open target/site/surefire-report.html in your browser
```

### Run a single test class or method
```bash
mvn -q -Dtest=SuccessfulLoginTest test
mvn -q -Dtest=BuggyUsersTest#givenBuggyPersona_whenLogin_thenExpectedQuirkIsObserved test
```

## 👤 SauceDemo Test Personas

SauceDemo provides special usernames that each expose unique site behaviors.

| Username                | Behavior / Quirk |
|--------------------------|------------------|
| `standard_user`          | Normal user (happy path, stable) |
| `locked_out_user`        | Cannot log in (error shown) |
| `problem_user`           | Broken images, odd layout glitches |
| `performance_glitch_user`| Slow page loads (artificial delay) |
| `error_user`             | Cart icon gets `error` class (UI bug) |
| `visual_user`            | Visual bug (sorting dropdown missing/misaligned) |

> ⚠️ Each persona maps to **one fixed behavior**. There are no multiple “versions” (e.g. `visual_user1`, `visual_user2`).  
> Sometimes Sauce Labs updates the site, so quirks may temporarily not appear. Tests in this suite skip (rather than fail) when a quirk is not observed.


## 📂 Test Data

- `login-credentials.json` → Stores login accounts and validity flags.
- `config.json` → Stores the `baseUrl` (defaults to `https://www.saucedemo.com/` if missing).


## 📝 Notes

- This suite uses **Selenium Manager** (built into Selenium 4.6+) to resolve drivers automatically.
- No need to install ChromeDriver, EdgeDriver, or GeckoDriver manually.
- Screenshots and console logs are saved to `target/artifacts/<test-name>/` on failure.

## 📌 Requirements

- Java 21+
- Maven 3.9+
- Chrome/Firefox/Edge installed locally
