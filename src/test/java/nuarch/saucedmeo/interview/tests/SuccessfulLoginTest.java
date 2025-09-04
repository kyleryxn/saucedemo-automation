package nuarch.saucedmeo.interview.tests;

import nuarch.saucedmeo.interview.pom.CartPage;
import nuarch.saucedmeo.interview.pom.CheckoutPage;
import nuarch.saucedmeo.interview.pom.InventoryPage;
import nuarch.saucedmeo.interview.pom.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Successful Login Test")
public class SuccessfulLoginTest extends BaseTest {

    @Test
    @DisplayName("Given standard_user | When adding 2 items via details page | Then checkout succeeds")
    void givenStandardUser_whenAddsTwoItemsViaDetails_thenCheckoutSucceeds() {
        pages.create(LoginPage.class).open().login("standard_user", "secret_sauce");

        InventoryPage inv = pages.create(InventoryPage.class).waitForLoaded();
        inv.openDetails("Sauce Labs Backpack").addToCart().backToProducts();
        inv.openDetails("Sauce Labs Bike Light").addToCart().backToProducts();

        CartPage cart = inv.openCart();
        assertEquals(2, cart.itemCount(), "Cart should contain 2 items.");

        CheckoutPage checkout = cart.proceedToCheckout()
                .fillStepOne("Jane", "Doe", "12345")
                .finish();

        assertTrue(checkout.isComplete(), "Expected checkout to complete with a Thank You message.");
    }

}
