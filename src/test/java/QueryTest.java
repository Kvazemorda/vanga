import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class QueryTest {
    static Integer truePositive = 0;
    static Integer falsePositive = 0;
    static Integer falseNegative = 0;
    static Integer trueNegative = 0;
    static Integer label0Predict0, label1Predict0, label1Predict1, label0Predict1;
    static StringBuilder string = new StringBuilder();

    public static void sendUrl() throws URISyntaxException, IOException {
        label0Predict0 = 0;
        label0Predict1 = 0;
        label1Predict0 = 0;
        label1Predict1 = 0;
        //97.107.137.201
        String path = "http://localhost:8080/vanga-predict/get_predict_for_du?";
        StringBuilder stringBuildor = new StringBuilder();
        File file = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\test1.csv");
        ArrayList<String> lines = getParamsFromFile(file);
        System.out.println(lines.size());
        int count = 0;
        for(String line: lines){
            count++;
                line = line.replace(" ", "+");
                String[] split = line.split(",");
                line = split[1];

                URIBuilder builder = new URIBuilder(path + line);
                HttpGet request = new HttpGet(builder.build());
                request.addHeader("Content-Type", "multipart/related;");
                HttpClient client = HttpClientBuilder.create().build();
                client.execute(request);

                HttpResponse response = client.execute(request);
                String answer = new BasicResponseHandler().handleResponse(response);
                splitAnswerToDouble(answer, Integer.valueOf(split[0]), line);
                stringBuildor.append(split[0]).append(" ").append(answer).append("\n");

        }
        printPrediction(stringBuildor.toString());
        double accuracy = (double)(truePositive + trueNegative)
                /(double) (truePositive + falsePositive + falseNegative + trueNegative);
        double precision = (double) truePositive/(double)(truePositive + falsePositive);
        double recall = (double) truePositive/(double)(truePositive + falseNegative);
        double f1score = (double) 2*(recall * precision)/(double) (recall + precision);
        System.out.println("accuracy = " + accuracy);
        System.out.println("precision = " + precision);
        System.out.println("recall = " + recall);
        System.out.println("f1score = " + f1score);
        System.out.println("label 0, predict 0 " + trueNegative);
        System.out.println("label 0, predict 1 " + falsePositive);
        System.out.println("label 1, predict 0 " + falseNegative);
        System.out.println("label 1, predict 1 " + truePositive);
    }

    private static void splitAnswerToDouble(String answer, Integer actual, String params) {
        Double one = Double.valueOf(answer);
        boolean predictYes = false;
        boolean predictNo = false;
        if(one >= 0.50 )
            predictYes = true;
        else {
            predictNo = true;
        }

        if (predictNo && actual == 0){
            trueNegative++;
        }
        if(predictYes && actual == 0){
            falsePositive++;
            string.append(actual).append(" ").append(answer).append(" ").append(params).append("\n");
        }
        if(predictYes && actual == 1){
            truePositive++;
        }
        if(predictNo && actual == 1){
            falseNegative++;
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        //Main main = new Main();
        //args = new String[1];
        //args[0] = "2018-08-27.txt";
        //main.main(args);
        sendUrl();
    }

    public static void printPrediction(String answer) throws IOException, URISyntaxException {
        PrintWriter out = new PrintWriter("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\test\\java\\resp.txt");
        out.print(answer);
        out.close();

        PrintWriter fileFolsPositive = new PrintWriter("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\falsePositive");
        fileFolsPositive.print(string);
        fileFolsPositive.close();
    }


    public static ArrayList<String> getParamsFromFile(File file) throws IOException {
        String line = null;
        ArrayList<String> lines = new ArrayList<>();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        int countRow = 0;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
}

