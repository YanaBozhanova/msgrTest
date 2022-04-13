import java.io.IOException;
import java.net.URISyntaxException;

public class WebSocket {

    AuthERIB session = new AuthERIB();
    String X_Messenger_Token = session.getTokenMessenger();

    public WebSocket() throws IOException, URISyntaxException {
    }


   /*     ContainerProvider.getWebSocketContainer();

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect("wss://messenger-t.sberbank.ru:7766/api/ws/3.8/" + tokenMessenger, sessionHandler);

        new Scanner(System.in).nextLine();*/
    // container.connectToServer(this, new URI("wss://messenger-t.sberbank.ru:7766/api/ws/3.8/" + tokenMessenger));


}
