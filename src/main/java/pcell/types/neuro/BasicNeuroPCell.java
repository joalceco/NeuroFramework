package pcell.types.neuro;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.controller.Controller;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import pcell.model.ANNFactory;
import pcell.model.Model;
import pcell.types.ProcessingCell;
import utils.Data;
import utils.G;
import utils.Parameters;

public class BasicNeuroPCell extends ProcessingCell {


    private BasicNeuroPCell() {
        params = Parameters.initializeParameters();
    }


//
//    public static ProcessingCell buildBasicDifferentialLS(){
//        BasicNeuroPCell pCell = new BasicNeuroPCell();
//        pCell.evaluator = new Error();
//        pCell.algorithm = new Base<>();
//        pCell.algorithm = new Differential<>(
//                pCell.algorithm,
//                G.paramD("crossover_rate"),
//                G.paramD("weighting_factor")
//        );
//        pCell.algorithm = new WeightMutation<>(
//                pCell.algorithm,
//                G.paramD("weight_mutation_p")
//        );
//        pCell.control = new StaticController(new NoManager());
//        return pCell;
//    }
//
//    public static ProcessingCell buildBasicGeneticLS(){
//        BasicNeuroPCell pCell = new BasicNeuroPCell();
//        pCell.evaluator = new Error();
//        pCell.algorithm = new Base<>();
//        pCell.algorithm = new Genetic<>(
//                pCell.algorithm,
//                0.8
//        );
//        pCell.algorithm = new WeightMutation<>(
//                pCell.algorithm,
//                G.paramD("weight_mutation_p")
//        );
//        pCell.control = new StaticController(new NoManager());
//        return pCell;
//    }
//
//    public static ProcessingCell buildBasicGenetic(){
//        BasicNeuroPCell pCell = new BasicNeuroPCell();
//        pCell.evaluator = new Error();
//        pCell.algorithm = new Base();
//        pCell.algorithm = new Genetic<>(
//                pCell.algorithm,
//                0.8
//        );
//        pCell.control = new StaticController(pCell.params);
//        return pCell;
//    }

    public static ProcessingCell build(Algorithm algorithm,
                                       Evaluator evaluator,
                                       Controller controller,
                                       Parameters params) {
        BasicNeuroPCell pCell = new BasicNeuroPCell();
        pCell.evaluator = evaluator;
        pCell.algorithm = algorithm;
        pCell.control = controller;
        pCell.params = params;
        return pCell;
    }

    public static ProcessingCell buildEmpty() {
        BasicNeuroPCell pCell = new BasicNeuroPCell();
        return pCell;
    }

    @Override
    public BasicNeuroPCell fit(Data X, Data Y) {
        // TODO: 8/11/17 Agregar validaciones
        evaluator.prepareData(X, Y);
        buildPopulation(X, Y);
        evaluatePopulation(population, evaluator);
        control.reportStatistics(population);
//        System.out.println(population.toString());
        int gen=0;
        while (control.live()) {
            gen++;
            if(gen%1000==0)
                System.out.println(gen);
            algorithm.apply(population, evaluator);
            control.reportStatistics(population);
            if(gen%50==0){
                evaluator.prepareNextBatch();
                algorithm.apply(population, evaluator);
            }
        }
        logger.flush();
        return this;
    }

    private void incrementEpoch() {
        params.increment("epoch");
        G.incrementEpoch();
    }

    private void evaluatePopulation(Population<ANN> population, Evaluator evaluator) {
        for (ANN ann : population) {
            incrementEpoch();
            ann.setFitness(evaluator.evaluate(ann));
        }
    }

//    @Override
//    public void historyToCSV(String s) throws IOException {
//        File file = new File(s);
//        file.getParentFile().mkdirs();
//        file.createNewFile();
//        FileWriter fileWriter = new FileWriter(file,true);
//        CSVPrinter printer = CSVFormat.RFC4180.print(fileWriter);
//        Log.csvHeadertoPrinter(printer);
//        List<Log> history = control.getHistory();
//        for (Log log: history) {
//            log.csvToPrinter(printer);
//        }
//        printer.close();
//    }

    private void buildPopulation(Data x, Data y) {
        ANNFactory factory = ANNFactory.buildANNFactory(x, y, control);
        population = Population.emptyPopulation();
        int population_size = control.intParameter("population_size");
        for (int i = 0; i < population_size; i++) {
            population.add(factory.buildANN());
        }
    }

    @Override
    public Model getBestModel() {
        if(bestModel != null){
            return bestModel;
        }
        population.sort(ANN::compareTo);
        return population.get(0);
    }
}
