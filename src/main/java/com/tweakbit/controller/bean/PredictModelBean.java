package com.tweakbit.controller.bean;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.ComposableRecordReader;
import org.datavec.api.records.reader.impl.LineRecordReader;
import org.datavec.api.records.reader.impl.collection.CollectionRecordReader;
import org.datavec.api.records.reader.impl.collection.ListStringRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputStreamInputSplit;
import org.datavec.api.split.StringSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import java.io.*;
@Stateless
public class PredictModelBean {

    public String predict(String paramsForPredict, ServletContext context) {
        int rows = 1;
        int inputs = getRowsFromDataSet(paramsForPredict);
        return main(rows, inputs, 2, paramsForPredict, context);

    }

    private static int getRowsFromDataSet(String toFile) {
            String[] split = toFile.split(",");
        return split.length;
    }

    public String main(int batchSize, int numInputs, int numOutputs, String dataForTrain, ServletContext context){
        try {
            System.out.println("----------------------------- " + dataForTrain);
            RecordReader rr = new CSVRecordReader();
            rr.initialize(new StringSplit(dataForTrain));
            DataSetIterator trainIter = new RecordReaderDataSetIterator(rr, batchSize, 0, numOutputs);
            DataSet allData = trainIter.next();

            // Now we want to save the normalizer to a binary file. For doing this, one can use the NormalizerSerializer.
            NormalizerSerializer serializer = NormalizerSerializer.getDefault();
            NormalizerStandardize restoredNormalizer = serializer.restore(context.getRealPath("WEB-INF\\classes\\du-scale"));
            restoredNormalizer.transform(allData);
            // load train model
            MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(context.getRealPath("WEB-INF\\classes\\train_model_for_du.zip"));
            model.init();
            return model.output(allData.getFeatures()).toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error";
        }
    }
}