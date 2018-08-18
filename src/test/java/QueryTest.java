import com.Main;
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
        URIBuilder builder = new URIBuilder("http://localhost:8080/vanga-predict/get_predict_for_du");
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
        Main main = new Main();
        args = new String[1];
        args[0] = "2018-08-17";
        main.main(args);
        printPredicion();
        ArrayList<ProductTweakBit> listOfDU = main.listOfPrk;
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
}

