package nuarch.saucedmeo.interview.pom;

import org.openqa.selenium.WebDriver;

public final class DefaultPageFactory implements PageFactory {
    private final WebDriver driver;
    private final String baseUrl;

    public DefaultPageFactory(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    @Override
    public <P extends BasePage<P>> P create(Class<P> pageClass) {
        try {

            // Prefer (WebDriver, PageFactory, String) then (WebDriver, PageFactory)
            try {
                return pageClass
                        .getConstructor(WebDriver.class, PageFactory.class, String.class)
                        .newInstance(driver, this, baseUrl);
            } catch (NoSuchMethodException ignored) {
                return pageClass
                        .getConstructor(WebDriver.class, PageFactory.class)
                        .newInstance(driver, this);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to construct page: " + pageClass.getSimpleName(), e);
        }
    }

}
