package com.itcm;

import pcell.evaluator.MaeEvaluator;
import pcell.types.ProcessingCell;
import pcell.types.neuro.BasicNeuroPCell;
import pcell.types.svr.SVRPCell;
import utils.Data;
import utils.Global;
import utils.ProblemReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CellularProcessing {

    HashMap<String,ProcessingCell> cells;
    HashMap<String,Double> probabilities = new HashMap<>();

    private CellularProcessing() {
        cells = new HashMap<>();
    }

    public void addPCell(String name, ProcessingCell cell){
        cells.put(name, cell);
    }

    public static CellularProcessing buildBasicCPA(){
//        Genetic	differentialLS	geneticLS	svr	differential
//        -0.422565712,-0.0415381,0.397754818,1.058045602,0.009326618
        CellularProcessing cellularProcessing = new CellularProcessing();
        cellularProcessing.addPCell("differential", BasicNeuroPCell.buildBasicDifferential());
        cellularProcessing.addPCell("genetic", BasicNeuroPCell.buildBasicGenetic());
        cellularProcessing.addPCell("differentialLS", BasicNeuroPCell.buildBasicDifferentialLS());
        cellularProcessing.addPCell("geneticLS", BasicNeuroPCell.buildBasicGeneticLS());
        cellularProcessing.addPCell("svr", SVRPCell.buildBasicSVR());
        return cellularProcessing;
    }

    public void fit(Data X, Data Y) {
        cells.forEach((name, processingCell) -> {
            Global.evaluations=0;
            processingCell.fit(X, Y);
        });
    }

    public Data test(Data X) {
        HashMap<String, Data> results = new HashMap<>();
        cells.forEach((name, processingCell) -> {
            Data test = processingCell.test(X);
            System.out.println(name);
            System.out.println(test.getRawMatrix());
            results.put(name, test);
        });
        return ensemble(results);
    }

    public Data ensemble(Map<String, Data> results){
        probabilities.put("differential",0.009326618);
        probabilities.put("genetic",-0.422565712);
        probabilities.put("differentialLS",-0.0415381);
        probabilities.put("geneticLS",0.397754818);
        probabilities.put("svr",1.058045602);
        results.forEach((name, data) -> {
            // TODO: 22/11/17 apply a search of best values
            data.getRawMatrix().assign(value -> value*probabilities.get(name));
        });
        Data yHat = Data.sumData(results);
        System.out.println(yHat);
        return yHat;
    }

    public static void main(String[] args) throws IOException {
        Data xTrain = ProblemReader.getX("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/testX.csv");
        Data yTrain = ProblemReader.getY("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/testY.csv");
        Data xTest = ProblemReader.getX("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/trainX.csv");
        Data yTest = ProblemReader.getY("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/trainY.csv");

        CellularProcessing cpa = CellularProcessing.buildBasicCPA();
        cpa.fit(xTrain,yTrain);
        Data yHat = cpa.test(xTest);
        double mae = MaeEvaluator.computeError(yTest, yHat);
        System.out.println(mae);
    }

}
