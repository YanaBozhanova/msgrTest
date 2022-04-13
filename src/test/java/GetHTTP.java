import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static io.restassured.RestAssured.given;

public class GetHTTP {

    static String X_Messenger_Token = "lizyjPtdjUvkpgIMsnePR1MnTwFtlFC1QLgdZqgEcdYFsOQtgQZ7QAvuv390QUWaTxxD6WK0qpcEM3ps9NMZtDMmy81zxsWWb0JLvN6s4orIFn6zB8E3Ve2stZRs3IHY";
    static String X_Messenger_Device_UID = null;

    @Test
    public void getConversations() throws IOException {

        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:443")
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
                .baseUri("https://messenger-t.sberbank.ru:443")
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
    @Test
    public void getSequenceId() throws Exception {

        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:443")
                .basePath("/api/main/page/get_sequence_id")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("X-Messenger-Device-UID",X_Messenger_Device_UID)
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
    }

    @Test
    public void getEventData() throws Exception {

        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:443")
                .basePath("/api/main/page/get_event_data")
                .header("X-Messenger-Token", X_Messenger_Token)
                .header("X-Messenger-Device-UID",X_Messenger_Device_UID)
                .body("{\"data\": {\"sequence_id\": 7,\"limit\": 100}}")
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
    }

}
