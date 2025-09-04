package nuarch.saucedmeo.interview.model;

public record LoginData(String username, String password, String expectedBug) {

    @Override
    public String toString() {
        return "LoginData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", expectedBug='" + expectedBug + '\'' +
                '}';
    }

}
