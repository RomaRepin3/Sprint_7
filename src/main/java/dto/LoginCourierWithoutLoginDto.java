package dto;

public class LoginCourierWithoutLoginDto {
    private String password;

    public LoginCourierWithoutLoginDto() {}

    public LoginCourierWithoutLoginDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
