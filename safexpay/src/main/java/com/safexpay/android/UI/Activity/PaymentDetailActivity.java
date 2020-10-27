package com.safexpay.android.UI.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.safexpay.android.Model.BrandingData;
import com.safexpay.android.R;
import com.safexpay.android.SafeXPay;
import com.safexpay.android.UI.Fragment.BaseFragment;
import com.safexpay.android.UI.Fragment.ConfirmationFragment;
import com.safexpay.android.UI.Fragment.LogInFragment;
import com.safexpay.android.UI.Fragment.PaymentResult;
import com.safexpay.android.Utils.Constants;
import com.safexpay.android.Utils.CryptoUtils;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.ViewModel.BrandingDetailsViewModel;
import com.safexpay.android.databinding.ActivityPaymentDetailBinding;
import com.safexpay.android.databinding.SafexpayToolbarMainBinding;

import java.util.Objects;

public class PaymentDetailActivity extends BaseActivity implements View.OnClickListener, ConfirmationFragment.CancelTransactionListener {

    private ActivityPaymentDetailBinding binding;
    private BrandingDetailsViewModel detailsViewModel;
    private SafexpayToolbarMainBinding toolbarMainBinding;
    public static String paymentResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentDetailBinding.inflate(getLayoutInflater());
        toolbarMainBinding = SafexpayToolbarMainBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
        this.setFinishOnTouchOutside(false);
        init();
    }

    private void init() {
        detailsViewModel = ViewModelProviders.of(this).get(BrandingDetailsViewModel.class);
        detailsViewModel.init();
        observerViewModel();
        binding.mainContainerSdk.setVisibility(View.GONE);
        toolbarMainBinding.sdkCloseIv.setOnClickListener(this);
        binding.payOutButtonCard.setOnClickListener(this);
        toolbarMainBinding.safeXOrderAmount.setText(String.format("₹%s", getIntent().getStringExtra(Constants.AMOUNT)));
        toolbarMainBinding.safeXOrderId.setText(String.format(getString(R.string.order_no_s), getIntent().getStringExtra(Constants.ORDER_NO)));
        SessionStore.merchantId = getIntent().getStringExtra(Constants.MERCHANT_ID);
        SessionStore.merchantKey = getIntent().getStringExtra(Constants.MERCHANT_KEY);
        if (SessionStore.merchantId == null || SessionStore.merchantKey == null || SessionStore.merchantId.isEmpty() ||
                SessionStore.merchantKey.isEmpty()) {
            fireBroadcastAndReturn(SafeXPay.RESULT_FAILED, null, getString(R.string.transaction_failed));
        }
        fetchMerchantBrandingDetails(SessionStore.merchantId);
        IntentFilter filter = new IntentFilter(SafeXPay.ACTION_INTENT_FILTER);
        registerReceiver(SafeXPay.getInstance(), filter);
    }

    private void observerViewModel() {
        detailsViewModel.getBrandingLiveData().observe(this, new Observer<BrandingData>() {
            @Override
            public void onChanged(BrandingData brandingData) {
                bindDataWithView(brandingData);
            }
        });
        detailsViewModel.getPaymentLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String htmlContent) {
                loadPaymentResultFragment(htmlContent);
            }
        });
    }

    private void bindDataWithView(BrandingData brandingData) {
        SessionStore.menuColor = brandingData.getMerchantThemeDetails().getMenuColor();
        SessionStore.headingColor = brandingData.getMerchantThemeDetails().getHeadingBgcolor();
        SessionStore.bgColor = brandingData.getMerchantThemeDetails().getBgcolor();
        SessionStore.footerColor = brandingData.getMerchantThemeDetails().getFooterColor();

        //set views with colors and resources
        try {
            Glide.with(PaymentDetailActivity.this)
                    .load(brandingData.getLogo())
                    .error(getResources().getDrawable(R.drawable.ic_safexpay_favicon_10))
                    .into(toolbarMainBinding.safeXPayLogo);
        } catch (Exception e){
            e.printStackTrace();
        }
        toolbarMainBinding.headerViewToolbarSdk.setBackgroundColor(Color.parseColor(SessionStore.headingColor));
        binding.container.setBackgroundColor(Color.parseColor(SessionStore.bgColor));
        binding.payOutButtonCard.setBackgroundColor(Color.parseColor(SessionStore.headingColor));
        binding.mainContainerSdk.setVisibility(View.VISIBLE);
        loadLoginFragment();
    }

    private void fireBroadcastAndReturn(int resultCode, Bundle data, String message) {
        Intent intent = new Intent();
        if (data != null) {
            intent.putExtras(data);
        }
        intent.putExtra(Constants.KEY_CODE, resultCode);
        if (message != null && !message.isEmpty()) {
            intent.putExtra(Constants.KEY_MESSGE, message);
        }
        intent.setAction(SafeXPay.ACTION_INTENT_FILTER);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
        // Finish current activity
        finish();
    }

    private void fetchMerchantBrandingDetails(String merchantId) {
        // API call to fetch the branding details
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("me_id", SessionStore.merchantId);
        detailsViewModel.getBrandingData(jsonObject);
    }

    private void loadLoginFragment() {
        loadFragment(new LogInFragment(), false);
        binding.payOutButtonCard.setVisibility(View.GONE);
    }

    private void loadPaymentResultFragment(String htmlContent) {
        clearFragmentStack();
        loadFragment(new PaymentResult(htmlContent), false);
        binding.payOutButtonCard.setVisibility(View.GONE);
        toolbarMainBinding.amountCardContainerSdk.setVisibility(View.INVISIBLE);
    }

    /**
     * Load the given fragment to the support fragment manager
     *
     * @param fragment       Fragment to be added. Must be a subclass of {@link BaseFragment}
     * @param addToBackStack Whether to add this fragment to back stack
     */
    public void loadFragment(BaseFragment fragment, boolean addToBackStack) {
        binding.progressBarSdk.setVisibility(View.GONE);
        binding.payOutButtonCard.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.container, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getFragmentName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (paymentResult.equals("")) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                showConfirmationDialog();
            } else
                getSupportFragmentManager().popBackStackImmediate();
        } else {
            if (paymentResult.equals(getString(R.string.transaction_failed))) {
                fireBroadcastAndReturn(SafeXPay.RESULT_FAILED, null, getString(R.string.transaction_failed));
            } else if (paymentResult.equals(getString(R.string.transaction_successful))) {
                Bundle bundleData = new Bundle();
                bundleData.putString(Constants.ORDER_NO, SessionStore.successOrderId);
                bundleData.putString(Constants.PAYMENT_ID, SessionStore.successPaymentId);
                bundleData.putString(Constants.TRANSACTION_ID, SessionStore.successTransactionId);
                fireBroadcastAndReturn(SafeXPay.RESULT_SUCCESS, bundleData, null);
            }
        }
    }

    private void clearFragmentStack() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private int mapResultCode(int activityResultCode) {
        switch (activityResultCode) {
            case RESULT_OK:
                return SafeXPay.RESULT_SUCCESS;

            case RESULT_CANCELED:
                return SafeXPay.RESULT_CANCELLED;
        }

        return SafeXPay.RESULT_FAILED;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(SafeXPay.getInstance());
        SessionStore.clearSession();
        paymentResult = "";
        super.onDestroy();
    }

    private void showConfirmationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ConfirmationFragment reportDialogFragment = new ConfirmationFragment(this);
        reportDialogFragment.show(fm, "ConfirmationFragment");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sdk_close_iv) {
            showConfirmationDialog();
        } else if (v.getId() == R.id.pay_out_button_card) {
            preparePayment();
        }
    }

    private void preparePayment() {
        // API call to fetch the branding details
        if (!SessionStore.PG_ID.isEmpty()) {
            JsonObject jsonObject = new JsonObject();
            String txn_details = Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.AGGREGATOR_ID) + "|" + CryptoUtils.decrypt(SessionStore.merchantId,
                    Constants.internalKey) + "|" + Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.ORDER_NO) + "|" + Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.AMOUNT) + "|" + Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.COUNTRY_CODE) + "|" + Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.CURRENCY) + "|" + Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.TXNTYPE) + "|" + Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.SUCCESS_URL) + "|" + Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.FAILURE_URL) + "|" + Objects.requireNonNull(getIntent().getExtras())
                    .getString(Constants.CHANNEL);
            jsonObject.addProperty("txn_details", CryptoUtils.encrypt(txn_details, SessionStore.merchantKey));
            jsonObject.addProperty("pg_details",
                    CryptoUtils.encrypt(SessionStore.PG_ID + "|" + SessionStore.PAYMODE_ID + "|" +
                            SessionStore.SCHEME_ID + "|" + SessionStore.EMI_MONTHS, SessionStore.merchantKey));
            jsonObject.addProperty("card_details", CryptoUtils.encrypt("||||", SessionStore.merchantKey));
            jsonObject.addProperty("cust_details", CryptoUtils.encrypt("||||Y", SessionStore.merchantKey));
            jsonObject.addProperty("bill_details", CryptoUtils.encrypt("||||", SessionStore.merchantKey));
            jsonObject.addProperty("ship_details", CryptoUtils.encrypt("||||||", SessionStore.merchantKey));
            jsonObject.addProperty("item_details", CryptoUtils.encrypt("||", SessionStore.merchantKey));
            jsonObject.addProperty("other_details", CryptoUtils.encrypt("||||", SessionStore.merchantKey));
            jsonObject.addProperty("me_id", CryptoUtils.decrypt(SessionStore.merchantId, Constants.internalKey));
            detailsViewModel.makePayment(jsonObject);
        } else {
            Toast.makeText(this, "Please select some payment method in order to continue the transaction", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancelClicked() {
        fireBroadcastAndReturn(SafeXPay.RESULT_CANCELLED, null, null);
    }
}
