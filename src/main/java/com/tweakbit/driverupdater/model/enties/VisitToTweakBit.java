package com.tweakbit.driverupdater.model.enties;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class VisitToTweakBit implements Comparable {
    private boolean download;
    private String keyError;
    private String downloadHourOfDay;
    private String visitHourOfDay;
    private String productName;
    private String auid;
    private String url;
    private String lang;
    private String os;
    private String browser;
    private String gclid;
    private String beltTime;
    private String marker;
    private String belt;
    private String sessionCount;
    private String size;
    private String sizeHigh;
    private int purchase;
    private double revenue;
    private String currency;
    private String errorsInCart;
    private String kwFromGetOfUrl;
    private String contentFromGetOfUrl;
    private String clkid;
    private String clientId;
    private Date dateOfVizit;
    private int weekVisit;
    private String AUIDFromateTime;

    public String getAUIDFromateTime() {
        return AUIDFromateTime;
    }

    public VisitToTweakBit() {
    }

    public String getSizeHigh() {
        return sizeHigh;
    }

    public void setSizeHigh(String sizeHigh) {
        this.sizeHigh = sizeHigh;
    }

    public Date getDateOfVizit() {
        return dateOfVizit;
    }

    public void setDateOfVizit(Date dateOfVizit) {
        this.dateOfVizit = dateOfVizit;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClkid() {
        return clkid;
    }

    public void setClkid(String clkid) {
        this.clkid = clkid;
    }

    public String getContentFromGetOfUrl() {
        return contentFromGetOfUrl;
    }

    public void setContentFromGetOfUrl(String contentFromGetOfUrl) {
        this.contentFromGetOfUrl = contentFromGetOfUrl;
    }

    public int getWeekVisit() {
        return weekVisit;
    }

    public void setWeekVisit(int weekVisit) {
        this.weekVisit = weekVisit;
    }

    public String getKwFromGetOfUrl() {
        return kwFromGetOfUrl;
    }

    public void setKwFromGetOfUrl(String kwFromGetOfUrl) {
        this.kwFromGetOfUrl = kwFromGetOfUrl;
    }

    public String getErrorsInCart() {
        return errorsInCart;
    }

    public void setErrorsInCart(String errorsInCart) {
        this.errorsInCart = errorsInCart;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {

        this.revenue = revenue;
    }

    public int getPurchase() {
        return purchase;
    }

    public void setPurchase(int purchase) {
        this.purchase = purchase;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(String sessionCount) {
        this.sessionCount = sessionCount;
    }

    public VisitToTweakBit(String auid) {
        this.auid = auid;
    }

    public String getBelt() {
        return belt;
    }

    public void setBelt(String belt) {
        this.belt = belt;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public String getGclid() {
        return gclid;
    }

    public void setGclid(String gclid) {
        this.gclid = gclid;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getKeyError() {
        return keyError;
    }

    public void setKeyError(String keyError) {
        this.keyError = keyError;
    }

    public String getDownloadHourOfDay() {
        return downloadHourOfDay;
    }

    public void setDownloadHourOfDay(String downloadHourOfDay) {
        this.downloadHourOfDay = downloadHourOfDay;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVisitHourOfDay() {
        return visitHourOfDay;
    }

    public void setVisitHourOfDay(String visitHourOfDay) {
        this.visitHourOfDay = visitHourOfDay;
    }

    public String getBeltTime() {
        return beltTime;
    }

    public void setBeltTime(String beltTime) {
        this.beltTime = beltTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VisitToTweakBit{");
        sb.append(", auid='").append(auid).append('\'');
        sb.append(", productName='").append(productName).append('\'');
        sb.append(", downloadHourOfDay=").append(downloadHourOfDay);
        sb.append("download=").append(download);
        sb.append(", visitHourOfDay=").append(visitHourOfDay);
        sb.append(", keyError='").append(keyError).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(@NotNull Object o) {
        VisitToTweakBit visitToTweakBit = (VisitToTweakBit) o;

        if(this.getDateOfVizit().before(visitToTweakBit.getDateOfVizit())){
            return 1;
        }else {
            return 0;
        }
    }

    public void setAUIDFromateTime(String AUIDFromateTime) {
        this.AUIDFromateTime = AUIDFromateTime;
    }
}
