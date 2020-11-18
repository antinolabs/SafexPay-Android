package com.safexpay.android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentMode{

    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("payModeId")
    @Expose
    private String payModeId;
    @SerializedName("paymentModeDetailsList")
    @Expose
    private List<PaymentModeDetailsList> paymentModeDetailsList = null;

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(String payModeId) {
        this.payModeId = payModeId;
    }

    public List<PaymentModeDetailsList> getPaymentModeDetailsList() {
        return paymentModeDetailsList;
    }

    public void setPaymentModeDetailsList(List<PaymentModeDetailsList> paymentModeDetailsList) {
        this.paymentModeDetailsList = paymentModeDetailsList;
    }

    public class PaymentModeDetailsList {

        @SerializedName("schemeDetailsResponse")
        @Expose
        private SchemeDetailsResponse schemeDetailsResponse;
        @SerializedName("pgDetailsResponse")
        @Expose
        private PgDetailsResponse pgDetailsResponse;
        @SerializedName("webViewUrl")
        @Expose
        private String webViewUrl;
        private Boolean isSelected = false;

        public SchemeDetailsResponse getSchemeDetailsResponse() {
            return schemeDetailsResponse;
        }

        public void setSchemeDetailsResponse(SchemeDetailsResponse schemeDetailsResponse) {
            this.schemeDetailsResponse = schemeDetailsResponse;
        }

        public PgDetailsResponse getPgDetailsResponse() {
            return pgDetailsResponse;
        }

        public void setPgDetailsResponse(PgDetailsResponse pgDetailsResponse) {
            this.pgDetailsResponse = pgDetailsResponse;
        }

        public String getWebViewUrl() {
            return webViewUrl;
        }

        public void setWebViewUrl(String webViewUrl) {
            this.webViewUrl = webViewUrl;
        }

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }
    }

    public class PgDetailsResponse {

        @SerializedName("pg_id")
        @Expose
        private String pgId;
        @SerializedName("pg_name")
        @Expose
        private String pgName;
        @SerializedName("pg_icon")
        @Expose
        private String pgIcon;

        public String getPgId() {
            return pgId;
        }

        public void setPgId(String pgId) {
            this.pgId = pgId;
        }

        public String getPgName() {
            return pgName;
        }

        public void setPgName(String pgName) {
            this.pgName = pgName;
        }

        public String getPgIcon() {
            return pgIcon;
        }

        public void setPgIcon(String pgIcon) {
            this.pgIcon = pgIcon;
        }
    }

    public class SchemeDetailsResponse {

        @SerializedName("schemeId")
        @Expose
        private String schemeId;
        @SerializedName("schemeName")
        @Expose
        private String schemeName;

        public String getSchemeId() {
            return schemeId;
        }

        public void setSchemeId(String schemeId) {
            this.schemeId = schemeId;
        }

        public String getSchemeName() {
            return schemeName;
        }

        public void setSchemeName(String schemeName) {
            this.schemeName = schemeName;
        }
    }
}
