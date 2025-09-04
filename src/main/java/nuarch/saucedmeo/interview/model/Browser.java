package nuarch.saucedmeo.interview.model;

public enum Browser {
    CHROME,
    FIREFOX,
    EDGE;

    public static Browser from(String value) {
        if (value == null || value.isBlank()) {
            return CHROME; // default
        }

        try {
            return Browser.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unsupported browser: " + value + ". Valid values: CHROME, FIREFOX, EDGE");
        }
    }

    public String toString() {
        return this.name().toLowerCase();
    }

}
