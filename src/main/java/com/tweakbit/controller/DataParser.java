package com.tweakbit.controller;

import com.CalcOfMoon;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweakbit.driverupdater.model.enties.VisitToTweakBit;
import com.tweakbit.model.Params;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataParser {
    StringBuilder toFile;
    SaveToString saveToString;
    CalcOfMoon calcOfMoon;
    TreeMap<Params,TreeMap<String,Integer>> labelsOfTree;
    VisitToTweakBit visitToDU;
    File fileForQuery;
    Boolean safeDataForQuery;
    static public Integer countDownloaded = 0;
    protected static final String CHARSET_FOR_URL_ENCODING = "UTF-8";

    public DataParser() {
    }

    public DataParser(StringBuilder toFile) {
        this.toFile = toFile;
        this.saveToString = new SaveToString();
        this.calcOfMoon = new CalcOfMoon();
        this.safeDataForQuery = false;
    }

    public DataParser(StringBuilder toFile, TreeMap<Params,
            TreeMap<String, Integer>> labelsOfTree, HttpServletRequest req) throws IOException {
        this.toFile = toFile;
        this.labelsOfTree = labelsOfTree;
        this.saveToString = new SaveToString(labelsOfTree);
        this.calcOfMoon = new CalcOfMoon();
        this.safeDataForQuery = false;
        this.visitToDU = saveDataToAppFromHttp(req);
        saveToFile(visitToDU, "\n", true, false);
    }

    public SaveToString getSaveToString() {
        return saveToString;
    }

    public StringBuilder getToFile() {
        return toFile;
    }

    private VisitToTweakBit saveDataToAppFromHttp(HttpServletRequest req){
        VisitToTweakBit app = new VisitToTweakBit();
        String jsonInString = null;
        try {
            jsonInString = req.getParameter("data");
            byte[] decodedBytes = Base64.getDecoder().decode(jsonInString);
            jsonInString = new String(decodedBytes);
            ObjectMapper mapper = new ObjectMapper();
            app = mapper.readValue(jsonInString, VisitToTweakBit.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //здесь сохраняется время визита в секундах, часовой пояс, название пояса и дата
        getTimeAndTimeZoneFromData(app.getTime(), app, false);
        app.setSize(getScreenWide(app.getScreenSize()));
        app.setSizeHigh(getScreenHigh(app.getScreenSize()));
        setWeekFromDateToApp(app, app.getDateOfVizit());
        app.setDownload(false);

        return app;
    }

    public Date getDateFromFileName(String s) {
        String split = s.substring(0, s.length()-4);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formate.parse(split);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public int getRowsFromDataSet(StringBuilder toFile) {
        Scanner scanner = new Scanner(toFile.toString());
        String line = "";
        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            break;
        }
        String[] splitLine = line.split(",");
        return splitLine.length;
    }

    public void parsingOfSession(JSONObject jsonObject, VisitToTweakBit app, int sessionCount){
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyValue = jsonObject.get(keyStr);

            if (keyStr.equals("url")) {
                String value = keyValue.toString();
                getProduct(value, app);
                // сохраняю имя ошибки из wikifixes из рекламы
                app.setKeyError(getParamFromWiki(value));
                //app.setUrl(value);
                setGetToAppFromData(app, keyValue.toString());
                if (keyValue.toString().contains("/afterinstall/") || keyValue.toString().contains("/download/")) {
                    app.setDownload(true);
                    countDownloaded++;
                }
            }

            if (keyStr.equals("lang")) {
                app.setLang(keyValue.toString());
            }
            if (keyStr.equals("downloadTime")){
                if(app.isDownload()){
                    app.setDownload(true);
                }else {
                    countDownloaded++;
                }
            }

            if (keyStr.equals("time")) {
                getTimeAndTimeZoneFromData(keyValue.toString(), app, false);
                app.setAUIDFromateTime(keyValue.toString());
            }

            if (keyStr.equals("os")) {
                app.setOs(getOsFromData(keyValue.toString()));
            }
            if (keyStr.equals("clkid")) {
                app.setClkid(keyValue.toString());
            }

            if (keyStr.equals("browser")) {
                app.setBrowser(getBrowserFromData(keyValue.toString()));
            }
            if (keyStr.equals("screenSize")) {
                app.setSize(getScreenWide(keyValue.toString()));
                app.setSizeHigh(getScreenHigh(keyValue.toString()));
            }
            if (keyStr.equals("marker")) {
                try{
                    app.setMarker(changeMarker(keyValue.toString()));
                }catch(StringIndexOutOfBoundsException e){
                }
            }
            if(keyStr.equals("purchase")){
                parsingOfPurchase((JSONObject) keyValue, app);
            }
            if(keyStr.equals("carttime")){
                app.setVisitToCart(true);
                getTimeAndTimeZoneFromData(keyValue.toString(), app, true);
            }

            if(keyStr.equals("geo")){
                if(keyValue.toString() != "true" && keyValue.toString() != "null"
                        && !keyValue.toString().contains("not connected") ){
                        parsingOfPurchase((JSONObject) keyValue, app);
                }
            }
            if(keyStr.equals("localLang")){
                app.setLocalLang(keyValue.toString());
            }
            if(keyStr.equals("loadTime")){
                if(keyValue.toString().contains("browser not support")){
                    app.setLoadSite(-1.0);
                }else {
                    app.setLoadSite(Double.valueOf(keyValue.toString()));
                }
            }
        }
    }

    public static String changeMarker(String marker){
        String changed = marker.substring(marker.indexOf("src_"), marker.indexOf("\\d"));
        return changed;
    }

    private void setGetToAppFromData(VisitToTweakBit app, String keyValue) {
        if(keyValue.contains("kw=")){
            app.setKwFromGetOfUrl(getValueFromGet(keyValue, "kw=", "&"));
        }
        if(keyValue.contains("content=")){
            app.setContentFromGetOfUrl(getValueFromGet(keyValue,"content=", "&"));
        }
    }

    private String getScreenWide(String keyValue) {
        if(keyValue == null){
            return "null";
        }else return keyValue.toString().split("x")[0];
    }

    private String getScreenHigh(String keyValue) {
        if(keyValue == null){
            return "null";
        }else {
            try{
                return keyValue.toString().split("x")[1];
            }catch (ArrayIndexOutOfBoundsException e){
                return "null";
            }
        }
    }

    public String getBrowserFromData(String keyValue) {
        if(keyValue != null){
            if(keyValue.toString().contains("Mozilla")){
                return  "Mozilla";
            }else if(isFamousBrowsers(keyValue.toString())){
                return keyValue.toString();
            }else {
                return "Other browser";
            }
        }else {
            return "null";
        }
    }

    public void setWeekFromDateToApp(VisitToTweakBit app, Date date){
        Calendar cal = Calendar.getInstance();
        if(date != null){
            cal.setTime(date);
            int week = cal.get(Calendar.DAY_OF_WEEK);
            app.setWeekVisit(week);
        }
    }

    public void getTimeAndTimeZoneFromData(String s, VisitToTweakBit app, boolean visitToCart) {
        String[] timeVizit = getTime(s); // получаю время, часовой поис из строки со временем
        setTimeAndTimeZoneToApp(app, timeVizit, visitToCart); // сохраняю данные в visitToDU

    }

    private void setTimeAndTimeZoneToApp(VisitToTweakBit prk, String[] timeVizit, boolean cartVizit) {
        if(timeVizit != null && !cartVizit){
            prk.setVisitHourOfDay(timeVizit[2]);
            prk.setBeltTime(timeVizit[1]);
            prk.setBelt(timeVizit[0]);

            long time = Long.parseLong(timeVizit[3]);
            prk.setDateOfVizit(new Date(time * 1000));
        }else if(timeVizit !=null && cartVizit){
            prk.setCartVisitHourOfDay(timeVizit[2]);
            prk.setBeltTimeCartVisit(timeVizit[1]);
            prk.setBeltCartVisit(timeVizit[0]);

            long time = Long.parseLong(timeVizit[3]);
            prk.setDateOfViziteToCart(new Date(time * 1000));
        }
        else if(timeVizit == null && cartVizit) {
            prk.setCartVisitHourOfDay("0");
            prk.setBeltTimeCartVisit("null");
            prk.setBeltCartVisit("null");
        } else {
            prk.setVisitHourOfDay("0");
            prk.setBeltTime("null");
            prk.setBelt("null");
        }

    }

    // возвращаем ОС юзера
    public String getOsFromData(String keyValue) {
        if(!keyValue.toString().contains("Windows")){
            return "other OS";
        }else {
            return keyValue.toString();
        }
    }

    private String getValueFromGet(String paramOfGet, String startString, String endString) {
        String valueOfGet = paramOfGet;
        valueOfGet = valueOfGet.substring(valueOfGet.indexOf(startString)+startString.length(), valueOfGet.length());
        if(valueOfGet.contains(endString)) {
            valueOfGet = valueOfGet.substring(0, valueOfGet.indexOf(endString));
        }
        return valueOfGet;
    }

    public boolean isFamousBrowsers(String s) {
        boolean isFamousBrowser = false;
        String[] listBrowser = new String[6];
        listBrowser[0] = "Opera";
        listBrowser[1] = "Chrome";
        listBrowser[2] = "Internet Explorer";
        listBrowser[3] = "Firefox";
        listBrowser[4] = "Microsoft Edge";
        listBrowser[5] = "Safari";


        for(int i = 0; i < listBrowser.length; i++){
            if(s.equals(listBrowser[i])){
                isFamousBrowser = true;
                break;
            }
        }
        return isFamousBrowser;
    }

    private void parsingOfPurchase(JSONObject jsonObject, VisitToTweakBit prk) {
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyValue = jsonObject.get(keyStr);
            if(keyStr.equals("currency")){
                prk.setCurrency(keyValue.toString());
            }
            if(keyStr.equals("revenue")){
                prk.setRevenue(Double.parseDouble(keyValue.toString()));
            }
            if(keyStr.equals("time")) {
                prk.setTimeForPurchase(Long.valueOf(keyValue.toString()));
            }
            if(keyStr.equals("country")){
                prk.setCountry(keyValue.toString());
            }
            if(keyStr.equals("city")){
                prk.setCity(keyValue.toString());
            }
            if(keyStr.equals("subdivisions")){
                prk.setSubdivision(keyValue.toString());
            }
            if(keyStr.equals("zip")){
                prk.setZip(keyValue.toString());
            }
            if(keyStr.equals("time_zone")){
                prk.setTimeZone(keyValue.toString());
            }
        }

    }

    private String getLangFromUrlOfWikifixes(String value) {
        String[] prepairLang = value.split("/");
        if(prepairLang.length >= 2){
            return prepairLang[1];
        }else {
            return "null";
        }
    }

    public String[] getTime(String downloadTime){
        String[] time = new String[4];
        if(downloadTime != null) {
            //  "Fri Jul 06 2018 15:39:48 GMT+1200"
            // выделить дату до временной зоны
            // выделить время из даты, чтобы расчитывать секунды
            // отделить название пояса от временной зоны
            String onlyTime = downloadTime.substring(downloadTime.indexOf(":") - 2, downloadTime.indexOf(":") + 6);
            String prepareBelt = downloadTime.substring(downloadTime.indexOf(":") + 6);
            time[3] = getLongTimeFromTextDate(downloadTime);
            prepareBelt = prepareBelt.trim();
            String[] splitBelt = new String[2];
            if (prepareBelt.contains("-")) {
                splitBelt = prepareBelt.split("-");
                if (splitBelt.length >= 2) {
                    splitBelt[1] = "-" + splitBelt[1];
                }
            } else if (prepareBelt.contains("+")) {
                splitBelt = prepareBelt.split("\\+");
                if (splitBelt.length >= 2) {
                    splitBelt[1] = "+" + splitBelt[1];
                }
            } else if(!prepareBelt.contains("+") && !prepareBelt.contains("-")) {
                splitBelt[0] = prepareBelt;
                splitBelt[1] = "null";
            }
            if (splitBelt != null && splitBelt.length >= 2) {
                time[0] = splitBelt[0];
                time[1] = splitBelt[1];
            }

            String[] timeParse = onlyTime.split(":");
            int hourToSecond = Integer.parseInt(timeParse[0]) * 3600;
            int minToSecond = Integer.parseInt(timeParse[1]) * 60;
            int second = Integer.parseInt(timeParse[2]);
            time[2] = String.valueOf((double) (hourToSecond + minToSecond + second));
            return time;
        }else{
            return null;
        }
    }

    private String getLongTimeFromTextDate(String downloadTime){
        String dateText = downloadTime.substring(0, downloadTime.indexOf(":")+6);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
        try {
            return String.valueOf(sdf.parse(dateText).getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return "null";
        }
    }

    // сохраняю имя продукта
    public void getProduct(String value, VisitToTweakBit visitToTweakBit){
       // String driverUpdaterDownload = "/driver-updater/download";
        String driverUpdater = "/driver-updater/";
        if(safeDataForQuery){
            driverUpdater = "/land/driver-updater/";
        }
        String prk = "tweakbit.com/en/land/pc-repair/";
        String pcsup = "pc-speed-up";
        String am = "anti-malware";
        String wikiErLand = "errors/dll/";
        String wikiNoErLand = "errors/0x/";
        String pcr = "outbyte.com/en/land/pc-repair/";
        String wikiErrorsExe = "/errors/exe/";
        String wikiErrorsBDll = "errors/b/dll/";
        String wikiErrorsB0x = "errors/b/0x";

        if(value.contains(driverUpdater)){
            visitToTweakBit.setProductName("driver-updater");

        }
        if(value.contains(prk)){
            visitToTweakBit.setProductName("pc-repair-kit");
        }
        if(value.contains(pcsup)){
            visitToTweakBit.setProductName("pc-speed-up");
        }
        if(value.contains(am)){
            visitToTweakBit.setProductName("anti-malware");
        }
        if(value.contains(wikiErLand)) {
            visitToTweakBit.setProductName("pc-repair-kit");
        }
        if(value.contains(pcr)){
            visitToTweakBit.setProductName("pc-repair");
        }
        if(value.contains(wikiNoErLand) || value.contains(wikiErrorsExe)
                || value.contains(wikiErrorsBDll) || value.contains(wikiErrorsB0x))
            visitToTweakBit.setProductName("pc-repair-kit");
    }

    public String getParamFromWiki(String url){
        String param = "";
        String splitX = "/0x/";
        String splitDll = "/dll/";
        String splitExe = "/exe/";
        if(url.contains(splitX) && url.split(splitX).length >= 2){
            param = url.split(splitX)[1];
        }else if(url.contains(splitDll) && url.split(splitDll).length >=2){
            param = url.split(splitDll)[1];
        }else if(url.contains(splitExe)){
            param = url.split(splitExe)[1];
        }
        if(param.length()> 0){
            if(param.substring(param.length()-1).equals("/")){
                param = param.substring(0,param.length()-1);
            }
        }
        param = param.trim();
        return param;
    }

    public void saveToFile(VisitToTweakBit app, String separator, boolean needAddScoreToFile, boolean forTestQuery){
        Integer download = 0;
        if (needAddScoreToFile) {
            if (app.isDownload() &&
                    (app.getOs().contains("Windows")
                            || app.getOs().contains("Unknown Unknown")
                            || app.getOs().contains("undefined undefined"))) {
                toFile.append(1);
                download = 1;
            } else toFile.append(0);
            toFile.append(",");
            //дистанция до луны
            //    toFile.append(saveToString.safeOwnData(calcOfMoon.distanceOfMoon(app.getDateOfVizit())));
        }
        if (!forTestQuery) {
            toFile.append(saveToString.safeToString(app.getProductName(), Params.PRODUCT));
            //сохраняем os
            toFile.append(saveToString.safeToString(app.getOs(), Params.OS));
            //сохраняем пояс часовой
            toFile.append(saveToString.safeToString(app.getBelt(), Params.BELT));
            toFile.append(saveToString.safeToString(app.getBeltTime(), Params.TIMEBELT));
            toFile.append(saveToString.safeToString(app.getLang(), Params.LANG));
            toFile.append(saveToString.safeToString(app.getSessionCount(), Params.SESSIONS));
            toFile.append(saveToString.safeToString(app.getKwFromGetOfUrl(), Params.KW));
            toFile.append(saveToString.safeToString(app.getSize(), Params.SIZE));
            toFile.append(saveToString.safeToString(app.getSizeHigh(), Params.SIZEHIGH));
            //сохраняем дни недели
            //toFile.append(saveToString.safeToString(String.valueOf(visitToDU.getWeekVisit()), Params.WEEK));
            //сохраняем время визита "hours"
            if (app.getVisitHourOfDay() != null) {
                toFile.append(saveToString.safeOwnData(Double.valueOf(app.getVisitHourOfDay())));
            } else {
                toFile.append(saveToString.safeOwnData(-1.0));
            }
            toFile.append(saveToString.safeToString(app.getContentFromGetOfUrl(), Params.CONTENT));
            //toFile.append(saveToString.safeToString(visitToDU.getClkid(), Params.CLIKID));
            toFile.append(saveToString.safeToString(app.getMarker(), Params.MARKERS));
            //сохраняем браузеры
            toFile.append(saveToString.safeToString(app.getCountry(), Params.COUNTRY));
              toFile.append(saveToString.safeToString(app.getCity(), Params.CITY));
            // toFile.append(saveToString.safeToString(app.getSubdivision(), Params.SUBDIVISION));
             // toFile.append(saveToString.safeToString(app.getZip(), Params.ZIP));
              toFile.append(saveToString.safeToString(app.getTimeZone(), Params.TIMEZONE));
            toFile.append(saveToString.safeToString(app.getLocalLang(), Params.LOCALLANG));
            toFile.append(saveToString.safeOwnData(app.getLoadSite()));
            toFile.append(saveToString.safeToString(app.getBrowser(), Params.BROWSER));
            toFile.append(separator);
        } else {
            if (separator.equals(",")) {
                //язык для DU. для wikifixes язык сохрянть из url
                toFile.append("&").append("lang=").append(app.getLang());
                toFile.append("&").append("sessions=").append(app.getSessionCount());
                //здесь сохраняется время визита в секундах, часовой пояс, название пояса и дата
                toFile.append("&").append("time=").append(app.getAUIDFromateTime());
                toFile.append("&").append("os=").append(app.getOs());
                toFile.append("&").append("browser=").append(app.getBrowser());
                toFile.append("&").append("screenSize=").append(app.getSize()).append("x").append(app.getSizeHigh());
                toFile.append("&").append("marker=").append(app.getMarker());
                toFile.append("&").append("kw=").append(app.getKwFromGetOfUrl());
                toFile.append("&").append("content=").append(app.getContentFromGetOfUrl());
                toFile.append("&");
            } else {
                //язык для DU. для wikifixes язык сохрянть из url
                toFile.append("&").append("pr_lang=").append(app.getLang());
                toFile.append("&").append("pr_sessions=").append(app.getSessionCount());
                //здесь сохраняется время визита в секундах, часовой пояс, название пояса и дата
                toFile.append("&").append("pr_time=").append(app.getAUIDFromateTime());
                toFile.append("&").append("pr_os=").append(app.getOs());
                toFile.append("&").append("pr_browser=").append(app.getBrowser());
                toFile.append("&").append("pr_screenSize=").append(app.getSize()).append("x").append(app.getSizeHigh());
                toFile.append("&").append("pr_marker=").append(app.getMarker());
                toFile.append("&").append("pr_kw=").append(app.getKwFromGetOfUrl());
                toFile.append("&").append("pr_content=").append(app.getContentFromGetOfUrl());
                toFile.append(separator);
            }
        }
    }

    public void savePurchaseToFile(VisitToTweakBit app, String separator,
                                   boolean needAddScoreToFile, boolean forTestQuery, StringBuilder toFile){
        if(app.getCartVisitHourOfDay() != null && app.getTimeForPurchase() > 0){

            double visitToCart = Double.valueOf(app.getCartVisitHourOfDay());
            long dayOfVisitToCart = app.getDateOfViziteToCart().getTime() + (long) visitToCart;

            toFile.append(saveToString.safeToString(app.getOs(), Params.OS));
            toFile.append(saveToString.safeToString(app.getKwFromGetOfUrl(),Params.KW));
            toFile.append(saveToString.safeToString(app.getBrowser(), Params.BROWSER));
            toFile.append(separator);
        }
    }


    public void setSafeDataForQuery(Boolean safeDataForQuery) {
        this.safeDataForQuery = safeDataForQuery;
        fileForQuery = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\fileForQuery");
    }

    public Boolean isSaveDataForQuery() {
        return safeDataForQuery;
    }

    public void saveToFile(StringBuilder toFile) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileForQuery));
            writer.write(toFile.toString());
        } finally {
            if(writer != null){
                writer.close();
            }
        }


    }
}

