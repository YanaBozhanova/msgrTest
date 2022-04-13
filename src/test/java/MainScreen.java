import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

public class MainScreen {
    AuthERIB session = new AuthERIB();
    String X_Messenger_Token = session.getTokenMessenger();

    public MainScreen() throws IOException, URISyntaxException {
    }

    @Test

    public void getSequenceId() {
        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:7766")
                .basePath("/api/main/page/get_sequence_id")
                .header("X_Messenger_Token", X_Messenger_Token)
                .when()
                .get()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        Integer sequence_id = response.jsonPath().get("sequence_id");
        assertNotNull(sequence_id);

    }

    @Test

    public void getEventData(){
        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:7766")
                .basePath("/api/main/page/get_event_data")
                .header("X_Messenger_Token", X_Messenger_Token)
                .body("{\"sequence_id\": \"2980\",\"limit\": \"9\",\"version\": \"3.8\"}")
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();

    }

    @Test

    public void generateUserDeeplink(){
        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:7766")
                .basePath("/api/user/deeplink/generate")
                .header("X_Messenger_Token", X_Messenger_Token)
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        String userDeeplink = response.jsonPath().get("deeplink");
        Long creationDate = response.jsonPath().get("creation_date");
        assertNotNull(userDeeplink);
        assertNotNull(creationDate);

    }


    @Test

    public void getOrGenerateUserDeeplink(){
        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:7766")
                .basePath("/api/user/deeplink/get_or_generate")
                .header("X_Messenger_Token", X_Messenger_Token)
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        String userDeeplink = response.jsonPath().get("deeplink");
        Long creationDate = response.jsonPath().get("creation_date");
        assertNotNull(userDeeplink);
        assertNotNull(creationDate);

    }
    }
