import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class AuthERIB {

    public AuthERIB() {
    }

    private String tokenOne = null;
    private String tokenTwo = null;
    private String tokenMessenger;
    private String mGUID = null;
    private Cookie ESAMAPIJSESSIONID = null;
    private Cookie JSESSIONID = null;
    private Cookie DPJSESSIONID = null;
    private String host = null;

    public String getTokenMessenger() throws IOException, URISyntaxException {
        RestAssured.useRelaxedHTTPSValidation();

        User user = new User ("mobile3", "55098", "11223", "https://psi-csa.testonline.sberbank.ru:4477", "https://mobile-psinode1.testonline.sberbank.ru:4477");

        System.out.println("Регистрация приложения и получение mGUID. Ввод логина:");
        System.out.println();

        Response response1 = given()
                .baseUri(user.getHost())
                .basePath("/CSAMAPI/registerApp.do")
                .param("operation", "register")
                .param("login", user.getUsername())
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
        assertEquals(200, response1.statusCode());
        assertEquals("false", response1.xmlPath().getString("response.loginCompleted"));
        mGUID = response1.xmlPath().getString("response.confirmRegistrationStage.mGUID");
        ESAMAPIJSESSIONID = response1.getDetailedCookie("ESAMAPIJSESSIONID");
        JSESSIONID = response1.getDetailedCookie("JSESSIONID");
        System.out.println(ESAMAPIJSESSIONID);
        System.out.println(mGUID);


        System.out.println("Подтверждение логина. Ввод смс-пароля:");
        System.out.println();

        Response response2 = given()
                .baseUri(user.getHost())
                .basePath("/CSAMAPI/registerApp.do")
                .param("operation", "confirm")
                .param("mGUID", mGUID)
                .param("smsPassword", user.getSms_password())
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
        assertEquals(200, response2.statusCode());


        System.out.println("Создание PIN:");
        System.out.println();

        Response response3 = given()
                .baseUri(user.getHost())
                .basePath("/CSAMAPI/registerApp.do")
                .param("operation", "createPIN")
                .param("mGUID", mGUID)
                .param("password", user.getPin())
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
       // host =  response3.xmlPath().getString("response.loginData.host");
        System.out.println("tokenOne=" + tokenOne);

        System.out.println("Аутентификация по токену в блоке. Получение профиля пользователя:");
        System.out.println();

        Response response4 = given()
                .baseUri(user.getHost_block())
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
                .baseUri(user.getHost_block())
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
        return tokenMessenger;

    }


}
