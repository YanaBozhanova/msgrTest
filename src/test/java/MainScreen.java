import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import static io.restassured.RestAssured.given;


public class MainScreen {
    public MainScreen() throws IOException, URISyntaxException {
    }

    AuthERIB session = new AuthERIB();
    String X_Messenger_Token = session.getTokenMessenger();
    String URL = ConfigurationStand.getBaseUrlPSI();


    @Test
    public void getSequenceId() throws IOException, URISyntaxException {

        Response response = given()
                .baseUri(URL)
                .basePath("/api/main/page/get_sequence_id")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("Content-Type","application/json")
                .log().all().request()
                .when()
                .get()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
    //  Integer sequence_id = response.jsonPath().get("sequence_id");
    //  Assertions.assertNotNull(sequence_id);

    }

    @Test
    public void getEventData() throws IOException {

        Response response1 = given()
                .baseUri(URL)
                .basePath("/api/main/page/get_sequence_id")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("Content-Type","application/json")
                .when()
                .get()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        Integer sequence_id = response1.jsonPath().get("sequence_id");


        Response response2 = given()
                .baseUri(URL)
                .basePath("/api/main/page/get_event_data")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("Content-Type","application/json")
                .body("{\"sequence_id\":" + (sequence_id-10) + "\"limit\": \"9\",\"version\": \"3.8\"}")
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();

    }

    @Test
    public void generateUserDeeplink() throws IOException {

        Response response = given()
                .baseUri(URL)
                .basePath("/api/user/deeplink/generate")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("Content-Type","application/json")
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
      // String userDeeplink = response.jsonPath().get("deeplink");
      // Long creationDate = response.jsonPath().get("creation_date");
      //Assertions.assertNotNull(userDeeplink);
     // Assertions.assertNotNull(creationDate);

    }


    @Test
    public void getOrGenerateUserDeeplink() throws IOException {

        Response response = given()
                .baseUri(URL)
                .basePath("/api/user/deeplink/get_or_generate")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("Content-Type","application/json")
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        String userDeeplink = response.jsonPath().get("deeplink");
        Long creationDate = response.jsonPath().get("creation_date");
        Assertions.assertNotNull(userDeeplink);
        Assertions.assertNotNull(creationDate);

    }

    @Test
    public void getConversations() throws IOException {

        Response response = given()
                .baseUri(URL)
                .basePath("/api/main/page/get_conversations")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("Content-Type","application/json")
                .body("{\"start_row\": 1, \"limit\": 5}")
                .log().all().request()
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();


    }

    @Test
    public void getConversationsByIds() throws Exception {

        Response response = given()
                .baseUri(URL)
                .basePath("/api/main/page/get_conversations_by_ids")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("Content-Type","application/json")
                .body("{\"ids\": [609629901]}")
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
    }
    }
