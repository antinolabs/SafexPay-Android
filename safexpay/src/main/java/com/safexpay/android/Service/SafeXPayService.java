package com.safexpay.android.Service;

import com.google.gson.JsonObject;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SafeXPayService {

    /**
     * API for getting list of Payment Modes like NetBanking, Wallets, UPI payments
     * @param jsonObject
     * @return
     */
    @POST("agcore/api/query/payModeAndSchemeAPI")
    Call<ResponseBody> getPaymentMode(@Body JsonObject jsonObject);


    /**
     * API for getting Merchant color scheme
     * @param jsonObject
     * @return
     */
    @POST("agcore/api/query/merchantBrandingDetails")
    Call<ResponseBody> getMerchantBranding(@Body JsonObject jsonObject);

    /**
     * API for getting Merchant saved cards details
     * @param jsonObject
     * @return
     */
    @POST("agcore/api/query/getCardsByMerchant")
    Call<ResponseBody> getCardsByMerchant(@Body JsonObject jsonObject);

    /**
     * API for getting Merchant color scheme
     * @param jsonObject
     * @return
     */
    @POST("agcore/appPay")
    Call<ResponseBody> makePayment(@Body JsonObject jsonObject);

}
