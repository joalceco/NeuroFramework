package com.itcm;

import pcell.PUnitFactory;
import pcell.evaluator.Error;
import pcell.types.ProcessingUnit;
import utils.Data;
import utils.G;
import utils.ProblemReader;
import utils.loggers.CsvManager;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeneticRunner {

    int runID = 0;

    public GeneticRunner(int runID) {
        this.runID = runID;
    }

    public static void main(String[] args) throws IOException {
        CommandLine cmd = Main.parser(args);
        G.runid = Integer.parseInt(cmd.getOptionValue("run_id"));
        G.resetR(Integer.parseInt(cmd.getOptionValue("seed")));
        Path inputPath, outputPath;
        String algorithm = "Genetic";
        if (cmd.hasOption("input_path")) {
            inputPath = Paths.get(cmd.getOptionValue("input_path"));
        } else {
            inputPath = Paths.get(System.getProperty("user.home"))
                    .resolve(Paths.get("Dropbox", "Doctorado", "paper2019", "2-PreparedData", "msf"));
        }
        if (cmd.hasOption("output_path")) {
            outputPath = Paths.get(cmd.getOptionValue("output_path"));
        } else {
            Path tmpPath = Paths.get(System.getProperty("user.home"))
                    .resolve(Paths.get("Dropbox", "Doctorado", "paper2019", "3-Uploaded Data"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(formatter);
            outputPath = Paths.get(tmpPath.toString(), date, algorithm);
            outputPath.toFile().mkdirs();
        }
        if (!inputPath.toFile().isDirectory()) {
            System.err.println("Must be a folder with testX.csv, testY.csv, trainX.csv, trainY.csv");
        }
        System.out.println("Input: "+inputPath.toString());
        System.out.println("Output: "+outputPath.toString());
        Data xTest = ProblemReader.read_csv(inputPath.resolve("testX.csv"), true);
        Data yTest = ProblemReader.read_csv(inputPath.resolve("testY.csv"), true);
        Data xTrain = ProblemReader.read_csv(inputPath.resolve("trainX.csv"), true);
        Data yTrain = ProblemReader.read_csv(inputPath.resolve("trainY.csv"), true);
        ProcessingUnit pCell = PUnitFactory.buildBasicGenetic();
        Path logPath = Paths.get(outputPath.toString(), "genetic_" + G.runid + ".csv");
        pCell.setLogManager(new CsvManager(pCell, logPath));
        pCell.control.setTestData(xTrain, yTrain, xTest, yTest);
//        int epochs= 20000;
//        int epochs = 100000;
        Integer epochs = Integer.parseInt(cmd.getOptionValue("epoch"));
        System.out.println(epochs);
        G.setParam("max_epochs",epochs);
        String out = "" + G.getDoubleParam("batch_percent") + " " + epochs;
        out += " " + G.getStringParam("error");
        out += " " + G.getDoubleParam("cooling_rate");
        out += " " + G.getDoubleParam("temperature");
//        pCell.setLogManager();
//        CellularProcessing cpa = buildBasicDifferential.buildBasicCPA();
        //CellularProcessing cpa = CellularProcessing.buildCPAFromDefaults();
        pCell.setParam("max_epochs", epochs);
        int max_size = pCell.params.getInt("max_size");
        out += " " + G.getIntegerParam("population_size");
        pCell.fit(xTrain, yTrain);
        Data yHat = pCell.predict(xTrain);
        yHat.setIndex(xTrain.index());
        Path yOutPath = Paths.get(logPath.getParent().toString(), "trainYHat_" + G.runid + ".csv");
        yHat.toCsv(yOutPath.toString());
        out += " " + Error.computeError(yTrain, yHat, "mse");
        yHat = pCell.predict(xTest);
        yHat.setIndex(xTest.index());
        yOutPath = Paths.get(logPath.getParent().toString(), "testYHat_" + G.runid + ".csv");
        yHat.toCsv(yOutPath.toString());
        pCell.saveAs(Paths.get(outputPath.toString(), "model_" + G.runid + ".ann"));
        out += " " + Error.computeError(yTest, yHat, "mse");
//        out += " " + G.r.("seed");
//        out += " " + dataset;
        System.out.println(out);
//        double mae = Error.computeError(yTest, yHat);
//        System.out.println(mae);
//        System.out.println(pCell.getBestModel().getFitness());
    }

}
