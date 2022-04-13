public class User {
    private String username;
    private String sms_password;
    private String pin;

    public User(String username, String sms_password, String pin) {
        this.username = username;
        this.sms_password = sms_password;
        this.pin = pin;
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
}
