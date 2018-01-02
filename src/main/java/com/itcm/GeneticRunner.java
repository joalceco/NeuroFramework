package com.itcm;

import pcell.PCellFactory;
import pcell.evaluator.MAError;
import pcell.types.ProcessingCell;
import utils.Data;
import utils.ProblemReader;
import utils.loggers.CsvManager;

import java.io.IOException;

public class GeneticRunner {

    public static void main(String[] args) throws IOException {
        String dir = "/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/";
        Data xTrain = ProblemReader.getX(dir + "testX.csv");
        Data yTrain = ProblemReader.getY(dir + "testY.csv");
        Data xTest = ProblemReader.getX(dir + "trainX.csv");
        Data yTest = ProblemReader.getY(dir + "trainY.csv");

        ProcessingCell pCell = PCellFactory.buildBasicDifferential();
        pCell.setLogManager(new CsvManager(pCell, "/home/joalceco/Documents/tmp/dif.csv"));
//        pCell.setLogManager();
//        CellularProcessing cpa = buildBasicDifferential.buildBasicCPA();
//        CellularProcessing cpa = CellularProcessing.buildCPAFromDefaults();
        pCell.setParam("max_epochs", 1000);
        pCell.fit(xTrain, yTrain);
        Data yHat = pCell.test(xTest);
        double mae = MAError.computeError(yTest, yHat);

        System.out.println(mae);
    }

}
