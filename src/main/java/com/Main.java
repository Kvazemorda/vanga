package com;

import com.tweakbit.controller.DataParser;
import com.tweakbit.driverupdater.model.enties.VisitToTweakBit;
import com.tweakbit.driverupdater.trainmodel.MLPClassifierLeaner;
import com.tweakbit.model.AUID;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class Main {
    static StringBuilder toFile = new StringBuilder();
    static String testData;
    public static ArrayList<VisitToTweakBit> listOfPrk;
    static int countRows, downloaded, goAway;
    static int numImputs;
    static CalcOfMoon calcOfMoon;
    static Date date;
    static DataParser dataParser;
    public static TreeMap<String, AUID> auids;

    public static void main(String[] args) throws InterruptedException, IOException {
        downloaded = 0;
        goAway = 0;
        auids = new TreeMap<>();
        dataParser = new DataParser(toFile);
        calcOfMoon = new CalcOfMoon();
        countRows = 0;
        listOfPrk = new ArrayList<>();
        int countRowForLearning = 0;
        File file = null;
        String line = null;
        testData = "C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\java\\com\\tweakbit\\driverupdater\\trainmodel\\test1.csv";
        if(args.length == 0 || !args[0].equals("learn")){
            try {
                ArrayList<String> days = new ArrayList<>();
                days.add("2018-09-17.txt");
                days.add("2018-09-19.txt");
                days.add("2018-09-20.txt");
                days.add("2018-09-21.txt");
                days.add("2018-09-22.txt");
                days.add("2018-09-23.txt");
                days.add("2018-09-26.txt");
                days.add("2018-09-27.txt");
                days.add("2018-09-28.txt");


                for(int i = 0; i < days.size(); i++) {
                    if(args.length > 0 ){
                        file = new File("C:\\Users\\ilyav\\Downloads\\files\\"+ args[0]);
                        dataParser.setSafeDataForQuery(true);
                        date = dataParser.getDateFromFileName(args[0]);
                    }else {
                        file = new File("C:\\Users\\ilyav\\Downloads\\files\\"+ days.get(i));
                        date = dataParser.getDateFromFileName(days.get(i));
                    }

                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    System.out.println(days.get(i));
                    int countRow = 0;
                    while ((line = bufferedReader.readLine()) != null) {
                        countRow++;
                        line = line.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                        line = line.replaceAll("\\+", "%2B");
                        line = URLDecoder.decode(line, "UTF-8");
                        String[] splitLine = line.split("\t");
                        //сохраняю ауид без сессии
                        try{
                            JSONObject obj = new JSONObject(splitLine[1]);
                            // находим все сессии одного AUID
                            String session = obj.getJSONObject("session").toString();
                            JSONObject sessionObj = new JSONObject(session);
                            //перебираем сессии каждого AUID
                            checkEachSession(sessionObj, splitLine[0]);
                        }catch (JSONException e){
                            e.printStackTrace();
                            System.out.println("строка файла " + countRow);
                        }
                    }
                    fileReader.close();
                    if(args.length > 0){
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // Если подготавливаем параметры к обучению, то в файл будет сохраняться значения 1,3,1,4,2.. итд
                // Если подготавилваем параметры к тестирования, то в файл будет сохраняться параметры http запроса
                PrintWriter pwTestData = new PrintWriter(new File(testData));
                //PrintWriter pwForRequest = new PrintWriter(new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\test1.csv"));
                StringBuilder fileForPurchase = new StringBuilder();
                for(Map.Entry<String,AUID> map: auids.entrySet()){
                    AUID auid = map.getValue();
                    int session = 1;
                    VisitToTweakBit previsousVisit = new VisitToTweakBit();
                    if(auid.isWasDownload()){
                        downloaded++;
                    }
                    if(!auid.isWasDownload()){
                        goAway++;
                    }
                    for(VisitToTweakBit app: auid.getSetOfVisits()){
                        app.setSessionCount(String.valueOf(session));
                        app.setDownload(auid.isWasDownload());
                        app.setRevenue(auid.getRevenue());
                        if(!dataParser.isSaveDataForQuery()){
                            dataParser.saveToFile(app, "\n", true, dataParser.isSaveDataForQuery());
                            //   dataParser.saveToFile(previsousVisit, "\n", false, dataParser.isSaveDataForQuery());
                            //   dataParser.savePurchaseToFile(app, "\n", true, dataParser.isSaveDataForQuery(), fileForPurchase);
                        }else {
                            dataParser.saveToFile(app, "\n", true, dataParser.isSaveDataForQuery());
                            //  dataParser.saveToFile(previsousVisit, "\n", false, dataParser.isSaveDataForQuery());
                        }
                        previsousVisit = app;
                        countRowForLearning++;
                        session++;
                    }
                }

                if(!dataParser.isSaveDataForQuery()) {
                    System.out.println("AUIDS = " + auids.size());
                    System.out.println("downloaded = " + downloaded + " goAway = " + goAway);
                    System.out.println("кол-во скачиваний " +  dataParser.countDownloaded);

                    pwTestData.write(toFile.toString());
                    pwTestData.close();

                 //   numImputs = dataParser.getRowsFromDataSet(toFile) - 1;
                    dataParser.getSaveToString().safeMapsOfKeysToFile();   // сохраняю список параметров

                    // Prepare a temporary file to save to and load from
                //    File scaleCof = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\du-scale");

                    // save toTrainMachine model
                //    File locationToSave = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\train_model_for_du.zip");

                  //  MLPClassifierLeaner mlp = new MLPClassifierLeaner(locationToSave, scaleCof);
                  //  mlp.toTrainMachine(countRows(testData), numImputs, 2, testData);
                }else {
                    //pwForRequest.write(toFile.toString());
                    //    pwForRequest.close();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            // Prepare a temporary file to save to and load from
            File scaleCof = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\du-scale");

            // save toTrainMachine model
            File locationToSave = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\train_model_for_du.zip");

            MLPClassifierLeaner mlp = new MLPClassifierLeaner(locationToSave, scaleCof);
            numImputs = countColumn(testData);
            mlp.toTrainMachine(countRows(testData), numImputs - 1, 2, testData);
        }
    }

    public static int countColumn(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            break;
        }
        return line.split(",").length;
    }

    public static int countRows(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    private static AUID getAUIDFromMap(String s) {

        if(auids.containsKey(s)){
            return auids.get(s);
        }else {
            auids.put(s, new AUID(s, 0.0));
            return getAUIDFromMap(s);
        }
    }

    public static void checkEachSession(JSONObject jsonObj, String auidText) {
        int sessionCount = 1;
        for (Object key : jsonObj.keySet()) {
            //based on you key types
            String keyStr = (String) key;
            Object keyValue = jsonObj.get(keyStr);
            if (keyValue instanceof JSONObject) {
                VisitToTweakBit visit = new VisitToTweakBit();
                // сохраняю кол-во сессий
                visit.setSessionCount(String.valueOf(sessionCount));
                visit.setSessionTime(Integer.valueOf(keyStr));
                dataParser.parsingOfSession((JSONObject) keyValue, visit, sessionCount);
                if(visit.getVisitHourOfDay() != null
                        && visit.getProductName() != null) {

                    //находим существущий auid или создаем новый
                    AUID auid = getAUIDFromMap(auidText);
                    if(visit.isDownload()){
                        auid.setWasDownload(visit.isDownload());
                    }
                    if(visit.getRevenue() > 0){
                        auid.setRevenue(visit.getRevenue());
                    }

                    visit.setAuid(auidText);
                    auid.getSetOfVisits().add(visit);
                }
            }
            sessionCount++;
        }
    }

}
