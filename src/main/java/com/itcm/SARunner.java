package com.itcm;

import pcell.PUnitFactory;
import pcell.evaluator.Error;
import pcell.types.ProcessingUnit;
import utils.Data;
import utils.G;
import utils.ProblemReader;
import utils.loggers.CsvManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SARunner {

    int runID=0;

    public SARunner(int runID){
        this.runID=runID;
    }

    public static void main(String[] args) throws IOException {
        Path dir;
        String cluster = "2";
        String alg = "genetic";
        String experiment = "results_30_sumar456null";


        String dataset = "min_max_(0,1)";
        if(args.length >0){
            dir = Paths.get(args[0]);
        }else{
//            dir = Paths.get(System.getProperty("user.home"))
//                    .resolve(Paths.get("Dropbox","Doctorado", "Datasets", "20171001-ILKatritzky2002b","3-Uploaded Data", "20171010"));
            dir = Paths.get("results","test_files","20180313-il","tree",dataset).toAbsolutePath();
//            dir = Paths.get("results","test_files","boston").toAbsolutePath();
            dir = Paths.get(System.getProperty("user.home"))
                    .resolve(Paths.get("Dropbox","Doctorado", "paper2018",experiment, "dataset", cluster));
        }
//        dir = Paths.get("C:\Users\joalc\Dropbox\Doctorado\paper2018\datasets\40_all");
//        C:\Users\joalc\Dropbox\Doctorado\Programas\cellular-processing-neuroevolution\results\test_files\20180313-il\tree\min_max_(-1,1)
        if(!dir.toFile().isDirectory()){
            System.err.println("Must be a folder with testX.csv, testY.csv, trainX.csv, trainY.csv");
        }
//        dir=folder.toString();
        Data xTest = ProblemReader.read_csv(dir.resolve("testX.csv"),true);
        Data yTest = ProblemReader.read_csv(dir.resolve("testY.csv"),true);
        Data xTrain = ProblemReader.read_csv(dir.resolve("trainX.csv"),true);
        Data yTrain = ProblemReader.read_csv(dir.resolve("trainY.csv"),true);

//        ProcessingCell pCell = PCellFactory.buildBasicDifferential();
//        ProcessingCell pCell = PCellFactory.buildSimulatingAnneling();
//        ProcessingCell pCell = SVRPCell.buildBasicSVR();
        ProcessingUnit pCell = PUnitFactory.buildBasicGenetic();
//        ProcessingCell pCell = PCellFactory.buildBestMutator();
//        Paths.get("Dropbox","Doctorado", "paper2018", "results_30","diff", "1","dif_"+G.runid+".csv");
//        Path logPath = Paths.get("results", "tmp", "dif_"+G.runid+".csv");
        Path logPath = Paths.get(System.getProperty("user.home")).resolve(Paths.get("Dropbox","Doctorado", "paper2018", experiment,alg, cluster,"dif_"+G.runid+".csv"));

        pCell.setLogManager(new CsvManager(pCell, logPath));
        pCell.control.setTestData(xTrain,yTrain,xTest,yTest);
//        int epochs= 20000;
//        int epochs= 26342;
        int epochs = 700*1000;



//        int epochs= 200;



        String out = "" + G.getDoubleParam("batch_percent")+" "+epochs;
        out+= " "+G.getStringParam("error");
        out+= " "+G.getDoubleParam("cooling_rate");
        out+= " "+G.getDoubleParam("temperature");


//        pCell.setLogManager();
//        CellularProcessing cpa = buildBasicDifferential.buildBasicCPA();
//        CellularProcessing cpa = CellularProcessing.buildCPAFromDefaults();
        Path dirExp = Paths.get(System.getProperty("user.home"))
                .resolve(Paths.get("Dropbox","Doctorado", "paper2018", experiment,alg, cluster));
        pCell.setParam("max_epochs", epochs);
        out+= " "+G.getIntegerParam("population_size");
        pCell.fit(xTrain, yTrain);
        Data yHat = pCell.predict(xTrain);

        yHat.toCsv(dirExp.resolve("trainYHat_"+G.runid+".csv").toFile().toString());
//        yHat.toCsv(dir.resolve("trainyHat.csv").toFile().toString());
        out += " "+ Error.computeError(yTrain, yHat,"mse");
        yHat = pCell.predict(xTest);
        Path dirmodel = Paths.get(System.getProperty("user.home"))
                .resolve(Paths.get("Dropbox","Doctorado", "paper2018", experiment,"models",cluster,alg+G.runid+".ann"));
        pCell.saveAs(dirmodel);
        yHat.toCsv(dirExp.resolve("testYHat_"+G.runid+".csv").toFile().toString());
//        yHat.toCsv(dir.resolve("testyHat.csv").toFile().toString());
        out += " " + Error.computeError(yTest, yHat,"mse");
        out+=" " + G.getIntegerParam("seed");
        out+= " " + dataset;
        System.out.println(out);
//        double mae = Error.computeError(yTest, yHat);
//        System.out.println(mae);
//        System.out.println(pCell.getBestModel().getFitness());
    }

}
