import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;

public class Loans {

    public Loans() throws IOException, URISyntaxException {
    }

    AuthERIB session = new AuthERIB();
    String X_Messenger_Token = session.getTokenMessenger();


    @Test
    public void getLoans() throws IOException {

        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:443")
                .basePath("/api/loans/get")
                .param("userId", "")
                .param("isBorrower", "true")
                .param("statuses", "OPENED")
                .param("limit", "10")
                .param("page", "0")
                .param("otherUserId", "")
                .header("X-Messenger-Token", X_Messenger_Token)
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();


    }

    @Test
    public void getLoanById() throws Exception {

        Response response = given()
                .baseUri("https://messenger-t.sberbank.ru:443")
                .basePath("/api/loans/get_by_id")
                .param("loanId", "200")
                .header("X-Messenger-Token", X_Messenger_Token)
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
    }


}
