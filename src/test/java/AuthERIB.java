import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.junit.Test;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class AuthERIB {
    private static String tokenOne = null;
    private static String tokenTwo = null;
    private static String tokenMessenger = null;
    private static String mGUID = null;
    private static Cookie ESAMAPIJSESSIONID = null;
    private static Cookie JSESSIONID = null;
    private static Cookie DPJSESSIONID = null;


    @Test
    public void getTokenMessengerTest() throws IOException, URISyntaxException, DeploymentException {
        System.out.println("Ввод логина:");
        System.out.println();
        Response response = given()
                .baseUri("https://psi-csa.testonline.sberbank.ru:4477")
                .basePath("/CSAMAPI/registerApp.do")
                .param("operation", "register")
                .param("login", "mobile3")
                .param("version", "9.10")
                .param("appType", "iPhone")
                .param("appVersion", "5.5.0")
                .param("deviceName", "Simulator")
                .param("devID", "08D4B172-B264-419A-BFBE-6EA7E00B6239")
                .header("Accept-Language", "ru;q=1")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mobile Device")
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        assertEquals(200, response.statusCode());
        assertEquals("false", response.xmlPath().getString("response.loginCompleted"));
        mGUID = response.xmlPath().getString("response.confirmRegistrationStage.mGUID");
        ESAMAPIJSESSIONID = response.getDetailedCookie("ESAMAPIJSESSIONID");
        JSESSIONID = response.getDetailedCookie("JSESSIONID");
        System.out.println(ESAMAPIJSESSIONID);
        System.out.println(mGUID);


        System.out.println("Ввод смс-пароля:");
        System.out.println();
        Response response1 = given()
                .baseUri("https://psi-csa.testonline.sberbank.ru:4477")
                .basePath("/CSAMAPI/registerApp.do")
                .param("operation", "confirm")
                .param("mGUID", mGUID)
                .param("smsPassword", "55098")
                .param("version", "9.10")
                .param("appType", "iPhone")
                .header("Accept-Language", "ru;q=1")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mobile Device")
                .cookie(ESAMAPIJSESSIONID)
                .cookie(JSESSIONID)
                .log().all().request()
                .log().body()
                .when()
                .post()
                .then()
                .log().body()
                .extract().response();
        assertEquals(200, response1.statusCode());


        System.out.println("Ввод пароля МП:");
        System.out.println();
        Response response3 = given()
                .baseUri("https://psi-csa.testonline.sberbank.ru:4477")
                .basePath("/CSAMAPI/registerApp.do")
                .param("operation", "createPIN")
                .param("mGUID", mGUID)
                .param("password", "11223")
                .param("version", "9.10")
                .param("appType", "iPhone")
                .param("appVersion", "5.5.0")
                .param("deviceName", "Simulator")
                .param("isLightScheme", "false")
                .param("devID", "08D4B172-B264-419A-BFBE-6EA7E00B6239")
                .param("mobileSdkData", "1")
                .header("Accept-Language", "ru;q=1")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mobile Device")
                .cookie(ESAMAPIJSESSIONID)
                .cookie(JSESSIONID)
                .when().post()
                .then()
                .log().body()
                .extract().response();
        assertEquals(0, response3.xmlPath().getInt("response.status.code"));
        tokenOne = response3.xmlPath().getString("response.loginData.token");
        System.out.println("tokenOne=" + tokenOne);

        System.out.println("Получение профиля пользователя:");
        System.out.println();
        Response response4 = given()
                .baseUri("https://mobile-psinode1.testonline.sberbank.ru:4477")
                .basePath("/mobile9/postCSALogin.do")
                .param("token", tokenOne)
                .header("Accept-Language", "ru;q=1")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mobile Device")
                .cookie(ESAMAPIJSESSIONID)
                .cookie(JSESSIONID)
                .when().post()
                .then()
                .log().body()
                .extract().response();
        assertEquals(200, response4.statusCode());
        assertEquals(0, response4.xmlPath().getInt("response.status.code"));
        DPJSESSIONID = response4.getDetailedCookie("DPJSESSIONID");
        JSESSIONID = response4.getDetailedCookie("JSESSIONID");


        System.out.println("Получение токена ЕРИБ:");
        System.out.println();

        Response response5 = given()
                .baseUri("https://mobile-psinode1.testonline.sberbank.ru:4477")
                .basePath("/mobile9/private/unifiedClientSession/getToken.do")
                .param("systemName", "messenger")
                .cookie(ESAMAPIJSESSIONID)
                .cookie(JSESSIONID)
                .cookie(DPJSESSIONID)
                .when().get()
                .then()
                .log().body()
                .extract().response();
        assertEquals(0, response5.xmlPath().getInt("response.status.code"));
        tokenTwo = response5.xmlPath().getString("response.token");
        System.out.println("tokenTwo=" + tokenTwo);


        System.out.println("Получение токена Мессенджера:");
        System.out.println();
        Response response6 = given()
                .baseUri("https://messenger-t.sberbank.ru:443")
                .basePath("/api/device/auth_erib")
                .header("Content-Type", "application/json")
                .cookie(ESAMAPIJSESSIONID)
                .cookie(JSESSIONID)
                .cookie(DPJSESSIONID)
                .when()
                .body("{\"device\":{\"version\":\"9.6.0.398\",\"model\":\"iPhone\",\"application_type\":\"MP_SBOL_IOS\",\"platform\":\"10.1.1\",\"uid\":\"035E992A-BF2C-4200-A517-2649A4B61382\"},\"business\":false,\"erib_auth_token\":\"" + tokenTwo + "\"}")
                .post()
                .then()
                .log().body()
                .extract().response();

        assertEquals(200, response6.jsonPath().getInt("status"));
        tokenMessenger = response6.jsonPath().get("token");
        System.out.println("tokenMessenger=" + tokenMessenger);
        System.out.println("test");
   /*     ContainerProvider.getWebSocketContainer();

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect("wss://messenger-t.sberbank.ru:7766/api/ws/3.8/" + tokenMessenger, sessionHandler);

        new Scanner(System.in).nextLine();*/
        // container.connectToServer(this, new URI("wss://messenger-t.sberbank.ru:7766/api/ws/3.8/" + tokenMessenger));
    }


}
