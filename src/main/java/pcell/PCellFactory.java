package pcell;

import pcell.algorithm.Base;
import pcell.algorithm.operators.*;
import pcell.controller.StaticController;
import pcell.evaluator.Error;
import pcell.types.ProcessingCell;
import pcell.types.neuro.BasicNeuroPCell;
import utils.G;
import utils.loggers.NoManager;

public class PCellFactory {

    public static ProcessingCell buildBasicDifferential() {
        ProcessingCell pCell = BasicNeuroPCell.buildEmpty();
        pCell.setLogManager(new NoManager(pCell));
        pCell.setEvaluator(new Error());
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
        return pCell;
    }

    public static ProcessingCell buildBasicGenetic() {
        ProcessingCell pCell = BasicNeuroPCell.buildEmpty();
        pCell.setLogManager(new NoManager(pCell));
        pCell.setEvaluator(new Error());
        pCell.setAlgorithm(new Base<>(pCell));
        pCell.setAlgorithm(new Genetic<>(
                pCell.algorithm,
                G.getDoubleParam("crossover_rate")
        ));
        pCell.setAlgorithm(new RandomEliteTopologyMutation(
                pCell.algorithm
        ));
        pCell.setController(new StaticController(pCell));
        return pCell;
    }


    public static ProcessingCell buildBestMutator() {
        ProcessingCell pCell = BasicNeuroPCell.buildEmpty();
        pCell.setLogManager(new NoManager(pCell));
        pCell.setEvaluator(new Error());
        pCell.setAlgorithm(new Base<>(pCell));
        pCell.setAlgorithm(new SortPopulation(
                pCell.algorithm
                ));
        pCell.setAlgorithm(new MutateFromBest(
                pCell.algorithm,
                0.3
        ));
        pCell.setController(new StaticController(pCell));
        return pCell;
    }

    public static ProcessingCell buildSimulatingAnneling() {
        ProcessingCell pCell = BasicNeuroPCell.buildEmpty();
        pCell.setParam("population_size",1);
        pCell.setLogManager(new NoManager(pCell));
        pCell.setEvaluator(new Error());
        pCell.setAlgorithm(new Base<>(pCell));
        pCell.setAlgorithm(new SimulatingAnnealing(pCell.algorithm));
        pCell.setController(new StaticController(pCell));
        return pCell;
    }


//    public static ProcessingCell buildPCell(String name_id, Parameters params) {
//        Parameters localParams = params.copy();
//        SimpleOperatorFactory operatorFactory = new SimpleOperatorFactory();
//        ArrayList<String> algorithmList = localParams.getArrayListString("algorithm");
//        localParams.setParam("id", name_id);
//        Algorithm algorithm = operatorFactory.buildAlgorithm(algorithmList, localParams);
//        Evaluator evaluator = Evaluator.getErrorType(localParams.getString("error_type"));
//        StaticController controller = new StaticController(localParams);
//        return buildPCell(algorithm,evaluator,controller,localParams);
//    }

//    private static ProcessingCell buildPCell(
//            Algorithm algorithm,
//            Evaluator evaluator,
//            Controller controller,
//            Parameters params) {
//        ProcessingCell pCell;
//        switch (params.getString("type").toLowerCase()){
//            case "evolutionary":
//                return BasicNeuroPCell.build(algorithm, evaluator, controller, params);
//            case "svr":
//                return SVRPCell.build(algorithm, evaluator, controller, params);
//            default:
//                return null;
//        }
//
//    }
}
