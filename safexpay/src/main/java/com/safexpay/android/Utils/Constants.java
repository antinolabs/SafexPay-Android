package com.safexpay.android.Utils;

public class Constants {

    /**
     * Extra Bundle key passed to SAFEXPAY SDK.
     */
    public static final String PAYMENT_BUNDLE = "payment_bundle";

    /**
     * Extra Bundle key for Order No which is passed back from SDK.
     */
    public static final String ORDER_NO = "orderNo";

    /**
     * Extra key for the {@link Order} object that is sent through Bundle.
     */
    public static final String ORDER = "order";

    /**
     * Activity request code
     */
    public static final int REQUEST_CODE = 9;

    /**
     * Internal key for all transactions
     */
    public static final String internalKey = "HiktfH0Mhdla4zDg0/4ASwFQh2OS+nf9MVL0ik3DsmE=";

    /**
     * key for the Juspay card URL
     */
    public static final String URL = "url";

    /**
     * Key for the merchant ID for the current transaction
     */
    public static final String MERCHANT_ID = "merchantId";

    /**
     * Key for the success Url for the current transaction
     */
    public static final String SUCCESS_URL = "successUrl";

    /**
     * Key for the failure Url for the current transaction
     */
    public static final String FAILURE_URL = "failureUrl";

    /**
     * Key for the countryCode for the current transaction
     */
    public static final String COUNTRY_CODE = "countryCode";

    /**
     * Key for the AMOUNT for the current transaction
     */
    public static final String AMOUNT = "amount";

    /**
     * Key for the CURRENCY for the current transaction
     */
    public static final String CURRENCY = "currency";

    /**
     * Key for the transaction type for the current transaction
     */
    public static final String TXNTYPE = "txn_type";

    /**
     * Key for the CHANNEL for the current transaction
     */
    public static final String CHANNEL = "channel";

    /**
     * Key for the merchant KEY for the current transaction
     */
    public static final String MERCHANT_KEY = "merchantKey";

    /**
     * Key for the aggregator ID for the current transaction
     */
    public static final String AGGREGATOR_ID = "aggregator_id";

    /**
     * Key for Netbanking Data passed to Juspay
     */
    public static final String POST_DATA = "postData";

    /**
     * Key for transactionID in the return bundle
     */
    public static final String TRANSACTION_ID = "transactionID";

    /*
     * Key for paymentID in the return bundle
     */
    public static final String PAYMENT_ID = "paymentID";

    public static final String PAYMENT_STATUS_SUCCESS = "SUCCESS";

    /**
     * Status code for UPI Pending Authentication
     */
    public static final int PENDING_PAYMENT = 2;

    public static final String KEY_CODE = "code";

    public static final String KEY_MESSGE = "message";
}
