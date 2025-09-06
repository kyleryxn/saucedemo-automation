package nuarch.saucedmeo.interview.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage<LoginPage> {
    private static final By USERNAME = By.cssSelector("[data-test='username']");
    private static final By PASSWORD = By.cssSelector("[data-test='password']");
    private static final By LOGIN_BTN = By.cssSelector("[data-test='login-button']");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver, PageFactory pages, String baseUrl) {
        super(driver, pages, baseUrl);
    }

    public LoginPage open() {
        driver.get(baseUrl);
        return waitForLoaded();
    }

    @Override
    public LoginPage waitForLoaded() {
        shouldBeVisible(USERNAME);
        shouldBeVisible(PASSWORD);
        shouldBeVisible(LOGIN_BTN);

        return this;
    }

    /** Navigates to InventoryPage on success */
    public InventoryPage login(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BTN);

        return pages.create(InventoryPage.class).waitForLoaded();
    }

    /** Stays on LoginPage (useful for negative tests) */
    public LoginPage attemptLogin(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BTN);

        return this;
    }

    public boolean isErrorVisible() {
        return exists(ERROR);
    }

    public String errorMessage() {
        return exists(ERROR) ? text(ERROR) : "";
    }

}