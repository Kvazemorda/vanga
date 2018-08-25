package com;

import com.tweakbit.controller.DataParser;
import com.tweakbit.driverupdater.model.enties.ProductTweakBit;
import com.tweakbit.driverupdater.trainmodel.MLPClassifierLeaner;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class Main {
    static StringBuilder toFile = new StringBuilder();
    static String testData;
    public static ArrayList<ProductTweakBit> listOfPrk;
    static int countRows;
    static int numImputs;
    static CalcOfMoon calcOfMoon;
    static Date date;
    static DataParser dataParser;

    public static void main(String[] args) {
        dataParser = new DataParser(toFile);
        calcOfMoon = new CalcOfMoon();
        countRows = 0;
        listOfPrk = new ArrayList<>();
        File file = null;
        String line = null;
        testData = "C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\java\\com\\tweakbit\\driverupdater\\trainmodel\\test1.csv";
        try {
            ArrayList<String> days = new ArrayList<>();
            days.add("2018-07-30.txt");
            days.add("2018-07-31.txt");
            days.add("2018-08-01.txt");
            days.add("2018-08-02.txt");
            days.add("2018-08-04.txt");
            days.add("2018-08-05.txt");
            days.add("2018-08-06.txt");
            days.add("2018-08-07.txt");
            days.add("2018-08-08.txt");
            days.add("2018-08-09.txt");
            days.add("2018-08-10.txt");
            days.add("2018-08-11.txt");
            days.add("2018-08-13.txt");
            days.add("2018-08-14.txt");
            days.add("2018-08-15.txt");
            days.add("2018-08-16.txt");
            days.add("2018-08-17.txt");
            days.add("2018-08-18.txt");
            days.add("2018-08-19.txt");
            days.add("2018-08-20.txt");
            days.add("2018-08-21.txt");
            days.add("2018-08-22.txt");
            days.add("2018-08-23.txt");

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
                        //перебираем сессии юзера
                        checkEachSession(sessionObj, splitLine[0], date, listOfPrk);
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
            // Если подготавилваем параметры к тестирования, то в файл будет сохраняться параметр запроса
            if(!dataParser.isSaveDataForQuery()){
                PrintWriter pwTestData = new PrintWriter(new File(testData));
                for(ProductTweakBit productTweakBit: listOfPrk){
                    dataParser.saveToFile(productTweakBit);
                }
                pwTestData.write(toFile.toString());
                pwTestData.close();

                numImputs = dataParser.getRowsFromDataSet(toFile) - 1;
                dataParser.getSaveToString().safeMapsOfKeysToFile();   // сохраняю список параметров

                // Prepare a temporary file to save to and load from
                File scaleCof = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\du-scale");

                // save toTrainMachine model
                File locationToSave = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\train_model_for_du.zip");


                MLPClassifierLeaner mlp = new MLPClassifierLeaner(locationToSave, scaleCof);
                mlp.toTrainMachine(listOfPrk.size(), numImputs, 2, testData);
            }else {
                dataParser.saveToFile(dataParser.getToFile());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkEachSession(JSONObject jsonObj, String auid, Date date,
                                        ArrayList<ProductTweakBit> listOfPrk) {
        boolean wasDownload = jsonObj.toString().contains("downloadTime");
        int sessionCount = 1;
        for (Object key : jsonObj.keySet()) {
            //based on you key types
            String keyStr = (String) key;
            Object keyValue = jsonObj.get(keyStr);
            if (keyValue instanceof JSONObject) {
                ProductTweakBit prk = new ProductTweakBit(auid + "." + keyStr);
                // сохраняю кол-во сессий
                prk.setSessionCount(String.valueOf(sessionCount));
                // если был скачан в другой сессии но с этим же auid, то считаем, что скачает
                prk.setDownload(wasDownload);
                // сохраняю дату визита из имени файла с данными
                prk.setDateOfVizit(date);

                dataParser.parsingOfSession((JSONObject) keyValue, prk, date, sessionCount);
                if(prk.getVisitHourOfDay() != null
                        && prk.getProductName() != null
                        && prk.getProductName().equals("du")) {

                    listOfPrk.add(prk);
                }
            }
            sessionCount++;
        }
    }

}
