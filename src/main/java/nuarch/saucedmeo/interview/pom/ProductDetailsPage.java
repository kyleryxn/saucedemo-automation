package nuarch.saucedmeo.interview.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage<ProductDetailsPage> {
    private static final By DETAILS_CONTAINER = By.cssSelector(".inventory_details_desc_container");
    private static final By ADD_BTN = By.cssSelector("[data-test^='add-to-cart']");
    private static final By REMOVE_BTN = By.cssSelector("[data-test^='remove']");
    private static final By BACK_BTN = By.cssSelector("[data-test='back-to-products']");

    public ProductDetailsPage(WebDriver driver, PageFactory pages) {
        super(driver, pages);
    }

    @Override
    public ProductDetailsPage waitForLoaded() {
        shouldBeVisible(DETAILS_CONTAINER);
        return this;
    }

    public ProductDetailsPage addToCart() {
        click(ADD_BTN); return this;
    }

    public boolean isAdded() {
        return exists(REMOVE_BTN);
    }

    public InventoryPage backToProducts() {
        click(BACK_BTN);
        return pages.create(InventoryPage.class).waitForLoaded();
    }

}
