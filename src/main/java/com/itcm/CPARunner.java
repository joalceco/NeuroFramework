package com.itcm;

import pcell.evaluator.Error;
import utils.Data;
import utils.ProblemReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CPARunner {

    public static void main(String[] args) throws IOException {
        Path dir = Paths.get("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/");
        Data xTrain = ProblemReader.getX(dir + "testX.csv");
        Data yTrain = ProblemReader.getY(dir + "testY.csv");
        Data xTest = ProblemReader.getX(dir + "trainX.csv");
        Data yTest = ProblemReader.getY(dir + "trainY.csv");
//        CellularProcessing cpa = CellularProcessing.buildBasicCPA();
        CellularProcessing cpa = CellularProcessing.buildCPAFromDefaults();
        cpa.fit(xTrain, yTrain);
        Data yHat = cpa.test(xTest);
        double mae = Error.computeError(yTest, yHat);
        System.out.println(mae);
    }

}
