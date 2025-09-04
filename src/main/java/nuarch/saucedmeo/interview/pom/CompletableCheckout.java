package nuarch.saucedmeo.interview.pom;

public interface CompletableCheckout {

    CheckoutPage fillStepOne(String first, String last, String zip);

    CheckoutPage finish();

    boolean isComplete();

}
