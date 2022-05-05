public class ConfigurationStand {

    private static String baseUrlPSI = "https://messenger-t.sberbank.ru:443";

    private static String baseUrlIFT = "https://messenger-ift.sberbank.ru:7764";

    private static String baseUrlST = "https://messenger-ift.sberbank.ru:7769";

    public static String getBaseUrlIFT() {
        return baseUrlIFT;
    }

    public static String getBaseUrlPSI() {
        return baseUrlPSI;
    }

    public static String getBaseUrlST() {
        return baseUrlST;
    }
}
