package com.safexpay.android.Service;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.safexpay.android.BuildConfig;
import com.safexpay.android.SafeXPay;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String TAG = ServiceGenerator.class.getSimpleName();
    private static final String PRODUCTION_BASE_URL = "https://prod.avantgardepayments.com/";
    private static final String TEST_BASE_URL = "https://test.avantgardepayments.com/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit;

    static {
        initialize(SafeXPay.Environment.TEST);
    }

    public static void initialize(SafeXPay.Environment environment) {
        String baseUrl = (environment == SafeXPay.Environment.PRODUCTION) ? PRODUCTION_BASE_URL : TEST_BASE_URL;
        retrofit = builder.baseUrl(baseUrl).build();
    }

    public static SafeXPayService getSafeXService() {
        return retrofit.create(SafeXPayService.class);
    }
}
