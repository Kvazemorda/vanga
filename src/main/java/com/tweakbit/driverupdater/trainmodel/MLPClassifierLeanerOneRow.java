package com.tweakbit.driverupdater.trainmodel;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MLPClassifierLeanerOneRow {
    static String testData = "C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\toTrainMachine\\java\\com\\tweakbit\\driverupdater\\trainmodel\\test1.csv";
    static int rows = 0;
    static int inputs = 0;


    public void main(String[] args) throws IOException, InterruptedException {
        int rows = 168100;
        String paramsForPredict = "0,3,2,12,4,3,32,40973,2";
        //paramsForPredict = "0," + paramsForPredict;
        int inputs = getRowsFromDataSet(paramsForPredict);
        main(rows, inputs, 2, paramsForPredict);
    }

    private int getRowsFromDataSet(String toFile) {
        String[] split = toFile.split(",");
        return split.length;
    }

    public void main(int batchSize, int numInputs, int numOutputs, String dataForTrain)
            throws IOException, InterruptedException {
        try {
                RecordReader rr = new CSVRecordReader();
                rr.initialize(new FileSplit(new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\toTrainMachine\\java\\com\\tweakbit\\driverupdater\\trainmodel\\test1.csv")));
                DataSetIterator trainIter = new RecordReaderDataSetIterator(rr, batchSize, 0, numOutputs);
                DataSet allData = trainIter.next();

                // Now we want to save the normalizer to a binary file. For doing this, one can use the NormalizerSerializer.
                NormalizerSerializer serializer = NormalizerSerializer.getDefault();
                File file = null;
                NormalizerStandardize restoredNormalizer = null;
                try {
                    file = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\toTrainMachine\\resources\\du-scale");
                    restoredNormalizer = serializer.restore(file);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // Output the feature means and standard deviations so we can verify it was restored correctly
             //   System.out.println("Means restored: " + restoredNormalizer.getMean());
           //     System.out.println("Stds restored:  " + restoredNormalizer.getStd());

                // load toTrainMachine model
                MultiLayerNetwork model = null;
                restoredNormalizer.transform(allData);
                try {
                    model = ModelSerializer.restoreMultiLayerNetwork(new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\toTrainMachine\\resources\\train_model_for_du.zip"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                model.init();

            System.out.println("Evaluete model...");
            Evaluation evaluation = new Evaluation(numOutputs);

            INDArray featurs = allData.getFeatureMatrix();
            INDArray lables = allData.getLabels();
            INDArray predicted = model.output(featurs, false);
            evaluation.eval(lables, predicted);

            System.out.println(evaluation.stats());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}