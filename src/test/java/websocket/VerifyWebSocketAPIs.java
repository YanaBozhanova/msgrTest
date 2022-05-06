package websocket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URISyntaxException;

public class VerifyWebSocketAPIs {
    SocketServiceData context;
    AuthERIB session = new AuthERIB();
    String X_Messenger_Token = session.getTokenMessenger();

    public VerifyWebSocketAPIs() throws IOException, URISyntaxException {
    }

    @Before
    public void createContext(){
        context = new SocketServiceData();
        context.URI ="wss://messenger-t.sberbank.ru:7766/api/ws/3.8/" + X_Messenger_Token;
        context.timeOut = 5;
        context.expectedMessage = "This is a test";
        context.actualMessage = "This is a test";
    }

    @Test
    public void verifyWebSocketAPI(){
        SocketServiceData responseContext = WebClient.getInstance().connectAndListen(context);
        Assertions.assertEquals(responseContext.statusCode, 1000, "Status code is different");
    }

    @Test
    public void verifyWebSocketAPI_TimeOutsAutomatically(){
        context.actualMessage = "Invalid message";
        SocketServiceData responseContext = WebClient.getInstance().connectAndListen(context);
        Assertions.assertEquals(responseContext.statusCode, 1006, "Status code is different");
    }
}
