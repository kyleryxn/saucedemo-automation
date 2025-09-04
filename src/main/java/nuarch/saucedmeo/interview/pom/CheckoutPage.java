package nuarch.saucedmeo.interview.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage<CheckoutPage> implements CompletableCheckout {
    private static final By FIRST = By.cssSelector("[data-test='firstName']");
    private static final By LAST = By.cssSelector("[data-test='lastName']");
    private static final By ZIP = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE = By.cssSelector("[data-test='continue']");
    private static final By SUMMARY = By.cssSelector(".summary_info");
    private static final By FINISH = By.cssSelector("[data-test='finish']");
    private static final By COMPLETE = By.cssSelector("[data-test='complete-header'], .complete-header");

    public CheckoutPage(WebDriver driver, PageFactory pages) {
        super(driver, pages);
    }

    @Override
    public CheckoutPage waitForLoaded() {
        shouldBeVisible(FIRST);
        shouldBeVisible(LAST);
        shouldBeVisible(ZIP);
        return this;
    }

    @Override
    public CheckoutPage fillStepOne(String first, String last, String zip) {
        type(FIRST, first);
        type(LAST, last);
        type(ZIP, zip);
        click(CONTINUE);
        shouldBeVisible(SUMMARY);

        return this;
    }

    @Override
    public CheckoutPage finish() {
        click(FINISH);
        return this;
    }

    @Override
    public boolean isComplete() {
        return exists(COMPLETE);
    }

}
