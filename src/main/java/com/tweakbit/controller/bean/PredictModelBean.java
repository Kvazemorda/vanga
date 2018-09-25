package com.tweakbit.controller.bean;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.StringSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;

@Local
@Stateless
public class PredictModelBean {

    public Double predict(String paramsForPredict, ServletContext context) {
        int rows = 1;
        int inputs = getRowsFromDataSet(paramsForPredict);
        return main(rows, inputs, 2, paramsForPredict, context);

    }

    private static int getRowsFromDataSet(String toFile) {
            String[] split = toFile.split(",");
        return split.length;
    }

    public double main(int batchSize, int numInputs, int numOutputs, String dataForTrain, ServletContext context){
        try {
            System.out.println("----------------------------- " + dataForTrain);
            RecordReader rr = new CSVRecordReader();
            rr.initialize(new StringSplit(dataForTrain));
            DataSetIterator trainIter = new RecordReaderDataSetIterator(rr, batchSize, 0, numOutputs);
            DataSet allData = trainIter.next();

            // Now we want to save the normalizer to a binary file. For doing this, one can use the NormalizerSerializer.
            NormalizerSerializer serializer = NormalizerSerializer.getDefault();
            InputStream scaleIS = context.getResourceAsStream("/" + File.separator + "WEB-INF"
                    + File.separator + "classes" + File.separator + "du-scale");
            NormalizerStandardize restoredNormalizer = serializer
                    .restore(scaleIS);
            scaleIS.close();
            restoredNormalizer.transform(allData);
            // load toTrainMachine model
            InputStream modelIS = context.getResourceAsStream("/" + File.separator + "WEB-INF"
                            + File.separator + "classes" + File.separator + "train_model_for_du.zip");
            MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(modelIS);
            modelIS.close();
            model.init();
            INDArray predict = model.output(allData.getFeatureMatrix(), false);
            System.out.println(predict);
            return  predict.getFloat(0,1);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }
}