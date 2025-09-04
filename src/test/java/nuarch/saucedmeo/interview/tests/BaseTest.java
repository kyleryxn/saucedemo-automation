package nuarch.saucedmeo.interview.tests;

import nuarch.saucedmeo.interview.model.Browser;
import nuarch.saucedmeo.interview.model.ConfigData;
import nuarch.saucedmeo.interview.pom.DefaultPageFactory;
import nuarch.saucedmeo.interview.pom.PageFactory;
import nuarch.saucedmeo.interview.testinfra.ScreenshotOnFailureExtension;
import nuarch.saucedmeo.interview.util.TestDataLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Locale;

@DisplayName("Base Test")
public abstract class BaseTest {
    protected WebDriver driver;
    protected String baseUrl = "https://www.saucedemo.com/";
    protected PageFactory pages;

    @RegisterExtension
    ScreenshotOnFailureExtension onFail = new ScreenshotOnFailureExtension(() -> driver);

    @BeforeEach
    void setUp() {
        // Load baseUrl from config.json if present
        try {
            ConfigData cfg = TestDataLoader.loadObjectFromResource("config.json", ConfigData.class);
            if (cfg != null && cfg.baseUrl() != null && !cfg.baseUrl().isBlank()) {
                baseUrl = cfg.baseUrl();
            }
        } catch (Exception ignored) {}

        Browser target = Browser.valueOf(System.getProperty("browser", "CHROME").toUpperCase(Locale.ROOT));
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        switch (target) {
            case CHROME -> startChrome(headless);
            case EDGE -> startEdge(headless);
            case FIREFOX-> startFirefox(headless);
            // Keep BRAVE opt-in (disabled unless a binary is provided)
            // If your enum includes BRAVE, you can support it like the below
            // case BRAVE  -> startBrave(headless);
            default -> startChrome(headless);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // explicit waits only
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();

        pages = new DefaultPageFactory(driver, baseUrl);
    }

    private void startChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--disable-save-password-bubble",
                "--disable-features=PasswordLeakDetection,PasswordManagerOnboarding,PasswordImport"
        );

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Selenium Manager auto-resolves chromedriver
        driver = new ChromeDriver(options);
    }

    private void startEdge(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments(
                "--disable-save-password-bubble",
                "--disable-features=PasswordLeakDetection,PasswordManagerOnboarding,PasswordImport"
        );

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Selenium Manager auto-resolves msedgedriver
        driver = new EdgeDriver(options);
    }

    private void startFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        }

        // Selenium Manager auto-resolves geckodriver
        driver = new FirefoxDriver(options);
    }

    // Optional Brave support (requires Brave binary path)
    @SuppressWarnings("unused")
    private void startBrave(boolean headless) {
        String braveBinary = System.getProperty("brave.binary", "");

        if (braveBinary.isBlank()) {
            throw new IllegalStateException("""
                Brave support requires -Dbrave.binary=/path/to/brave
                Use Chrome or Edge for the default run:
                  mvn -q test -Dbrowser=chrome
                  mvn -q test -Dbrowser=edge
                """);
        }
        ChromeOptions options = new ChromeOptions();
        options.setBinary(braveBinary);

        if (headless) {
            options.addArguments("--headless=new");
        }

        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

}
