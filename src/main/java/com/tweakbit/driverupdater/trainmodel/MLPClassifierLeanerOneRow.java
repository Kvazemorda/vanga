package com.tweakbit.driverupdater.trainmodel;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.StringSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;

import java.io.*;

public class MLPClassifierLeanerOneRow {
    static String testData = "C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\java\\com\\tweakbit\\driverupdater\\trainmodel\\test2.csv";
    static int rows = 0;
    static int inputs = 0;


    public static void main(String[] args) throws IOException, InterruptedException {
        int rows = 1;
        String paramsForPredict = "3,2,12,4,3,32,40973,2";
        paramsForPredict = "0," + paramsForPredict;
        int inputs = getRowsFromDataSet(paramsForPredict);
        main(rows, inputs, 2, paramsForPredict);
    }

    private static int getRowsFromDataSet(String toFile) {
        String[] split = toFile.split(",");
        return split.length;
    }

    public static void main(int batchSize, int numInputs, int numOutputs, String dataForTrain)
            throws IOException, InterruptedException {
        try {
                RecordReader rr = new CSVRecordReader();
                rr.initialize(new StringSplit(dataForTrain));
                DataSetIterator trainIter = new RecordReaderDataSetIterator(rr, batchSize, 0, numOutputs);
                DataSet allData = trainIter.next();

                // Now we want to save the normalizer to a binary file. For doing this, one can use the NormalizerSerializer.
                NormalizerSerializer serializer = NormalizerSerializer.getDefault();
                File file = null;
                NormalizerStandardize restoredNormalizer = null;
                try {
                    file = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\du-scale5918166355171863020normalizers");
                    restoredNormalizer = serializer.restore(file);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // Output the feature means and standard deviations so we can verify it was restored correctly
             //   System.out.println("Means restored: " + restoredNormalizer.getMean());
           //     System.out.println("Stds restored:  " + restoredNormalizer.getStd());

                // load train model
                MultiLayerNetwork model = null;
                restoredNormalizer.transform(allData);
                try {
                    model = ModelSerializer.restoreMultiLayerNetwork(new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\train_model_for_du.zip"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                model.init();
           //     System.out.println("Evaluete model...");
                //  System.out.println(allData.getFeatures());
                System.out.println(model.output(allData.getFeatures()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }