package com.itcm;

import pcell.types.ProcessingUnit;
import utils.Data;

import java.util.HashMap;
import java.util.Map;

public class CellularProcessing {

    private HashMap<String, ProcessingUnit> cells;
    private HashMap<String, Double> probabilities = new HashMap<>();

    private CellularProcessing() {
        cells = new HashMap<>();
    }

    public static CellularProcessing buildBasicCPA() {
////        Genetic	differentialLS	geneticLS	svr	differential
////        -0.422565712,-0.0415381,0.397754818,1.058045602,0.009326618
//        CellularProcessing cellularProcessing = new CellularProcessing();
//        cellularProcessing.addPCell("differential", BasicNeuroPCell.buildBasicDifferential());
//        cellularProcessing.addPCell("genetic", BasicNeuroPCell.buildBasicGenetic());
//        cellularProcessing.addPCell("differentialLS", BasicNeuroPCell.buildBasicDifferentialLS());
//        cellularProcessing.addPCell("geneticLS", BasicNeuroPCell.buildBasicGeneticLS());
//        cellularProcessing.addPCell("svr", SVRPCell.buildBasicSVR());
//        return cellularProcessing;
        return null;
    }

    public static CellularProcessing buildCPAFromDefaults() {
//        CellularProcessing cellularProcessing = new CellularProcessing();
//        Map<String, Object> params_default = G.getMapParam("cells_params_default");
//        params_default.forEach((String namePCell, Object params) -> {
//            Parameters parameters = new Parameters((Map<String, Object>) params);
//            ProcessingCell processingCell = PCellFactory.buildPCell(namePCell, parameters);
//            cellularProcessing.addPCell(namePCell,processingCell);
//        });
////        cellularProcessing.addPCell("differential", BasicNeuroPCell.buildBasicDifferential());
////        cellularProcessing.addPCell("genetic", BasicNeuroPCell.buildBasicGenetic());
////        cellularProcessing.addPCell("differentialLS", BasicNeuroPCell.buildBasicDifferentialLS());
////        cellularProcessing.addPCell("geneticLS", BasicNeuroPCell.buildBasicGeneticLS());
////        cellularProcessing.addPCell("svr", SVRPCell.buildBasicSVR());
//        return cellularProcessing;
        return null;
    }

    public void addPCell(String name, ProcessingUnit cell) {
        cells.put(name, cell);
    }

    public void fit(Data X, Data Y) {
        cells.forEach((name, processingCell) -> processingCell.fit(X, Y));
    }

    public Data test(Data X) {
        HashMap<String, Data> results = new HashMap<>();
        cells.forEach((name, processingCell) -> {
            Data test = processingCell.predict(X);
            System.out.println(name);
            System.out.println(test.getRawMatrix());
            results.put(name, test);
        });
        return ensemble(results);
    }

    private Data ensemble(Map<String, Data> results) {
        probabilities.put("dif", 0.009326618);
        probabilities.put("ga", -0.422565712);
        probabilities.put("dif-bl", -0.0415381);
        probabilities.put("ga-bl", 0.397754818);
        probabilities.put("svr", 1.058045602);
        results.forEach((name, data) -> {
            // TODO: 22/11/17 apply a search of best values
            data.getRawMatrix().assign(value -> value * probabilities.get(name));
        });
        Data yHat = Data.sumData(results);
        System.out.println(yHat);
        return yHat;
    }
}
