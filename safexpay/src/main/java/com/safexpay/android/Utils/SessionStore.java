package com.safexpay.android.Utils;

public class SessionStore {
    public static String menuColor = "";
    public static String headingColor = "";
    public static String bgColor = "";
    public static String footerColor = "";
    public static String brandingLogo = "";
    public static String merchantKey = "";
    public static String merchantId = "";
    public static String PG_ID = "";
    public static String PAYMODE_ID = "";
    public static String SCHEME_ID = "";
    public static String EMI_MONTHS = "7";

    //Success keys
    public static String successOrderId = "";
    public static String successTransactionId = "";
    public static String successPaymentId = "";

    public static void clearSession() {
        menuColor = "";
        headingColor = "";
        bgColor = "";
        footerColor = "";
        merchantKey = "";
        merchantId = "";
        brandingLogo = "";
        PG_ID = "";
        PAYMODE_ID = "";
        SCHEME_ID = "";
        EMI_MONTHS = "7";
    }

    public static void clearPaymentIds() {
        PG_ID = "";
        PAYMODE_ID = "";
        SCHEME_ID = "";
    }
}
