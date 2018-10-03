package com.tweakbit.driverupdater.model.enties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tweakbit.controller.DataParser;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitToTweakBit implements Comparable {
    @JsonProperty("product") private String productName;
    @JsonProperty private String lang;
    @JsonProperty private String os;
    @JsonProperty private String browser;
    private String marker;
    @JsonProperty("session") private String sessionCount;
    @JsonProperty("screenSize") private String screenSize;
    @JsonProperty private String time;
    @JsonProperty("kw") private String kwFromGetOfUrl;
    @JsonProperty("content") private String contentFromGetOfUrl;
    @JsonProperty private  String country;
    @JsonProperty private String city;
    @JsonProperty private String subdivision;
    @JsonProperty private String zip;
    @JsonProperty("time_zone") private String timeZone;
    @JsonProperty private String code;
    @JsonProperty private String localLang;
    private boolean download;
    private boolean visitToCart;
    private String keyError;
    private String downloadHourOfDay;
    private String visitHourOfDay;
    private String auid;
    private String url;
    private String gclid;
    private String beltTime;
    private String belt;
    private String size;
    private String sizeHigh;
    private int purchase;
    private double revenue;
    private String currency;
    private String errorsInCart;
    private String clkid;
    private String clientId;
    private Date dateOfVizit, dateOfViziteToCart;
    private int weekVisit, sessionTime;
    private String AUIDFromateTime;
    private String cartVisitHourOfDay;
    private long timeForPurchase;
    private double loadSite;

    public String getLocalLang() {
        return localLang;
    }

    public void setLocalLang(String localLang) {
        this.localLang = localLang;
    }

    public double getLoadSite() {
        return loadSite;
    }

    public void setLoadSite(double loadSite) {
        this.loadSite = loadSite;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCartVisitHourOfDay() {
        return cartVisitHourOfDay;
    }

    public String getBeltCartVisit() {
        return beltCartVisit;
    }

    public String getBeltTimeCartVisit() {
        return beltTimeCartVisit;
    }

    private String beltCartVisit;
    private String beltTimeCartVisit;

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

    @JsonProperty("marker")
    public void setMarker(String marker) {
        this.marker = DataParser.changeMarker(marker);
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

    public void setAUIDFromateTime(String AUIDFromateTime) {
        this.AUIDFromateTime = AUIDFromateTime;
    }

    public void setCartVisitHourOfDay(String cartVisitHourOfDay) {
        this.cartVisitHourOfDay = cartVisitHourOfDay;
    }

    public void setBeltCartVisit(String beltCartVisit) {
        this.beltCartVisit = beltCartVisit;
    }

    public void setBeltTimeCartVisit(String beltTimeCartVisit) {
        this.beltTimeCartVisit = beltTimeCartVisit;
    }

    public void setVisitToCart(boolean visitToCart) {
        this.visitToCart = visitToCart;
    }

    public boolean isVisitToCart() {
        return visitToCart;
    }

    public void setTimeForPurchase(long timeForPurchase) {
        this.timeForPurchase = timeForPurchase;
    }

    public long getTimeForPurchase() {
        return timeForPurchase;
    }

    public void setDateOfViziteToCart(Date dateOfViziteToCart) {
        this.dateOfViziteToCart = dateOfViziteToCart;
    }

    public Date getDateOfViziteToCart() {
        return dateOfViziteToCart;
    }

    public int getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(int sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "VisitToTweakBit{" +
                "productName='" + productName + '\'' +
                ", lang='" + lang + '\'' +
                ", os='" + os + '\'' +
                ", browser='" + browser + '\'' +
                ", marker='" + marker + '\'' +
                ", sessionCount='" + sessionCount + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", time='" + time + '\'' +
                ", kwFromGetOfUrl='" + kwFromGetOfUrl + '\'' +
                ", contentFromGetOfUrl='" + contentFromGetOfUrl + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", subdivision='" + subdivision + '\'' +
                ", zip='" + zip + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", code='" + code + '\'' +
                ", localLang='" + localLang + '\'' +
                ", download=" + download +
                ", visitToCart=" + visitToCart +
                ", keyError='" + keyError + '\'' +
                ", downloadHourOfDay='" + downloadHourOfDay + '\'' +
                ", visitHourOfDay='" + visitHourOfDay + '\'' +
                ", auid='" + auid + '\'' +
                ", url='" + url + '\'' +
                ", gclid='" + gclid + '\'' +
                ", beltTime='" + beltTime + '\'' +
                ", belt='" + belt + '\'' +
                ", size='" + size + '\'' +
                ", sizeHigh='" + sizeHigh + '\'' +
                ", purchase=" + purchase +
                ", revenue=" + revenue +
                ", currency='" + currency + '\'' +
                ", errorsInCart='" + errorsInCart + '\'' +
                ", clkid='" + clkid + '\'' +
                ", clientId='" + clientId + '\'' +
                ", dateOfVizit=" + dateOfVizit +
                ", dateOfViziteToCart=" + dateOfViziteToCart +
                ", weekVisit=" + weekVisit +
                ", sessionTime=" + sessionTime +
                ", AUIDFromateTime='" + AUIDFromateTime + '\'' +
                ", cartVisitHourOfDay='" + cartVisitHourOfDay + '\'' +
                ", timeForPurchase=" + timeForPurchase +
                ", loadSite=" + loadSite +
                ", beltCartVisit='" + beltCartVisit + '\'' +
                ", beltTimeCartVisit='" + beltTimeCartVisit + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        VisitToTweakBit visitToTweakBit = (VisitToTweakBit) o;

        if(this.getSessionTime() > visitToTweakBit.getSessionTime()){
            return 1;
        }else {
            return 0;
        }
    }
}
