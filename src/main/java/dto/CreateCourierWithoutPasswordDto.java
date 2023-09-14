package dto;

public class CreateCourierWithoutPasswordDto {
    private String login;
    private String firstName;

    public CreateCourierWithoutPasswordDto() {}

    public CreateCourierWithoutPasswordDto(String login, String firstName) {
        this.login = login;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
