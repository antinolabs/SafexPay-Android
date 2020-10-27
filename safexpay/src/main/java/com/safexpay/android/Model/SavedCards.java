package com.safexpay.android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavedCards {
    @SerializedName("nameOnCard")
    @Expose
    private String nameOnCard;
    @SerializedName("first6Digits")
    @Expose
    private String first6Digits;
    @SerializedName("last4Digits")
    @Expose
    private String last4Digits;
    @SerializedName("expiryMonth")
    @Expose
    private String expiryMonth;
    @SerializedName("expiryYear")
    @Expose
    private String expiryYear;
    @SerializedName("payModeId")
    @Expose
    private String payModeId;

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getFirst6Digits() {
        return first6Digits;
    }

    public void setFirst6Digits(String first6Digits) {
        this.first6Digits = first6Digits;
    }

    public String getLast4Digits() {
        return last4Digits;
    }

    public void setLast4Digits(String last4Digits) {
        this.last4Digits = last4Digits;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(String payModeId) {
        this.payModeId = payModeId;
    }
}
