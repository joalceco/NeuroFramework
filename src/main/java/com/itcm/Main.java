package com.itcm;

import pcell.PCellFactory;
import pcell.evaluator.Error;
import pcell.model.Model;
import pcell.types.ProcessingCell;
import utils.Data;
import utils.G;
import utils.ProblemReader;
import utils.Utils;
import utils.loggers.CsvManager;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

        //DAme datos de x
        Data x = ProblemReader.getX("");
        //Dame datos de y
        Data y = ProblemReader.getY("");
        //Dame un BasicNeuroPCell
//        double cr=0.6,wm=0.8;
        for (double cr = 0.6; cr < 0.61; cr += 0.05) {
            for (double wf = 0.2; wf < 0.21; wf += 0.05) {
                for (double wm = 0.8; wm < 0.81; wm += 0.05) {
                    G.evaluations = 0;
                    G.setParam("crossover_rate", cr);
                    G.setParam("weigthing_factor", wf);
                    G.setParam("weight_mutation_p", wm);
                    ProcessingCell pcell = PCellFactory.buildBasicDifferential();
                    pcell.fit(x, y);
                    Model bestSolution = pcell.getBestModel();
                    pcell.setLogManager(new CsvManager(pcell, Paths.get("/home/joalceco/Dropbox/Doctorado/Datasets/20171014-CurveSet3D/4-Analysis/historico_ann/history")
                            .resolve(Utils.formatDouble(cr) + "-"
                            + Utils.formatDouble(wf) + "-"
                            + Utils.formatDouble(wm) + "-"
                            + ".csv")));

                    double fitness = bestSolution.getFitness();
                    System.out.print(cr + "," + wf + "," + wm + ",");
                    System.out.print(fitness);
                    Data xTest = ProblemReader.getX("/home/joalceco/Dropbox/Doctorado/Datasets/20171014-CurveSet3D/1-OriginalData/testX.csv");
                    Data yTest = ProblemReader.getY("/home/joalceco/Dropbox/Doctorado/Datasets/20171014-CurveSet3D/1-OriginalData/testY.csv");
                    Data yHat = bestSolution.epoch(xTest);
                    double mae = Error.computeError(yTest, yHat);
                    System.out.println(", " + mae);
                }
            }
        }
    }
}
