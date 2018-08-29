package com.tweakbit.controller;

import com.CalcOfMoon;
import com.tweakbit.driverupdater.model.enties.VisitToTweakBit;
import com.tweakbit.model.Params;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TreeMap;

public class DataParser {
    StringBuilder toFile;
    SaveToString saveToString;
    CalcOfMoon calcOfMoon;
    TreeMap<Params,TreeMap<String,Integer>> labelsOfTree;
    VisitToTweakBit visitToDU;
    File fileForQuery;
    Boolean safeDataForQuery;

    public DataParser() {
    }

    public DataParser(StringBuilder toFile) {
        this.toFile = toFile;
        this.saveToString = new SaveToString();
        this.calcOfMoon = new CalcOfMoon();
        this.safeDataForQuery = false;
    }

    public DataParser(StringBuilder toFile, VisitToTweakBit visitToDU, TreeMap<Params,
            TreeMap<String, Integer>> labelsOfTree, HttpServletRequest req) {
        this.toFile = toFile;
        this.labelsOfTree = labelsOfTree;
        this.visitToDU = visitToDU;
        this.saveToString = new SaveToString(labelsOfTree);
        this.calcOfMoon = new CalcOfMoon();
        this.safeDataForQuery = false;
        saveDataToAppFromHttp(this.visitToDU, req);

        VisitToTweakBit previousVisit = getPreviousVisit(this.visitToDU.getSessionCount(), req);
        saveToFile(visitToDU, ",", true, false);
        saveToFile(previousVisit, "\n", false, false);
    }

    public SaveToString getSaveToString() {
        return saveToString;
    }

    public StringBuilder getToFile() {
        return toFile;
    }

    private void saveDataToAppFromHttp(VisitToTweakBit app, HttpServletRequest req) {
       //язык для DU. для wikifixes язык сохрянть из url
        app.setLang(req.getParameter("lang"));
        app.setSessionCount(req.getParameter("sessions"));
        //здесь сохраняется время визита в секундах, часовой пояс, название пояса и дата
        getTimeAndTimeZoneFromData(req.getParameter("time"), app);
        app.setOs(req.getParameter("os"));
        app.setClkid(req.getParameter("clkid"));
        app.setBrowser(getBrowserFromData(req.getParameter("browser")));
        app.setSize(getScreenWide(req.getParameter("screenSize")));
        app.setSizeHigh(getScreenHigh(req.getParameter("screenSize")));
        app.setMarker(req.getParameter("marker"));
        app.setKwFromGetOfUrl(req.getParameter("kw"));
        app.setContentFromGetOfUrl(req.getParameter("content"));
        setWeekFromDateToApp(app, app.getDateOfVizit());
        app.setDownload(false);
    }

    private VisitToTweakBit getPreviousVisit(String sessionCount, HttpServletRequest req) {

        if(Integer.valueOf(sessionCount) == 1){
            return new VisitToTweakBit();
        }else {
            VisitToTweakBit app = new VisitToTweakBit();
            //язык для DU. для wikifixes язык сохрянть из url
            app.setLang(req.getParameter("pr_lang"));
            app.setSessionCount(req.getParameter("pr_sessions"));
            //здесь сохраняется время визита в секундах, часовой пояс, название пояса и дата
            getTimeAndTimeZoneFromData(req.getParameter("pr_time"), app);
            app.setOs(req.getParameter("pr_os"));
            app.setClkid(req.getParameter("pr_clkid"));
            app.setBrowser(getBrowserFromData(req.getParameter("pr_browser")));
            app.setSize(getScreenWide(req.getParameter("pr_screenSize")));
            app.setSizeHigh(getScreenHigh(req.getParameter("pr_screenSize")));
            app.setMarker(req.getParameter("pr_marker"));
            app.setKwFromGetOfUrl(req.getParameter("pr_kw"));
            app.setContentFromGetOfUrl(req.getParameter("pr_content"));
            setWeekFromDateToApp(app, app.getDateOfVizit());
            app.setDownload(false);
            return app;
        }
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

    public void parsingOfSession(JSONObject jsonObject, VisitToTweakBit app, Date date, int sessionCount) {
        setWeekFromDateToApp(app, date);
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyValue = jsonObject.get(keyStr);

            if (keyStr.equals("url")) {
                String value = keyValue.toString();
                getProduct(value, app);
                // сохраняю имя ошибки из wikifixes из рекламы
                app.setKeyError(getParamFromWiki(value));
                app.setUrl(value);
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
                app.setMarker(keyValue.toString());
            }

            if (keyStr.equals("get")) {
                setGetToAppFromData(app, keyValue.toString());
            }
        }
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
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        app.setWeekVisit(week);
    }

    public void getTimeAndTimeZoneFromData(String s, VisitToTweakBit app) {
        String[] timeVizit = getTime(s); // получаю время, часовой поис из строки со временем
        setTimeAndTimeZoneToApp(app, timeVizit); // сохраняю данные в visitToDU

    }

    private void setTimeAndTimeZoneToApp(VisitToTweakBit prk, String[] timeVizit) {
        if(timeVizit !=null){
            prk.setVisitHourOfDay(timeVizit[2]);
            prk.setBeltTime(timeVizit[1]);
            prk.setBelt(timeVizit[0]);
            long time = Long.parseLong(timeVizit[3]);
            prk.setDateOfVizit(new Date(time));
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
          //  System.out.println(downloadTime + "| " + time[0] + " " + time[1] + " " + time[2]);
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
            visitToTweakBit.setProductName("du");
        }
        if(value.contains(prk)){
            visitToTweakBit.setProductName("prk");
        }
        if(value.contains(pcsup)){
            visitToTweakBit.setProductName("pcsup");
        }
        if(value.contains(am)){
            visitToTweakBit.setProductName("am");
        }
        if(value.contains(wikiErLand)) {
            visitToTweakBit.setProductName("prk-wiki");
        }
        if(value.contains(pcr)){
            visitToTweakBit.setProductName("pcr");
        }
        if(value.contains(wikiNoErLand) || value.contains(wikiErrorsExe)
                || value.contains(wikiErrorsBDll) || value.contains(wikiErrorsB0x))
            visitToTweakBit.setProductName("prk-wiki");
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
        if(needAddScoreToFile){
            if (app.isDownload() &&
                    (app.getOs().contains("Windows")
                            || app.getOs().contains("Unknown Unknown")
                            || app.getOs().contains("undefined undefined"))) {
                toFile.append(1);
            } else toFile.append(0);
            toFile.append(",");
            //дистанция до луны
        //    toFile.append(saveToString.safeOwnData(calcOfMoon.distanceOfMoon(app.getDateOfVizit())));
        }
        if(!forTestQuery){
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
           // toFile.append(saveToString.safeToString(app.getUrl(), Params.URL));
            //сохраняем дни недели
            //toFile.append(saveToString.safeToString(String.valueOf(visitToDU.getWeekVisit()), Params.WEEK));
            //сохраняем время визита "hours"
            if(app.getVisitHourOfDay()!= null){
                toFile.append(saveToString.safeOwnData(Double.valueOf(app.getVisitHourOfDay())));
            }else {
                toFile.append(saveToString.safeOwnData(-1.0));
            }
            toFile.append(saveToString.safeToString(app.getContentFromGetOfUrl(), Params.CONTENT));
            //toFile.append(saveToString.safeToString(visitToDU.getClkid(), Params.CLIKID));
            toFile.append(saveToString.safeToString(app.getMarker(), Params.MARKERS));
            //сохраняем браузеры
            toFile.append(saveToString.safeToString(app.getBrowser(), Params.BROWSER));
            toFile.append(separator);
        }else {
            if(separator.equals(",")){
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
               // toFile.append("&").append("url=").append(app.getContentFromGetOfUrl());
                toFile.append("&");
            }else{
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
              //  toFile.append("&").append("pr_url=").append(app.getContentFromGetOfUrl());
                toFile.append(separator);
            }
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

