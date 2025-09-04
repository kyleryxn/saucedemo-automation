package nuarch.saucedmeo.interview.pom;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public boolean isAt() {
        return exists(INVENTORY_CONTAINER);
    }

    public boolean hasCartAreaErrorClass() {
        WebElement cart = driver.findElement(CART_ICON);
        String klass = cart.getAttribute("class");

        return klass != null && klass.contains("error");
    }

    /** Old suite compatibility: return header/cart link classes for debugging. */
    public String cartLinkClasses() {
        WebElement cart = driver.findElement(CART_ICON);
        String klass = cart.getAttribute("class");

        return klass == null ? "" : klass;
    }

    /** Heuristic visual misalignment check on cart icon vs header bar. */
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

    /** Collect product image srcs on inventory. */
    public List<String> imageSrcs() {
        return driver.findElements(PRODUCT_IMAGES).stream()
                .map(el -> el.getAttribute("src"))
                .filter(src -> src != null && !src.isBlank())
                .collect(Collectors.toList());
    }

    /** Try selecting a sort option by visible text within a timeout; returns true if applied. */
    public boolean trySelectSortOptionByText(String visibleText, Duration timeout) {
        long end = System.nanoTime() + timeout.toNanos();

        while (System.nanoTime() < end) {
            try {
                WebElement selEl = shouldBeVisible(SORT_SELECT);
                Select select = new Select(selEl);
                select.selectByVisibleText(visibleText);

                // Verify selection applied
                if (select.getFirstSelectedOption().getText().trim().equalsIgnoreCase(visibleText.trim())) {
                    return true;
                }
            } catch (Exception ignored) {
                /* retry until timeout */
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

        return false;
    }

    /** Wait for an error banner containing text within a timeout. */
    public boolean waitForErrorContains(String phrase, Duration timeout) {
        WebDriverWait localWait = new WebDriverWait(driver, timeout);

        // Try alert
        try {
            Alert alert = localWait.until(ExpectedConditions.alertIsPresent());
            if (alert.getText().contains(phrase)) {
                alert.accept();
                return true;
            }
        } catch (TimeoutException ignored) { /* fall through */ }

        // Try the in-page node
        try {
            By anyNodeWithPhrase = By.xpath("//*[contains(.,'" + phrase + "')]");
            localWait.until(ExpectedConditions.visibilityOfElementLocated(anyNodeWithPhrase));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
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
