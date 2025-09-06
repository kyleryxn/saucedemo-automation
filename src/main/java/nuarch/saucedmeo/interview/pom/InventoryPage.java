package nuarch.saucedmeo.interview.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage<InventoryPage> implements HasCart, HasSorting {
    private static final By INVENTORY_CONTAINER = By.cssSelector("#inventory_container");
    private static final By ITEM_NAME_ALL = By.cssSelector(".inventory_item_name");
    private static final By CART_ICON = By.cssSelector(".shopping_cart_link");
    private static final By SORT_SELECT = By.cssSelector("[data-test='product_sort_container']");
    private static final By PRODUCT_IMAGES = By.cssSelector(".inventory_item img");
    private static final By PRICE_ALL = By.cssSelector(".inventory_item_price");

    public InventoryPage(WebDriver driver, PageFactory pages) {
        super(driver, pages);
    }

    @Override
    public InventoryPage waitForLoaded() {
        shouldBeVisible(INVENTORY_CONTAINER);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(ITEM_NAME_ALL, 0));

        return this;
    }

    public ProductDetailsPage openDetails(String productName) {
        driver.findElements(ITEM_NAME_ALL).stream()
                .filter(el -> productName.equalsIgnoreCase(el.getText().trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Product not found: " + productName))
                .click();

        return pages.create(ProductDetailsPage.class).waitForLoaded();
    }

    @Override
    public CartPage openCart() {
        click(CART_ICON);
        return pages.create(CartPage.class).waitForLoaded();
    }

    @Override
    public int itemCount() {
        return pages.create(CartPage.class).itemCount();
    }

    @Override
    public boolean hasSortingControl() {
        return exists(SORT_SELECT);
    }

    public boolean hasCartAreaErrorClass() {
        WebElement cart = driver.findElement(CART_ICON);
        String klass = cart.getAttribute("class");

        return klass != null && klass.contains("error");
    }

    public String cartLinkClasses() {
        WebElement cart = driver.findElement(CART_ICON);
        String klass = cart.getAttribute("class");

        return klass == null ? "" : klass;
    }

    public boolean isCartVisuallyMisaligned() {

        // Compare vertical alignment of the cart icon vs. the header container
        Long diff = (Long) ((JavascriptExecutor) driver).executeScript(
                "const cart = document.querySelector('.shopping_cart_link');" +
                        "const header = document.querySelector('.primary_header');" +
                        "if (!cart || !header) return 0;" +
                        "const ct = cart.getBoundingClientRect().top;" +
                        "const ht = header.getBoundingClientRect().top;" +
                        "return Math.abs(ct - ht) > 8 ? 1 : 0;");

        return diff != null && diff == 1L;
    }

    public List<String> imageSrcs() {
        return driver.findElements(PRODUCT_IMAGES).stream()
                .map(el -> el.getAttribute("src"))
                .filter(src -> src != null && !src.isBlank())
                .collect(Collectors.toList());
    }

    public boolean selectSortLowToHigh(Duration timeout) {
        long end = System.nanoTime() + timeout.toNanos();
        while (System.nanoTime() < end) {
            try {
                WebElement el = shouldBeVisible(SORT_SELECT);
                new Select(el).selectByValue("lohi"); // SauceDemo uses values: az, za, lohi, hilo
                return true;
            } catch (Exception ignored) {
                try { Thread.sleep(100); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        return false;
    }

    public List<Double> prices() {
        return driver.findElements(PRICE_ALL).stream()
                .map(e -> e.getText().replace("$", "").trim())
                .filter(s -> !s.isBlank())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    public boolean isSortedAsc(List<Double> nums) {
        for (int i = 1; i < nums.size(); i++) {
            if (nums.get(i) < nums.get(i - 1)) return false;
        }
        return true;
    }

}
