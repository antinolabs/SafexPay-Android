package com.safexpay.android.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.google.gson.JsonObject;
import com.safexpay.android.Model.BrandingData;
import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.Service.SafeXRepository;

import okhttp3.ResponseBody;

public class BrandingDetailsViewModel extends AndroidViewModel {

    private LiveData<BrandingData> brandingLiveData;
    private LiveData<String> paymentLiveData;
    private SafeXRepository safeXRepository;

    public BrandingDetailsViewModel(@NonNull Application application) {
        super(application);
        safeXRepository = new SafeXRepository();
    }

    public void init() {
        brandingLiveData = safeXRepository.getBrandingMutableData();
        paymentLiveData = safeXRepository.getPaymentMutableData();
    }

    public void getBrandingData(JsonObject brandingObject) {
        safeXRepository.getBrandingDetails(brandingObject);
    }

    public void makePayment(JsonObject paymentObject) {
        safeXRepository.makePayment(paymentObject);
    }

    public LiveData<BrandingData> getBrandingLiveData() {
        return brandingLiveData;
    }

    public LiveData<String> getPaymentLiveData() {
        return paymentLiveData;
    }
}
