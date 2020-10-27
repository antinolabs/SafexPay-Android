package com.safexpay.android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandingData {

    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("integration_type")
    @Expose
    private String integrationType;
    @SerializedName("merchantThemeDetails")
    @Expose
    private MerchantThemeDetails merchantThemeDetails;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(String integrationType) {
        this.integrationType = integrationType;
    }

    public MerchantThemeDetails getMerchantThemeDetails() {
        return merchantThemeDetails;
    }

    public void setMerchantThemeDetails(MerchantThemeDetails merchantThemeDetails) {
        this.merchantThemeDetails = merchantThemeDetails;
    }

    public class MerchantThemeDetails {

        @SerializedName("heading_bgcolor")
        @Expose
        private String headingBgcolor;
        @SerializedName("bgcolor")
        @Expose
        private String bgcolor;
        @SerializedName("menu_color")
        @Expose
        private String menuColor;
        @SerializedName("footer_color")
        @Expose
        private String footerColor;

        public String getHeadingBgcolor() {
            return headingBgcolor;
        }

        public void setHeadingBgcolor(String headingBgcolor) {
            this.headingBgcolor = headingBgcolor;
        }

        public String getBgcolor() {
            return bgcolor;
        }

        public void setBgcolor(String bgcolor) {
            this.bgcolor = bgcolor;
        }

        public String getMenuColor() {
            return menuColor;
        }

        public void setMenuColor(String menuColor) {
            this.menuColor = menuColor;
        }

        public String getFooterColor() {
            return footerColor;
        }

        public void setFooterColor(String footerColor) {
            this.footerColor = footerColor;
        }

    }
}
