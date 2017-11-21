package pcell;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pcell.algorithm.operators.RandomMutation;
import pcell.algorithm.operators.RandomMutationLastIndividuals;
import pcell.ann.ANN;
import pcell.ann.ANNFactory;
import pcell.algorithm.Algorithm;
import pcell.algorithm.Base;
import pcell.algorithm.operators.Differential;
import pcell.controller.Controller;
import pcell.controller.StaticController;
import pcell.evaluator.Evaluator;
import pcell.evaluator.MaeEvaluator;
import utils.Data;
import utils.Global;
import utils.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ProcessingCell {

    public Population<ANN> population;
    public Algorithm<ANN> algorithm;
    public Controller control;
    public Evaluator evaluator;

    private ProcessingCell(){

    }

    public static ProcessingCell buildBasicPCell(){
        ProcessingCell pCell = new ProcessingCell();
        pCell.evaluator = new MaeEvaluator();
        pCell.algorithm = new Base<>(pCell.evaluator);
        pCell.algorithm = new Differential<>(pCell.algorithm);
        pCell.algorithm = new RandomMutationLastIndividuals<>(pCell.algorithm,Global.paramD("mutation_p"));
        pCell.control = new StaticController();
        return pCell;
    }



//    public static ProcessingCell buildPCell(int numberInputs, int numberOutputs){
//        ProcessingCell pCell = new ProcessingCell();
//
//        return new ProcessingCell();
//    }


    public ProcessingCell fit(Data X, Data Y){
        // TODO: 8/11/17 Agregar validaciones
        evaluator.prepareData(X,Y);
        buildPopulation(X,Y);
        evaluatePopulation(population,evaluator);
//        System.out.println(population.toString());
        while (control.live()){
            algorithm.apply(population);
            control.reportStatistics(population);
        }
        return this;
    }

    private void evaluatePopulation(Population<ANN> population, Evaluator evaluator) {
        for (ANN ann :
                population) {
            ann.setFitness(evaluator.evaluate(ann));
        }
    }

    private void buildPopulation(Data x, Data y) {
        ANNFactory factory = ANNFactory.buildANNFactory(x, y,control);
        population = Population.emptyPopulation();
        int population_size = control.intParameter("population_size");
        for (int i = 0; i < population_size; i++) {
            population.add(factory.buildANN());
        }
    }

    public ANN getBestSolution() {
        population.sort(ANN::compareTo);
        return population.get(0);
    }

    public void historyToCSV(String s) throws IOException {
        File file = new File(s);
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        CSVPrinter printer = CSVFormat.RFC4180.print(fileWriter);
        Log.csvHeadertoPrinter(printer);
        List<Log> history = control.getHistory();
        for (Log log: history) {
            log.csvToPrinter(printer);
        }
        printer.close();
    }

}
