import io.restassured.http.Cookie;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;


public class AuthUFS {
    private static String tokenOne = null;
    private static String tokenUFS = null;
    private static String tokenMessenger = null;
    private static String mGUID = null;
    private static Cookie ESAMAPIJSESSIONID = null;
    private static Cookie JSESSIONID = null;
    private static Cookie DPJSESSIONID = null;
    private static Cookie UFS_SESSION = null;
    private static Cookie UFS_TOKEN = null;
    private static String hostUFS = null;
    private static String host = null;
    private static String login = "fedos";


    public static String getTokenMessengerTestOfUFS() throws Exception {

        System.out.println();
        System.out.println("Регистрация приложения и получение mGUID. Ввод логина:");
        System.out.println();

        Response response1 = given()
                .baseUri("https://psi-csa.testonline.sberbank.ru:4477")
                .basePath("/CSAMAPI/registerApp.do")
                .param("operation", "register")
                .param("login", login)
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

        System.out.println();
        System.out.println("Подтверждение логина. Ввод смс-пароля:");
        System.out.println();

        Response response2 = given()
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
                .log().body()
                .when()
                .post()
                .then()
                .log().body()
                .extract().response();
        assertEquals(200, response2.statusCode());

        System.out.println();
        System.out.println("Создание PIN:");
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
        host = response3.xmlPath().getString("response.loginData.host");
        System.out.println("tokenOne = " + tokenOne);
        System.out.println("host = " + host);

        System.out.println();
        System.out.println("Аутентификация по токену в блоке. Получение профиля пользователя:");
        System.out.println();

        Response response4 = given()
                .baseUri("https://mobile-" + host +":4477")
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

        System.out.println();
        System.out.println("Получение токена ЕФС:");
        System.out.println();

        Response response5 = given()
                .baseUri("https://mobile-" + host + ":4477")
                .basePath("/mobile9/private/unifiedClientSession/getToken.do")
                .param("systemName", "ufs7")
              //  .cookie(ESAMAPIJSESSIONID)
                .cookie(JSESSIONID)
                .cookie(DPJSESSIONID)
                .when().get()
                .then()
                .log().body()
                .extract().response();
        assertEquals(0, response5.xmlPath().getInt("response.status.code"));
        tokenUFS = response5.xmlPath().getString("response.token");
        hostUFS = response5.xmlPath().getString("response.host");
        System.out.println("tokenUFS = " + tokenUFS);
        System.out.println("hostUFS = "+ hostUFS);

        System.out.println();
        System.out.println("Создание зависимой сессии в ЕФС: ");
        System.out.println();

        Response response6 = given()
                .baseUri("https://" + hostUFS)
                .basePath("/sm-uko/v2/session/create")
                .header("Content-Type","application/json")
                //.cookie(ESAMAPIJSESSIONID)
                .cookie(JSESSIONID)
                .cookie(DPJSESSIONID)
                .when()
                .body("{\"token\":\"" + tokenUFS + "\"}")
                .log().all().request()
                .post()
                .then()
                .log().body()
                .extract().response();
        assertEquals(true, response6.jsonPath().getBoolean("success"));
        UFS_SESSION = response6.getDetailedCookie("UFS-SESSION");
        UFS_TOKEN = response6.getDetailedCookie("UFS-TOKEN");
        System.out.println(UFS_SESSION);
        System.out.println(UFS_TOKEN);


        System.out.println();
        System.out.println("Получение токена Мессенджера:");
        System.out.println();

        Response response7 = given()
                .baseUri("https://messenger-t.sberbank.ru:443")
                .basePath("/api/device/auth_ufs")
                .header("Content-Type", "application/json")
                .header("X-UfsHost", hostUFS)
                .header("X-MessengerDeviceID", "035E992A-BF2C-4200-A517-2649A4B67888")
                .header("X-DeviceModel","iPhone")
                .header("X-DevicePlatform", "10.1.1")
                .header("X-ApplicationType","MP_SBOL_IOS")
                .header("X-ApplicationVersion","12.11")
                .cookie(ESAMAPIJSESSIONID)
                .cookie(JSESSIONID)
                .cookie(DPJSESSIONID)
                .cookie(UFS_SESSION)
                .cookie(UFS_TOKEN)
                .log().all().request()
                .when()
                .post()
                .then()
                .log().body()
                .extract().response();

        assertEquals(200, response7.jsonPath().getInt("status"));
        tokenMessenger = response7.jsonPath().get("token");
        System.out.println("tokenMessenger=" + tokenMessenger);


        return tokenMessenger;

}}
