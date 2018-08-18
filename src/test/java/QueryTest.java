import com.tweakbit.controller.SaveToString;
import com.tweakbit.driverupdater.model.enties.ProductTweakBit;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QueryTest {
    static StringBuilder toFile = new StringBuilder();
    static String timeBelt = null, testData;
    static String belt = null, lang = null;
    static SaveToString saveToString = new SaveToString();
    static List<ProductTweakBit> listOfPrk;
    static int countRows;
    static int numImputs;

    public static String sendUrl(String[] valueParams, String[] keyParams) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder("http://localhost:8080/vanga-vanga/get_predict_for_du");
        for(int i = 0; i < keyParams.length; i++){
            builder.setParameter(keyParams[i], valueParams[i]);
        }

        HttpGet request = new HttpGet(builder.build());
        HttpClient client = HttpClientBuilder.create().build();

        HttpResponse response = client.execute(request);
        String responseString = new BasicResponseHandler().handleResponse(response);
        return responseString;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        listOfPrk = new ArrayList<>();
        File file = new File("C:\\Users\\ilyav\\Downloads\\files\\2018-08-13.txt");
        StringBuffer stringBuffer = new StringBuffer();
        Date date = getDateFromFileName("2018-08-13.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            line = line.replaceAll("\\+", "%2B");
            line = URLDecoder.decode(line, "UTF-8");
            String[] splitLine = line.split("\t");
            //сохраняю ауид без сессии
            try{
                JSONObject obj = new JSONObject(splitLine[1]);
                // находим сессию пользователя
                String session = obj.getJSONObject("session").toString();
                JSONObject sessionObj = new JSONObject(session);
                //перебираем сессии юзера
                checkEachSession(sessionObj, splitLine[0], new Date());
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        printPredicion();
        fileReader.close();
    }

    private static Date getDateFromFileName(String s) {
        String split = s.substring(0, s.length()-4);
        SimpleDateFormat formate = new SimpleDateFormat("YYYY-dd-MM");
        Date date = null;
        try {
            date = formate.parse(split);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
    // сохраняю имя продукта
    public static void getProduct(String value, ProductTweakBit productTweakBit){
        String driverUpdater = "driver-updater";
        String driverUpdaterDownload = "tweakbit.com/driver-updater/download";
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
            productTweakBit.setProductName("du");
        }
        if(value.contains(driverUpdaterDownload)){
            productTweakBit.setProductName("duDownload");
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

    public static void checkEachSession(JSONObject jsonObj, String auid, Date date) throws IOException, URISyntaxException {
        for (Object key : jsonObj.keySet()) {
            //based on you key types
            String keyStr = (String) key;
            Object keyValue = jsonObj.get(keyStr);

            if (keyValue instanceof JSONObject) {
                ProductTweakBit prk = new ProductTweakBit(auid + "." + keyStr);
                // сохраняю кол-во сессий
                prk.setSessionCount(String.valueOf((((String) key).length()-1)));
                parsingOfSession((JSONObject) keyValue, prk);

                if(prk.getVisitHourOfDay() != null
                        && prk.getProductName() != null
                        && prk.getProductName().equals("du")
                        ){

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int week = cal.get(Calendar.WEEK_OF_YEAR);
                    prk.setWeekVisit(week);
                    listOfPrk.add(prk);
                }
            }
        }
    }
    public static void printPredicion() throws IOException, URISyntaxException {
        PrintWriter out = new PrintWriter("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\test\\java\\resp.txt");
        StringBuilder builder = new StringBuilder();
        String[] keyParams = createKeyParams();
        System.out.println(listOfPrk.size());
        for(int i = 0; i < 300; i++){
                        /*
                        sort a   table for predict
                        os
                        belt
                        timeBelt
                        lang
                        sessions
                        size
                        hours
                        browser
                        */
            String[] value = createValue(listOfPrk.get(i));
            builder.append(sendUrl(value, keyParams));
            builder.append("\n");
        }
        out.print(builder.toString());
        out.close();
    }

    private static String[] createValue(ProductTweakBit productTweakBit) {
        String[] value = new String[8];
        value[0] = productTweakBit.getOs();
        value[1] = productTweakBit.getBelt();
        value[2] = productTweakBit.getBeltTime();
        value[3] = productTweakBit.getLang();
        value[4] = productTweakBit.getSessionCount();
        value[5] = productTweakBit.getSize();
        value[6] = productTweakBit.getVisitHourOfDay();
        value[7] = productTweakBit.getBrowser();
        for (int i = 0; i < value.length; i++){
           if(value[i] == null){
               value[i] = "null";
           }
        }
        return value;
    }

    private static String[] createKeyParams() {
        String[] keyParams = new String[8];
        keyParams[0] = "os";
        keyParams[1] = "belt";
        keyParams[2] = "timeBelt";
        keyParams[3] = "lang";
        keyParams[4] = "sessions";
        keyParams[5] = "size";
        keyParams[6] = "hours";
        keyParams[7] = "browser";

        return keyParams;
    }

    private static String getLangFromUrl(String value) {
        lang = null;
        String[] prepairLang = value.split("/");
        if(prepairLang.length >= 2){
            lang = prepairLang[1];
        }
        return lang;
    }

    private static void parsingOfSession(JSONObject jsonObject, ProductTweakBit prk) {
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyValue = jsonObject.get(keyStr);

            if (keyStr.equals("url")) {
                String value = keyValue.toString();
                //      if (value.contains("/driver-updater/")) {
                //сохраняю имя продукта
                getProduct(value, prk);
                // сохраняю имя параметра из рекламы
                prk.setKeyError(getParamFromWiki(value));
                prk.setLang(getLangFromUrl(value));
                //     }
            }
            if(keyStr.equals("downloadTime")){
                prk.setDownload(true);
                prk.setDownloadHourOfDay(getTime(keyValue.toString()));
            }
            if(keyStr.equals("time")){
                prk.setVisitHourOfDay(getTime(keyValue.toString()));
                prk.setBeltTime(timeBelt);
                prk.setBelt(belt);
            }
            if(keyStr.equals("os")){
                if(!keyValue.toString().contains("Windows")){
                    prk.setOs("other OS");
                }else {
                    prk.setOs(keyValue.toString());
                }
            }

            if(keyStr.equals("browser")){
                    prk.setBrowser(keyValue.toString());
            }
            if(keyStr.equals("screenSize")){
                prk.setSize(keyValue.toString());
            }
            if(keyStr.equals("cartError")){
                prk.setErrorsInCart(keyValue.toString());
            }
            if(jsonObject.has("purchase")){
                prk.setPurchase(1);
            }else{
                prk.setPurchase(0);
            }

            if(keyStr.equals("get")){
                if(keyValue.toString().contains("kw=")){
                    prk.setKwFromGetOfUrl(getKwFromHttp(keyValue.toString()));
                }
            }

            if(keyStr.equals("purchase")){
                //      parsingOfPurchase((JSONObject) keyValue, prk);
            }
           /* if(keyStr.equals("cartError")){
                if(jsonObject.has("purchase")){
                    System.out.println(keyValue + "; prechase");
                }else{
                    System.out.println(keyValue);
                }
            }*/
        }
    }

    public static String getTime(String downloadTime){
        //  "Fri Jul 06 2018 15:39:48 GMT+1200"
        timeBelt = null;
        belt = null;
        String timeEndPart = downloadTime.substring(downloadTime.indexOf(":")-2);
        String onlyTime = timeEndPart.substring(0,timeEndPart.indexOf(" "));
        String prepareBelt = timeEndPart.substring(timeEndPart.indexOf(" "));
        prepareBelt = prepareBelt.trim();
        String[] splitBelt = null;
        if(prepareBelt.contains("-")){
            splitBelt = prepareBelt.split("-");
            if(splitBelt.length >= 2){
                splitBelt[1] = "-" + splitBelt[1];
            }
        }else if(prepareBelt.contains("+")){
            splitBelt = prepareBelt.split("\\+");
            if(splitBelt.length >= 2){
                splitBelt[1] = "+" + splitBelt[1];
            }
        }else{
            splitBelt = prepareBelt.split(" ");
            if(splitBelt.length >= 2){
                splitBelt[1] = "+" + splitBelt[1];
            }
        }
        if(splitBelt != null && splitBelt.length >= 2){
            timeBelt = splitBelt[1];
            belt = splitBelt[0];
        }

        String[] timeParse = onlyTime.split(":");
        int hourToSecond = Integer.parseInt(timeParse[0]) * 3600;
        int minToSecond = Integer.parseInt(timeParse[1]) * 60;
        int second = Integer.parseInt(timeParse[2]);
        String time = String.valueOf ((double) (hourToSecond + minToSecond + second));

        return time;
    }

    public static String getParamFromWiki(String url){
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

    private static String getKwFromHttp(String s) {
        String kw = s;
        kw = kw.substring(kw.indexOf("kw=")+3, kw.length());
        if(kw.contains("&")) {
            kw = kw.substring(0, kw.indexOf("&"));
        }
        return kw;
    }
}

