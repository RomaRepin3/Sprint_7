package dto;

public class CreateCourierDto extends LoginCourierDto {
    private String firstName;

    public CreateCourierDto() {}

    public CreateCourierDto(String login, String password) {
        super(login, password);
    }

    public CreateCourierDto(String login, String password, String firstName) {
        super(login, password);
        this.firstName = firstName;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
