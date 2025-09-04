package nuarch.saucedmeo.interview.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage<CartPage> {
    private static final By CART_LIST = By.cssSelector(".cart_list");
    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By CHECKOUT_BTN = By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver, PageFactory pages) {
        super(driver, pages);
    }

    @Override
    public CartPage waitForLoaded() {
        shouldBeVisible(CART_LIST);
        return this;
    }

    public int itemCount() {
        return driver.findElements(CART_ITEMS).size();
    }

    public CheckoutPage proceedToCheckout() {
        click(CHECKOUT_BTN);
        return pages.create(CheckoutPage.class).waitForLoaded();
    }

}
