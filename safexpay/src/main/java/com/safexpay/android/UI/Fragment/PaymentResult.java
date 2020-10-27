package com.safexpay.android.UI.Fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.safexpay.android.R;
import com.safexpay.android.UI.Activity.PaymentDetailActivity;
import com.safexpay.android.Utils.Constants;
import com.safexpay.android.Utils.CryptoUtils;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.FragmentPaymentResultBinding;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentResult extends BaseFragment {

    private FragmentPaymentResultBinding binding;
    private String htmlContent = "";

    public PaymentResult(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentResultBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        binding.resultUrlViewSdk.setVisibility(View.VISIBLE);
        binding.resultUrlViewSdk.getSettings().setJavaScriptEnabled(true);
        binding.resultUrlViewSdk.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
        binding.resultUrlViewSdk.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (!url.endsWith("checkPay")) {
                        String encryptedResp = CryptoUtils.decrypt(Uri.parse(url).getQueryParameter("txn_response"),
                                SessionStore.merchantKey);
                        String[] valueData = encryptedResp.split("\\|");
                        if (valueData.length != 0) {
                            boolean isSuccessful = checkStatus(valueData);
                            invalidateUI(isSuccessful, valueData);
                        }
                    }
                } catch (Exception e) {
                    binding.resultUrlViewSdk.loadUrl(url);
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private void invalidateUI(boolean isSuccess, String[] valueData) {
        binding.resultUrlViewSdk.setVisibility(View.GONE);
        if (isSuccess) {
            SessionStore.successOrderId = valueData[2];
            SessionStore.successPaymentId = valueData[8];
            SessionStore.successTransactionId = valueData[9];
            PaymentDetailActivity.paymentResult = getString(R.string.transaction_successful);
            binding.paymentResultHeaderTvSdk.setVisibility(View.VISIBLE);
            binding.paymentResultAmountTvSdk.setVisibility(View.VISIBLE);
            binding.paymentResultStatusIvSdk.setVisibility(View.VISIBLE);
            binding.paymentResultStatusTvSdk.setVisibility(View.VISIBLE);
            binding.paymentResultOrderNoTvSdk.setVisibility(View.VISIBLE);
            binding.paymentResultStatusIvSdk.setImageDrawable(getResources().getDrawable(R.drawable.ic_success));
            binding.paymentResultStatusTvSdk.setTextColor(getResources().getColor(R.color.blueColorDark));
            binding.paymentResultStatusTvSdk.setText(getText(R.string.transaction_successful));
            binding.paymentResultAmountTvSdk.setText(String.format("₹%s", Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras())
                    .getString(Constants.AMOUNT)));
            binding.paymentResultOrderNoTvSdk.setText(String.format(getString(R.string.order_no_s),
                    Objects.requireNonNull(getActivity().getIntent().getExtras())
                            .getString(Constants.ORDER_NO)));
        } else {
            PaymentDetailActivity.paymentResult = getString(R.string.transaction_failed);
            binding.paymentResultStatusIvSdk.setVisibility(View.VISIBLE);
            binding.paymentResultStatusTvSdk.setVisibility(View.VISIBLE);
            binding.paymentResultStatusTvSdk.setText(getText(R.string.transaction_failed));
            binding.paymentResultStatusIvSdk.setImageDrawable(getResources().getDrawable(R.drawable.ic_cross_error));
            binding.paymentResultStatusTvSdk.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private boolean checkStatus(String[] elementsOfUrl) {
        boolean isSuccessFul = false;
        for (String element : elementsOfUrl) {
            if (element.equalsIgnoreCase("successful")) {
                return true;
            } else {
                isSuccessFul = false;
            }
        }
        return isSuccessFul;
    }
}
