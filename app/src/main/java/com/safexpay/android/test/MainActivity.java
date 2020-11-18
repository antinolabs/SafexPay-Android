package com.safexpay.android.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.safexpay.android.SafeXPay;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SafeXPay.SafeXPayPaymentCallback {

    private static final HashMap<SafeXPay.Environment, String> env_options = new HashMap<>();
    static {
        env_options.put(SafeXPay.Environment.TEST, "https://test.avantgardepayments.com/");
        env_options.put(SafeXPay.Environment.PRODUCTION, "https://prod.avantgardepayments.com/");
    }
    private SafeXPay.Environment mCurrentEnv = SafeXPay.Environment.TEST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(this);
        SafeXPay.getInstance().initialize(MainActivity.this, mCurrentEnv, "paygate", "201710270001",
                "oqUl4D0LqA4plZw4reAX/K3UKJoQdet0k/N6X6K4Y5k=");
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPaymentComplete(String orderID, String transactionID, String paymentID, String paymentStatus) {
        showToast("TRANSACTION SUCCESSFUL");
        Log.d("*1234*", orderID +" -- " + transactionID + " -- " + paymentID + "  --  " + paymentStatus);
    }

    @Override
    public void onPaymentCancelled() {
        showToast("TRANSACTION CANCELLED");
    }

    @Override
    public void onInitiatePaymentFailure(String errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay_button){
            try {
                SafeXPay.getInstance().initiatePayment(this,
                        String.valueOf((int) ((Math.random() * 50000) + 1)),
                        Integer.parseInt(((EditText)findViewById(R.id.amount_et)).getText().toString()),
                        "INR", "SALE","MOBILE",
                        "http://localhost/safexpay/response.php",
                        "http://localhost/safexpay/response.php","IND",this);
            } catch (Exception e){
                showToast(getString(R.string.something_went_Wrong));
                e.printStackTrace();
            }
        }
    }
}