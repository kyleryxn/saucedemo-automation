package nuarch.saucedmeo.interview.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage<T extends BasePage<T>> {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final PageFactory pages;   // DIP: factory rather than new
    protected final String baseUrl;

    protected BasePage(WebDriver driver, PageFactory pages) {
        this(driver, pages, null);
    }

    protected BasePage(WebDriver driver, PageFactory pages, String baseUrl) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.pages = pages;
        this.baseUrl = baseUrl;
    }

    public abstract T waitForLoaded();

    protected WebElement shouldBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        shouldBeVisible(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement el = shouldBeVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected boolean exists(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    protected String text(By locator) {
        return shouldBeVisible(locator).getText();
    }

}
