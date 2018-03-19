package com.itcm;

import pcell.PCellFactory;
import pcell.evaluator.Error;
import pcell.types.ProcessingCell;
import utils.Data;
import utils.G;
import utils.ProblemReader;
import utils.loggers.CsvManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneticRunner {

    public static void main(String[] args) throws IOException {
        Path dir;
        String dataset = "min_max_(0,1)";
        if(args.length >0){
            dir = Paths.get(args[0]);
        }else{
//            dir = Paths.get(System.getProperty("user.home"))
//                    .resolve(Paths.get("Dropbox","Doctorado", "Datasets", "20171001-ILKatritzky2002b","3-Uploaded Data", "20171010"));
//            dir = Paths.get("results","test_files","20180313-il","tree",dataset).toAbsolutePath();
            dir = Paths.get("results","test_files","boston").toAbsolutePath();
        }
//        C:\Users\joalc\Dropbox\Doctorado\Programas\cellular-processing-neuroevolution\results\test_files\20180313-il\tree\min_max_(-1,1)
        if(!dir.toFile().isDirectory()){
            System.err.println("Must be a folder with testX.csv, testY.csv, trainX.csv, trainY.csv");
        }
//        dir=folder.toString();
        Data xTest = ProblemReader.read_csv(dir.resolve("testX.csv"),true);
        Data yTest = ProblemReader.read_csv(dir.resolve("testY.csv"),true);
        Data xTrain = ProblemReader.read_csv(dir.resolve("trainX.csv"),true);
        Data yTrain = ProblemReader.read_csv(dir.resolve("trainY.csv"),true);

        ProcessingCell pCell = PCellFactory.buildSimulatingAnneling();
        Path logPath = Paths.get("results", "tmp", "dif.csv");

        pCell.setLogManager(new CsvManager(pCell, logPath));
        pCell.control.setTestData(xTest,yTest);
        int epochs= 10000;
        String out = "" + G.paramD("batch_percent")+" "+epochs;
//        pCell.setLogManager();
//        CellularProcessing cpa = buildBasicDifferential.buildBasicCPA();
//        CellularProcessing cpa = CellularProcessing.buildCPAFromDefaults();
        pCell.setParam("max_epochs", epochs);
        out+= " "+G.getIntegerParam("population_size");
        pCell.fit(xTrain, yTrain);
        Data yHat = pCell.predict(xTrain);
        yHat.toCsv("results\\tmp\\trainYHat.csv");
        out += " "+ Error.computeError(yTrain, yHat,"mse");
        yHat = pCell.predict(xTest);
        yHat.toCsv("results\\tmp\\testYHat.csv");
        out += " " + Error.computeError(yTest, yHat,"mse");
        out+=" " + G.getIntegerParam("seed");
        out+= " " + dataset;
        System.out.println(out);
//        double mae = Error.computeError(yTest, yHat);
//        System.out.println(mae);
//        System.out.println(pCell.getBestModel().getFitness());
    }

}
