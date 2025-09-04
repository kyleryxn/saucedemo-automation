package nuarch.saucedmeo.interview.pom;

public interface PageFactory {

    <P extends BasePage<P>> P create(Class<P> pageClass);

}
