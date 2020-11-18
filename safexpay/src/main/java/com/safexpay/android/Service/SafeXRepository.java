package com.safexpay.android.Service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.safexpay.android.Model.BrandingData;
import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.Model.SavedCards;
import com.safexpay.android.Utils.CryptoUtils;
import com.safexpay.android.Utils.SessionStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafeXRepository {

    private SafeXPayService safeXPayService;
    private MutableLiveData<BrandingData> brandingMutableData = new MutableLiveData<>();
    private MutableLiveData<String> paymentMutableData = new MutableLiveData<>();
    private MutableLiveData<List<PaymentMode>> paymentModeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<SavedCards>> savedCardsMutableLiveData = new MutableLiveData<>();
    private final String ERROR_DETAILS = "error_details";
    private final String SUCCESS = "success";
    private final String ERROR_MESSAGE = "error_message";
    private final String MERCHANTBRANDINGDETAILS = "merchantBrandingDetails";
    private final String CARD_DETAILS = "cardsDetails";
    private final String SCHEMES = "schemes";

    public SafeXRepository() {
        safeXPayService =  ServiceGenerator.getSafeXService();
    }

    public void getBrandingDetails(JsonObject brandingDetailObject){
        safeXPayService.getMerchantBranding(brandingDetailObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        String encryptedData = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getJSONObject(ERROR_DETAILS).getString(ERROR_MESSAGE).equalsIgnoreCase(SUCCESS)){
                                encryptedData = jsonObject.getString(MERCHANTBRANDINGDETAILS);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String decryptedData = CryptoUtils.decrypt(encryptedData, SessionStore.merchantKey);
                            JSONObject jsonObject = new JSONObject(decryptedData);
                            BrandingData paymentMode = new Gson().fromJson(jsonObject.toString(), BrandingData.class);
                            brandingMutableData.setValue(paymentMode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void getCardsByMerchant(JsonObject getCardsObject){
        safeXPayService.getCardsByMerchant(getCardsObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        String encryptedData = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getJSONObject("error_Details").getString(ERROR_MESSAGE).equalsIgnoreCase(SUCCESS)){
                                encryptedData = jsonObject.getString(CARD_DETAILS);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        String decryptedData = CryptoUtils.decrypt(encryptedData, SessionStore.merchantKey);
                        try {
                            JSONArray jsonArray = new JSONArray(decryptedData);
                            List<SavedCards> savedCardsList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                savedCardsList.add(new Gson().fromJson(jsonObject.toString(), SavedCards.class));
                            }
                            savedCardsMutableLiveData.setValue(savedCardsList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void getPayModeDetails(JsonObject payModeObject){
        safeXPayService.getPaymentMode(payModeObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        String encryptedData = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getJSONObject(ERROR_DETAILS).getString(ERROR_MESSAGE).equalsIgnoreCase(SUCCESS)){
                                encryptedData = jsonObject.getString(SCHEMES);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        String decryptedData = CryptoUtils.decrypt(encryptedData, SessionStore.merchantKey);
                        try {
                            JSONArray jsonArray = new JSONArray(decryptedData);
                            List<PaymentMode> paymentModeList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                paymentModeList.add(new Gson().fromJson(jsonObject.toString(), PaymentMode.class));
                            }
                            paymentModeMutableLiveData.setValue(paymentModeList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void makePayment(JsonObject paymentObject){
        safeXPayService.makePayment(paymentObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        String htmlContent = "";
                        try {
                            htmlContent = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        paymentMutableData.setValue(htmlContent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<BrandingData> getBrandingMutableData() {
        return brandingMutableData;
    }

    public MutableLiveData<List<PaymentMode>> getPaymentModeMutableLiveData() {
        return paymentModeMutableLiveData;
    }

    public MutableLiveData<String> getPaymentMutableData() {
        return paymentMutableData;
    }

    public MutableLiveData<List<SavedCards>> getSavedCardsMutableLiveData() {
        return savedCardsMutableLiveData;
    }
}
