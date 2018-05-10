package com.itcm;

import pcell.PCellFactory;
import pcell.algorithm.Base;
import pcell.algorithm.operators.Differential;
import pcell.algorithm.operators.RandomEliteTopologyMutation;
import pcell.controller.StaticController;
import pcell.evaluator.Error;
import pcell.evaluator.Gym;
import pcell.types.ProcessingCell;
import pcell.types.neuro.BasicNeuroPCell;
import utils.Data;
import utils.G;
import utils.ProblemReader;
import utils.loggers.CsvManager;
import utils.loggers.NoManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GymRunner {

    public static void main(String[] args) throws IOException {
        ProcessingCell pCell = BasicNeuroPCell.buildEmpty();
        pCell.setLogManager(new NoManager(pCell));
        pCell.setEvaluator(new Gym("CartPole-v0"));
        pCell.setAlgorithm(new Base<>(pCell));
        pCell.setAlgorithm(new Differential<>(
                pCell.algorithm,
                G.getDoubleParam("crossover_rate"),
                G.getDoubleParam("weighting_factor")
        ));
//        pCell.setAlgorithm(new Genetic(
//                pCell.algorithm,
//                0.8
//        ));
        pCell.setAlgorithm(new RandomEliteTopologyMutation(
                pCell.algorithm
        ));
        pCell.setController(new StaticController(pCell));


        Path logPath = Paths.get("results", "tmp", "dif.csv");

        pCell.setLogManager(new CsvManager(pCell, logPath));
//        int epochs= 20000;
        int epochs= 500000;
        String out = "" + G.getDoubleParam("batch_percent")+" "+epochs;
        out+= " "+G.getStringParam("error");
        out+= " "+G.getDoubleParam("cooling_rate");
        out+= " "+G.getDoubleParam("temperature");

//        pCell.setLogManager();
//        CellularProcessing cpa = buildBasicDifferential.buildBasicCPA();
//        CellularProcessing cpa = CellularProcessing.buildCPAFromDefaults();
        pCell.setParam("max_epochs", epochs);
        out+= " "+G.getIntegerParam("population_size");
        pCell.fit();
//        Data yHat = pCell.predict(xTrain);
//        yHat.toCsv("results\\tmp\\trainYHat.csv");
//        out += " "+ Error.computeError(yTrain, yHat,"mse");
//        yHat = pCell.predict(xTest);
//        yHat.toCsv("results\\tmp\\testYHat.csv");
//        out += " " + Error.computeError(yTest, yHat,"mse");
//        out+=" " + G.getIntegerParam("seed");
//        out+= " " + dataset;
        System.out.println(out);
//        double mae = Error.computeError(yTest, yHat);
//        System.out.println(mae);
//        System.out.println(pCell.getBestModel().getFitness());
    }

}
