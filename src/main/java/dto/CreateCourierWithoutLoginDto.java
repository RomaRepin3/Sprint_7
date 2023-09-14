package dto;

public class CreateCourierWithoutLoginDto {
    private String password;
    private String firstName;

    public CreateCourierWithoutLoginDto() {}

    public CreateCourierWithoutLoginDto(String password, String firstName) {
        this.password = password;
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
