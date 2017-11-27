package pcell.types.neuro;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pcell.Population;
import pcell.algorithm.operators.Genetic;
import pcell.algorithm.operators.WeightMutation;
import pcell.model.ANN;
import pcell.model.ANNFactory;
import pcell.algorithm.Base;
import pcell.algorithm.operators.Differential;
import pcell.model.Model;
import pcell.types.ProcessingCell;
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

public class BasicNeuroPCell extends ProcessingCell {

    private BasicNeuroPCell(){

    }

    public static ProcessingCell buildBasicDifferential(){
        BasicNeuroPCell pCell = new BasicNeuroPCell();
        pCell.evaluator = new MaeEvaluator();
        pCell.algorithm = new Base<>(pCell.evaluator);
        pCell.algorithm = new Differential<>(
                pCell.algorithm,
                Global.paramD("crossover_rate"),
                Global.paramD("weigthing_factor")
        );

        pCell.control = new StaticController();
        return pCell;
    }

    public static ProcessingCell buildBasicDifferentialLS(){
        BasicNeuroPCell pCell = new BasicNeuroPCell();
        pCell.evaluator = new MaeEvaluator();
        pCell.algorithm = new Base<>(pCell.evaluator);
        pCell.algorithm = new Differential<>(
                pCell.algorithm,
                Global.paramD("crossover_rate"),
                Global.paramD("weigthing_factor")
        );
        pCell.algorithm = new WeightMutation<>(
                pCell.algorithm,
                Global.paramD("weight_mutation_p")
        );
        pCell.control = new StaticController();
        return pCell;
    }

    public static ProcessingCell buildBasicGeneticLS(){
        BasicNeuroPCell pCell = new BasicNeuroPCell();
        pCell.evaluator = new MaeEvaluator();
        pCell.algorithm = new Base<>(pCell.evaluator);
        pCell.algorithm = new Genetic<>(
                pCell.algorithm,
                0.8
        );
        pCell.algorithm = new WeightMutation<>(
                pCell.algorithm,
                Global.paramD("weight_mutation_p")
        );
        pCell.control = new StaticController();
        return pCell;
    }

    public static ProcessingCell buildBasicGenetic(){
        BasicNeuroPCell pCell = new BasicNeuroPCell();
        pCell.evaluator = new MaeEvaluator();
        pCell.algorithm = new Base<>(pCell.evaluator);
        pCell.algorithm = new Genetic<>(
                pCell.algorithm,
                0.8
        );
        pCell.control = new StaticController();
        return pCell;
    }

    @Override
    public BasicNeuroPCell fit(Data X, Data Y){
        // TODO: 8/11/17 Agregar validaciones
        evaluator.prepareData(X,Y);
        buildPopulation(X,Y);
        evaluatePopulation(population,evaluator);
        control.reportStatistics(population);
//        System.out.println(population.toString());
        while (control.live()){
            algorithm.apply(population);
            control.reportStatistics(population);
        }
        return this;
    }

    private void evaluatePopulation(Population<ANN> population, Evaluator evaluator) {
        for (ANN ann : population) {
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

    @Override
    public Model getBestModel() {
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
