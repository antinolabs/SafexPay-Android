package com.safexpay.android.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.JsonObject;
import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.Model.SavedCards;
import com.safexpay.android.Service.SafeXRepository;

import java.util.List;

public class PayModeViewModel extends AndroidViewModel {

    private LiveData<List<PaymentMode>> paymentModeLiveData;
    private LiveData<List<SavedCards>> savedCardsLiveData;
    private SafeXRepository safeXRepository;

    public PayModeViewModel(@NonNull Application application) {
        super(application);
        safeXRepository = new SafeXRepository();
    }

    public void init(){
        savedCardsLiveData = safeXRepository.getSavedCardsMutableLiveData();
        paymentModeLiveData = safeXRepository.getPaymentModeMutableLiveData();
    }

    public void getPayMode(JsonObject payModeObject){
        safeXRepository.getPayModeDetails(payModeObject);
    }

    public void getSavedCards(JsonObject savedCardsObject){
        safeXRepository.getCardsByMerchant(savedCardsObject);
    }

    public LiveData<List<PaymentMode>> getPaymentModeLiveData() {
        return paymentModeLiveData;
    }

    public LiveData<List<SavedCards>> getSavedCardsLiveData() {
        return savedCardsLiveData;
    }
}
