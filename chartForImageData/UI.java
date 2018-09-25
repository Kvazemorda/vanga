import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UI {

    public static void main(String[] args) throws IOException, InterruptedException {
        // load toTrainMachine model
        InputStream modelIS = new FileInputStream(new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\train_model_for_du.zip"));
        //Get our network and training data
        MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(modelIS);

        RecordReader rr = new CSVRecordReader();
        String testData = "C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\java\\com\\tweakbit\\driverupdater\\trainmodel\\test1.csv";
        rr.initialize(new FileSplit(new File(testData)));
        DataSetIterator trainData = new RecordReaderDataSetIterator(rr, 47524, 0, 2);
        DataSet allData = trainData.next();
        allData.shuffle();

        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.5);

        DataSet trainingData = testAndTrain.getTrain();
        DataSet testDatSet = testAndTrain.getTest();

        //We need to normalize our data. We'll use NormalizeStandardize (which gives us mean 0, unit variance):
        NormalizerStandardize normalizer = new NormalizerStandardize();
        normalizer.fit(trainingData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
        normalizer.transform(trainingData);     //Apply normalization to the training data
        normalizer.transform(testDatSet);         //Apply normalization to the com.tweakbit.driverupdater.trainmodel data. This is using statistics calculated from the *training* set

        // Output the feature means and standard deviations so we can compare them after restoring the normalizer
        System.out.println("Means original: " + normalizer.getMean());
        System.out.println("Stds original:  " + normalizer.getStd());

        //------------------------------------------------------------------------------------
        //Training is complete. Code that follows is for plotting the data & predictions only

        //Plot the data:
        double xMin = 0;
        double xMax = 10;
        double yMin = 0;
        double yMax = 10;

        //Let's evaluate the predictions at every point in the x/y input space
        int nPointsPerAxis = 10;
        double[][] evalPoints = new double[47524][12];
        int count = 0;
        for( int i=0; i<nPointsPerAxis; i++ ){
            for( int j=0; j<nPointsPerAxis; j++ ){
                double x = i * (xMax-xMin)/(nPointsPerAxis-1) + xMin;
                double y = j * (yMax-yMin)/(nPointsPerAxis-1) + yMin;

                evalPoints[count][0] = x;
                evalPoints[count][1] = y;

                count++;
            }
        }

        INDArray allXYPoints = Nd4j.create(evalPoints);
        INDArray predictionsAtXYPoints = net.output(allXYPoints);

        //Get all of the training data in a single array, and plot it:
        PlotUtil.plotTrainingData(trainingData.getFeatures(), trainingData.getLabels(), allXYPoints, predictionsAtXYPoints, nPointsPerAxis);


        //Get test data, run the test data through the network to generate predictions, and plot those predictions:
        INDArray testPredicted = net.output(testDatSet.getFeatures());
        PlotUtil.plotTestData(testDatSet.getFeatures(), testDatSet.getLabels(), testPredicted, allXYPoints, predictionsAtXYPoints, nPointsPerAxis);

        System.out.println("****************Example finished********************");
    }
}
