package com.safexpay.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.safexpay.android.Service.ServiceGenerator;
import com.safexpay.android.UI.Activity.PaymentDetailActivity;
import com.safexpay.android.Utils.Constants;
import com.safexpay.android.Utils.CryptoUtils;

import java.util.Objects;

public class SafeXPay extends BroadcastReceiver {

    public static final String TAG = SafeXPay.class.getSimpleName();
    public static final String ACTION_INTENT_FILTER = "com.safexpay.android.sdk";
    private static SafeXPay mInstance;
    private Context mContext;
    private String agId = "";
    private String merchantId = "";
    private String merchantKey = "";
    private SafeXPayPaymentCallback mCallback;
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_CANCELLED = 2;
    public static final int RESULT_FAILED = 3;

    public SafeXPay() {
        //Default private constructor
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int keyCode = intent.getIntExtra(Constants.KEY_CODE, 0);
        String keyMessage = intent.getStringExtra(Constants.KEY_MESSGE);
        if ( keyCode == RESULT_CANCELLED) {
            mCallback.onPaymentCancelled();
        } else if (keyCode == RESULT_FAILED) {
            mCallback.onInitiatePaymentFailure(keyMessage);
        } else if (keyCode == RESULT_SUCCESS) {
            if (intent.getExtras() != null) {
                mCallback.onPaymentComplete(intent.getExtras().getString(Constants.ORDER_NO),
                        intent.getExtras().getString(Constants.TRANSACTION_ID),
                        intent.getExtras().getString(Constants.PAYMENT_ID),
                        Constants.PAYMENT_STATUS_SUCCESS);
            }
        }
    }

    public interface SafeXPayPaymentCallback {
        void onPaymentComplete(String orderID, String transactionID, String paymentID, String paymentStatus);

        void onPaymentCancelled();

        void onInitiatePaymentFailure(String errorMessage);
    }

    public enum Environment {
        TEST, PRODUCTION
    }

    /**
     * Initialize the SDK with application context and environment
     */
    public void initialize(Context context, Environment environment, final String agId, final String merchantId,
                           final String merchantKey) {
        mContext = context;
        this.agId = agId;
        this.merchantId = merchantId;
        this.merchantKey = merchantKey;
        ServiceGenerator.initialize(environment);
    }

    public Context getContext() {
        return mContext;
    }

    public static SafeXPay getInstance() {
        if (mInstance == null) {
            synchronized (SafeXPay.class) {
                if (mInstance == null) {
                    mInstance = new SafeXPay();
                }
            }
        }
        return mInstance;
    }

    public void initiatePayment(final Activity activity, final String orderNo, final int amount, String currency,
                                final String txnType, final String channel,
                                final String successUrl, final String failureUrl, final String countryCode,
                                final SafeXPayPaymentCallback callback) {
        mCallback = callback;
        try {
            if (activity != null && orderNo != null && !orderNo.trim().isEmpty() && amount != 0 && currency != null &&
                    !currency.trim().isEmpty() && txnType != null && !txnType.trim().isEmpty() && channel != null &&
                    !channel.trim().isEmpty() && successUrl != null && !successUrl.trim().isEmpty() && failureUrl != null &&
                    !failureUrl.trim().isEmpty() && countryCode != null && !countryCode.trim().isEmpty() && callback != null) {
                Intent intent = new Intent(mContext, PaymentDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.AGGREGATOR_ID, agId);
                bundle.putString(Constants.MERCHANT_KEY, merchantKey);
                bundle.putString(Constants.ORDER_NO, orderNo);
                bundle.putString(Constants.AMOUNT, String.valueOf(amount));
                bundle.putString(Constants.CURRENCY, currency);
                bundle.putString(Constants.TXNTYPE, txnType);
                bundle.putString(Constants.CHANNEL, channel);
                bundle.putString(Constants.SUCCESS_URL, successUrl);
                bundle.putString(Constants.FAILURE_URL, failureUrl);
                bundle.putString(Constants.COUNTRY_CODE, countryCode);
                bundle.putString(Constants.MERCHANT_ID, CryptoUtils.encrypt(merchantId, Constants.internalKey));
                intent.putExtras(bundle);
                activity.startActivity(intent);
            } else {
                mCallback.onInitiatePaymentFailure(Objects.requireNonNull(activity).getString(R.string.data_not_accurate));
            }
        } catch (Exception e){
            mCallback.onInitiatePaymentFailure(Objects.requireNonNull(activity).getString(R.string.data_not_accurate));
        }
    }
}
