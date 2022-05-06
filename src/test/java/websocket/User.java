package websocket;

public class User {
    private String username;
    private String sms_password;
    private String pin;
    private String host;
    private String host_block;

    public User(String username, String sms_password, String pin, String host) {
        this.username = username;
        this.sms_password = sms_password;
        this.pin = pin;
        this.host = host;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSms_password() {
        return sms_password;
    }

    public void setSms_password(String sms_password) {
        this.sms_password = sms_password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getHost() { return host; }

    public void setHost(String host) { this.host = host;}


}
