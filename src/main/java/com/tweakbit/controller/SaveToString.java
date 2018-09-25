package com.tweakbit.controller;

import com.tweakbit.model.Params;

import java.io.*;
import java.util.TreeMap;

public class SaveToString {
    public  TreeMap<String, Integer> os, browser, errors, hours, belt, timeBelt, lang, sessions, size, kw, content,
            clickId, marker, clientId, week, sizeHigh, url, country, city, subdivision, zip, timeZone, localLang, productName;
    public int osCount, browserCount, errorsCount, hoursCount, beltCount, timeBeltCount, langCount, contentCount,
            sessionCount, sizeCount, kwCount, clickIdCount, markerCount, clientIdCount, weekCount, sizeHighCount, urlCount,
    countryCount, cityCount, subdivisionCount, zipCount, timeZoneCount, localLangCount, productNameCount;
    public  TreeMap<Params, TreeMap<String,Integer>> listOfTrees;

    public SaveToString(TreeMap<Params, TreeMap<String, Integer>> listOfTrees) {
        this.listOfTrees = listOfTrees;
        os = listOfTrees.get(Params.OS);
        browser = listOfTrees.get(Params.BROWSER);
        errors = listOfTrees.get(Params.ERRORS);
        hours = listOfTrees.get(Params.HOURS);
        belt = listOfTrees.get(Params.BELT);
        timeBelt = listOfTrees.get(Params.TIMEBELT);
        lang = listOfTrees.get(Params.LANG);
        sessions = listOfTrees.get(Params.SESSIONS);
        size = listOfTrees.get(Params.SIZE);
        kw = listOfTrees.get(Params.KW);
        content = listOfTrees.get(Params.CONTENT);
        clickId = listOfTrees.get(Params.CLIKID);
        marker = listOfTrees.get(Params.MARKERS);
        clientId = listOfTrees.get(Params.CLIENTID);
        week = listOfTrees.get(Params.WEEK);
        sizeHigh = listOfTrees.get(Params.SIZEHIGH);
        url = listOfTrees.get(Params.URL);
        country = listOfTrees.get(Params.COUNTRY);
        city = listOfTrees.get(Params.CITY);
        subdivision = listOfTrees.get(Params.SUBDIVISION);
        zip = listOfTrees.get(Params.ZIP);
        timeZone  = listOfTrees.get(Params.TIMEZONE);
        localLang = listOfTrees.get(Params.LOCALLANG);
        productName = listOfTrees.get(Params.PRODUCT);
     }

    public SaveToString() {
        listOfTrees = new TreeMap<>();
        os = new TreeMap<>();
        browser = new TreeMap<>();
        errors = new TreeMap<>();
        hours = new TreeMap<>();
        belt = new TreeMap<>();
        timeBelt = new TreeMap<>();
        lang = new TreeMap<>();
        sessions = new TreeMap<>();
        size = new TreeMap<>();
        kw = new TreeMap<>();
        content = new TreeMap<>();
        clickId = new TreeMap<>();
        marker = new TreeMap<>();
        clientId = new TreeMap<>();
        week = new TreeMap<>();
        sizeHigh = new TreeMap<>();
        url = new TreeMap<>();
        country = new TreeMap<>();
        city = new TreeMap<>();
        subdivision = new TreeMap<>();
        zip = new TreeMap<>();
        timeZone = new TreeMap<>();
        localLang = new TreeMap<>();
        productName = new TreeMap<>();

        osCount = 0;
        browserCount = 0;
        errorsCount = 0;
        hoursCount = 0;
        beltCount = 0;
        timeBeltCount = 0;
        langCount = 0;
        sessionCount = 0;
        sizeCount = 0;
        kwCount = 0;
        contentCount = 0;
        clickIdCount = 0;
        markerCount = 0;
        clientIdCount = 0;
        weekCount = 0;
        sizeHighCount = 0;
        urlCount = 0;
        countryCount = 0;
        cityCount = 0;
        subdivisionCount = 0;
        zipCount = 0;
        timeZoneCount = 0;
        localLangCount = 0;
        productNameCount = 0;
    }

    public String safeOwnData(Double keyParams){
        if(keyParams == null){
            return "null,";
        }else
        return keyParams + ",";
    }

    public String safeToString(String keyForPredict, Params keyParams) {
        String file = null;

        if(keyForPredict == null){
            keyForPredict = "null";
        }

        if(keyParams.equals(Params.OS)){
           file = addParamToString(os, keyForPredict, Params.OS);
        }
        if(keyParams.equals(Params.BROWSER)){
           file = addParamToString(browser, keyForPredict, Params.BROWSER);
        }
        if(keyParams.equals(Params.ERRORS)){
           file = addParamToString(errors, keyForPredict, Params.ERRORS);
        }
        if(keyParams.equals(Params.HOURS)){
           file = addParamToString(hours, keyForPredict, Params.HOURS);
        }
        if(keyParams.equals(Params.BELT)){
           file = addParamToString(belt, keyForPredict, Params.BELT);
        }
        if(keyParams.equals(Params.TIMEBELT)){
           file = addParamToString(timeBelt, keyForPredict, Params.TIMEBELT);
        }
        if(keyParams.equals(Params.LANG)){
           file = addParamToString(lang, keyForPredict, Params.LANG);
        }
        if(keyParams.equals(Params.SESSIONS)){
           file = addParamToString(sessions, keyForPredict, Params.SESSIONS);
        }
        if(keyParams.equals(Params.SIZE)){
           file = addParamToString(size, keyForPredict, Params.SIZE);
        }
        if(keyParams.equals(Params.KW)){
           file = addParamToString(kw, keyForPredict, Params.KW);
        }
        if(keyParams.equals(Params.CONTENT)){
           file = addParamToString(content, keyForPredict, Params.CONTENT);
        }
        if(keyParams.equals(Params.CLIKID)){
           file = addParamToString(clickId, keyForPredict, Params.CLIKID);
        }
        if(keyParams.equals(Params.MARKERS)){
           file = addParamToString(marker, keyForPredict, Params.MARKERS);
        }
        if(keyParams.equals(Params.WEEK)){
           file = addParamToString(week, keyForPredict, Params.WEEK);
        }
        if(keyParams.equals(Params.SIZEHIGH)){
            file = addParamToString(sizeHigh, keyForPredict, Params.SIZEHIGH);
        }
        if(keyParams.equals(Params.URL)){
            file = addParamToString(url, keyForPredict, Params.URL);
        }
        if(keyParams.equals(Params.COUNTRY)){
            file = addParamToString(country, keyForPredict, Params.COUNTRY);
        }
        if(keyParams.equals(Params.CITY)){
            file = addParamToString(city, keyForPredict, Params.CITY);
        }
        if(keyParams.equals(Params.SUBDIVISION)){
            file = addParamToString(subdivision, keyForPredict, Params.SUBDIVISION);
        }
        if(keyParams.equals(Params.ZIP)){
            file = addParamToString(zip, keyForPredict, Params.ZIP);
        }
        if(keyParams.equals(Params.TIMEZONE)){
            file = addParamToString(timeZone, keyForPredict, Params.TIMEZONE);
        }
        if(keyParams.equals(Params.LOCALLANG)){
            file = addParamToString(localLang, keyForPredict, Params.LOCALLANG);
        }
        if(keyParams.equals(Params.PRODUCT)){
            file = addParamToString(productName, keyForPredict, Params.PRODUCT);
        }
        return file;
    }

    private String addParamToString(TreeMap<String,Integer> treeMap, String keyForPredict, Params keyParams){
        StringBuilder string = new StringBuilder();
        if(findInMap(keyForPredict, treeMap)){
            string.append(treeMap.get(keyForPredict));
        }else{
            addFileToErrorsMap(keyForPredict, treeMap, keyParams);
            string.append(treeMap.get(keyForPredict));
        }
        if(keyParams.equals(Params.BROWSER)){
            string.append("");
        }else {
            string.append(",");
        }
        return string.toString();
    }

    private void addFileToErrorsMap(String keyError, TreeMap<String, Integer> treeMap, Params params) {
        switch (params){
            case OS: osCount++;
                treeMap.put(keyError, osCount);
                break;
            case BROWSER: browserCount++;
                treeMap.put(keyError, browserCount);
                break;
            case ERRORS: errorsCount++;
                treeMap.put(keyError, errorsCount);
                break;
            case HOURS: hoursCount++;
                treeMap.put(keyError, hoursCount);
                break;
            case BELT: beltCount++;
                treeMap.put(keyError, beltCount);
                break;
            case TIMEBELT: timeBeltCount++;
                treeMap.put(keyError, timeBeltCount);
                break;
            case LANG: langCount++;
                treeMap.put(keyError, langCount);
                break;
            case SESSIONS: sessionCount++;
                treeMap.put(keyError, sessionCount);
                break;
            case SIZE: sizeCount++;
                treeMap.put(keyError, sizeCount);
                break;
            case KW: kwCount++;
                treeMap.put(keyError, kwCount);
                break;
            case CONTENT: contentCount++;
                treeMap.put(keyError, contentCount);
                break;
            case CLIKID: clickIdCount++;
                treeMap.put(keyError, clickIdCount);
                break;
            case MARKERS: markerCount++;
                treeMap.put(keyError, markerCount);
                break;
            case CLIENTID: clientIdCount++;
                treeMap.put(keyError, clientIdCount);
                break;
            case WEEK: weekCount++;
                treeMap.put(keyError, weekCount);
                break;
            case SIZEHIGH: sizeHighCount++;
                treeMap.put(keyError, sizeHighCount);
                break;
            case URL: urlCount++;
                treeMap.put(keyError, urlCount);
                break;
            case COUNTRY: countryCount++;
                treeMap.put(keyError, countryCount);
                break;
            case CITY: cityCount++;
                treeMap.put(keyError, cityCount);
                break;
            case SUBDIVISION: subdivisionCount++;
                treeMap.put(keyError, subdivisionCount);
                break;
            case ZIP: zipCount++;
                treeMap.put(keyError, zipCount);
                break;
            case TIMEZONE: timeZoneCount++;
                treeMap.put(keyError, timeZoneCount);
                break;
            case LOCALLANG: localLangCount++;
                treeMap.put(keyError, localLangCount);
                break;
            case PRODUCT: productNameCount++;
                treeMap.put(keyError, productNameCount);
                break;
        }
    }

    public static boolean findInMap(String keyError, TreeMap<String, Integer> treeMap){
        if(keyError == null){
            keyError = "";
        }
        if(treeMap.containsKey(keyError)){
            return true;
        }else {
            return false;
        }
    }

    private File getFileFromURL(String nameOfFile) {
        File folder = new File("src\\main\\resources\\duparams\\" + nameOfFile);
        return folder;
    }

    public void safeMapsOfKeysToFile() {
        File folder = getFileFromURL(String.valueOf("treesOfParams.txt"));
        System.out.println(os.size());
        listOfTrees.put(Params.OS, os);
        listOfTrees.put(Params.BROWSER,browser);
        listOfTrees.put(Params.ERRORS, errors);
        listOfTrees.put(Params.HOURS, hours);
        listOfTrees.put(Params.BELT, belt);
        listOfTrees.put(Params.TIMEBELT, timeBelt);
        listOfTrees.put(Params.LANG, lang);
        listOfTrees.put(Params.SESSIONS, sessions);
        listOfTrees.put(Params.SIZE,size);
        listOfTrees.put(Params.KW, kw);
        listOfTrees.put(Params.CONTENT, content);
        listOfTrees.put(Params.CLIKID, clickId);
        listOfTrees.put(Params.MARKERS, marker);
        listOfTrees.put(Params.CLIENTID, clientId);
        listOfTrees.put(Params.WEEK, week);
        listOfTrees.put(Params.SIZEHIGH, sizeHigh);
        listOfTrees.put(Params.URL, url);
        listOfTrees.put(Params.COUNTRY, country);
        listOfTrees.put(Params.CITY, city);
        listOfTrees.put(Params.SUBDIVISION, subdivision);
        listOfTrees.put(Params.ZIP, zip);
        listOfTrees.put(Params.TIMEZONE, timeZone);
        listOfTrees.put(Params.LOCALLANG, localLang);
        listOfTrees.put(Params.PRODUCT, productName);

        try {
            FileOutputStream fos = new FileOutputStream(folder);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listOfTrees);
            oos.close();
            fos.close();
            System.out.println(listOfTrees);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
