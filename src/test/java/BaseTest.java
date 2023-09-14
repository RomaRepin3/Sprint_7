import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {

    protected static Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
}
