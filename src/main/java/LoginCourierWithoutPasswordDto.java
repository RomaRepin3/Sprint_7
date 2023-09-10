public class LoginCourierWithoutPasswordDto {
    private String login;

    public LoginCourierWithoutPasswordDto() {}

    public LoginCourierWithoutPasswordDto(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
