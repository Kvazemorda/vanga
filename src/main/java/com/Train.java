package com;

import com.tweakbit.driverupdater.trainmodel.MLPClassifierLeaner;

import java.io.File;
import java.io.IOException;

public class Train {
    public static void main(String[] args) {
        String testData = "C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\java\\com\\tweakbit\\driverupdater\\trainmodel\\test1.csv";
        // Prepare a temporary file to save to and load from
        File scaleCof = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\du-scale");

        // save toTrainMachine model
        File locationToSave = new File("C:\\Users\\ilyav\\IdeaProjects\\vanga\\src\\main\\resources\\train_model_for_du.zip");

        MLPClassifierLeaner mlp = new MLPClassifierLeaner(locationToSave, scaleCof);
        try {
            mlp.toTrainMachine(618580, 25, 2, testData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
