package com.tweakbit.controller;

import com.CalcOfMoon;
import com.tweakbit.driverupdater.model.enties.ProductTweakBit;
import com.tweakbit.model.Params;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataParser {
    StringBuilder toFile;
    SaveToString saveToString;
    CalcOfMoon calcOfMoon;
    TreeMap<Params,TreeMap<String,Integer>> labelsOfTree;
    ProductTweakBit app;
    File fileForQuery;

    Boolean safeDataForQuery;

    public DataParser(StringBuilder toFile) {
        this.toFile = toFile;
        this.saveToString = new SaveToString();
        this.calcOfMoon = new CalcOfMoon();
        this.safeDataForQuery = false;
    }

    public DataParser(StringBuilder toFile, ProductTweakBit app, TreeMap<Params,
            TreeMap<String, Integer>> labelsOfTree, HttpServletRequest req) {
        this.toFile = toFile;
        this.labelsOfTree = labelsOfTree;
        this.app = app;
        this.saveToString = new SaveToString(labelsOfTree);
        this.calcOfMoon = new CalcOfMoon();
        this.safeDataForQuery = false;
        saveDataToAppFromHttp(this.app, req);
        saveToFile(app);
    }

    public SaveToString getSaveToString() {
        return saveToString;
    }

    public StringBuilder getToFile() {
        return toFile;
    }

    private void saveDataToAppFromHttp(ProductTweakBit app, HttpServletRequest req) {
       //язык для DU. для wikifixes язык сохрянть из url
        app.setLang(req.getParameter("lang"));
        app.setSessionCount(req.getParameter("sessions"));
        //здесь сохраняется время визита в секундах, часовой пояс и название пояса
        getTimeAndTimeZoneFromData(req.getParameter("time"), app);
        app.setOs(req.getParameter("os"));
        app.setClkid(req.getParameter("clkid"));
        app.setBrowser(getBrowserFromData(req.getParameter("browser")));
        app.setSize(getScreenWide(req.getParameter("screenSize")));
        app.setSizeHigh(getScreenHigh(req.getParameter("screenSize")));
        app.setMarker(req.getParameter("marker"));
        app.setKwFromGetOfUrl(req.getParameter("kw"));
        app.setContentFromGetOfUrl(req.getParameter("content"));
        app.setDateOfVizit(new Date()); // TODO нормальный парсер даты и получить из него дату, belt, timeBelt
        setWeekFromDateToApp(app, app.getDateOfVizit());
        app.setDownload(false);
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

    public void parsingOfSession(JSONObject jsonObject, ProductTweakBit app, Date date, int sessionCount) {
        if(safeDataForQuery  && jsonObject.toString().contains("/land/driver-updater/")){
            if((jsonObject.has("downloadTime") || app.isDownload())){
                toFile.append(1).append("@");
            }else {
                toFile.append(0).append("@");
            }
            toFile.append("&sessions=").append(sessionCount);
        }
        setWeekFromDateToApp(app, date);
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyValue = jsonObject.get(keyStr);
            if(safeDataForQuery && jsonObject.toString().contains("/land/driver-updater/")){
                if(!keyStr.equals("get") && !keyStr.equals("url") && !keyStr.equals("clientId")
                        && !keyStr.equals("gclid") && !keyStr.equals("clkid") && !key.equals("msclkid")
                        && !keyStr.equals("purchase")){
                        toFile.append("&").append(keyStr).append("=").append(keyValue.toString());
                    }
                    if(keyValue.toString().contains("kw=")){
                        toFile.append("&").append("kw=")
                                .append(getValueFromGet(keyValue.toString(), "kw=", "&"));
                    }
                    if(keyValue.toString().contains("content=")){
                        toFile.append("&").append("content=")
                                .append(getValueFromGet(keyValue.toString(),"content=", "&"));
                    }
            } else {
                if (keyStr.equals("url")) {
                    String value = keyValue.toString();
                    getProduct(value, app);
                    // сохраняю имя ошибки из wikifixes из рекламы
                    app.setKeyError(getParamFromWiki(value));
                }

                if (keyStr.equals("lang")) {
                    app.setLang(keyValue.toString());
                }

                if (keyStr.equals("downloadTime")) {
                    app.setDownload(true);
                    app.setDownloadHourOfDay(getTime(keyValue.toString())[2]);
                }
                if (keyStr.equals("time")) {
                    getTimeAndTimeZoneFromData(keyValue.toString(), app);
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
                    app.setMarker(keyValue.toString());
                }

                if (keyStr.equals("get")) {
                    setGetToAppFromData(app, keyValue.toString());

                }
            }
        }
        if(safeDataForQuery){
            toFile.append("\n");
        }
    }

    private void setGetToAppFromData(ProductTweakBit app, String keyValue) {
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
        }else return keyValue.toString().split("x")[1];
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

    public void setWeekFromDateToApp(ProductTweakBit app, Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        app.setWeekVisit(week);
    }

    public void getTimeAndTimeZoneFromData(String s, ProductTweakBit app) {
        String[] timeVizit = getTime(s); // получаю время, часовой поис из строки со временем
        setTimeAndTimeZoneToApp(app, timeVizit); // сохраняю данные в app

    }

    private void setTimeAndTimeZoneToApp(ProductTweakBit prk, String[] timeVizit) {
        if(timeVizit !=null){
            prk.setVisitHourOfDay(timeVizit[2]);
            prk.setBeltTime(timeVizit[1]);
            prk.setBelt(timeVizit[0]);
        }else {
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

    private void parsingOfPurchase(JSONObject jsonObject, ProductTweakBit prk) {
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyValue = jsonObject.get(keyStr);
            if(keyStr.equals("currency")){
                prk.setCurrency(keyValue.toString());
            }
            if(keyStr.equals("revenue")){
                prk.setRevenue(Double.parseDouble(keyValue.toString()));
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
        String[] time = new String[3];
        if(downloadTime != null) {
            //  "Fri Jul 06 2018 15:39:48 GMT+1200"
            String timeEndPart = downloadTime.substring(downloadTime.indexOf(":") - 2);
            String onlyTime = timeEndPart.substring(0, timeEndPart.indexOf(" "));
            String prepareBelt = timeEndPart.substring(timeEndPart.indexOf(" "));
            prepareBelt = prepareBelt.trim();
            String[] splitBelt = null;
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
            } else {
                splitBelt = prepareBelt.split(" ");
                if (splitBelt.length >= 2) {
                    splitBelt[1] = "+" + splitBelt[1];
                }
            }
            if (splitBelt != null && splitBelt.length >= 2) {
                time[1] = splitBelt[1];
                time[0] = splitBelt[0];
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

    // сохраняю имя продукта
    public void getProduct(String value, ProductTweakBit productTweakBit){
        String druverUpdater = "/driver-updater/";
        if(safeDataForQuery){
            druverUpdater = "/land/driver-updater/";
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

        if(value.contains(druverUpdater)){
            productTweakBit.setProductName("du");
        }
        if(value.contains(prk)){
            productTweakBit.setProductName("prk");
        }
        if(value.contains(pcsup)){
            productTweakBit.setProductName("pcsup");
        }
        if(value.contains(am)){
            productTweakBit.setProductName("am");
        }
        if(value.contains(wikiErLand)) {
            productTweakBit.setProductName("prk-wiki");
        }
        if(value.contains(pcr)){
            productTweakBit.setProductName("pcr");
        }
        if(value.contains(wikiNoErLand) || value.contains(wikiErrorsExe)
                || value.contains(wikiErrorsBDll) || value.contains(wikiErrorsB0x))
            productTweakBit.setProductName("prk-wiki");
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

    public void saveToFile(ProductTweakBit app){
        if (app.isDownload() &&
                (app.getOs().contains("Windows")
                        || app.getOs().contains("Unknown Unknown")
                        || app.getOs().contains("undefined undefined"))) {
            toFile.append(1);
        } else toFile.append(0);
        toFile.append(",");

        //сохраняем os
        toFile.append(saveToString.safeToString(app.getOs(), Params.OS));
        //сохраняем пояс часовой*/
        toFile.append(saveToString.safeToString(app.getBelt(), Params.BELT));
        toFile.append(saveToString.safeToString(app.getBeltTime(), Params.TIMEBELT));
        toFile.append(saveToString.safeToString(app.getLang(), Params.LANG));
        toFile.append(saveToString.safeToString(app.getSessionCount(), Params.SESSIONS));
        toFile.append(saveToString.safeToString(app.getKwFromGetOfUrl(), Params.KW));
        toFile.append(saveToString.safeToString(app.getSize(), Params.SIZE));
        toFile.append(saveToString.safeToString(app.getSizeHigh(), Params.SIZEHIGH));
        //сохраняем дни недели
        //toFile.append(saveToString.safeToString(String.valueOf(app.getWeekVisit()), Params.WEEK));
        //сохраняем время визита "hours"
        toFile.append(saveToString.safeOwnData(Double.valueOf(app.getVisitHourOfDay())));
        toFile.append(saveToString.safeToString(app.getContentFromGetOfUrl(), Params.CONTENT));
        //toFile.append(saveToString.safeToString(app.getClkid(), Params.CLIKID));
        toFile.append(saveToString.safeToString(app.getMarker(), Params.MARKERS));
      //  Date azimutDate = new Date(app.getDateOfVizit().getTime() + Double.valueOf(app.getVisitHourOfDay()).intValue());
        //дистанция до луны
     //   toFile.append(saveToString.safeOwnData(calcOfMoon.distanceOfMoon(azimutDate)));
        //сохраняем браузеры
        toFile.append(saveToString.safeToString(app.getBrowser(), Params.BROWSER));
        toFile.append("\n");
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

