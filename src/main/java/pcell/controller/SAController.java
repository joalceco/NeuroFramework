package pcell.controller;

import pcell.Population;
import pcell.evaluator.Error;
import pcell.model.ANN;
import pcell.types.ProcessingUnit;
import utils.Data;
import utils.G;
import utils.Log;

public class SAController extends Controller {

    //    Parameters params;

//    public StaticController(LogManager logger) {
//        params = Parameters.initializeParameters();
//        this.history = logger;
//    }

    public SAController(ProcessingUnit cell) {
//        this.params = cell.params;
        this.cell = cell;
        cell.params.setParam("generation", 0);
        cell.params.setParam("epoch", 0);
    }

    @Override
    public void reportStatistics(Population<ANN> population) {
//        for (int i = 0; i < population.size(); i++) {
//            ANN ann = population.get(i);
//            Log log = new Log(
//                    ann.getID(),
//                    cell.params.getInt("generation"),
//                    G.evaluations,
//                    ann.getFitness(),
//                    i,
//                    ann.getNumberOfNeurons(),
//                    ann.toDot());
//            cell.logger.pushLog(log);
//        }
        double errorTrain = Double.NaN;
        double errorTest = Double.NaN;
        ANN ann = population.getBestModel();
        if(xTest != null){
            Data yHat = ann.epoch(xTrain);
            errorTrain = Error.computeError(yTrain, yHat,"mse");
            yHat = ann.epoch(xTest);
            errorTest = Error.computeError(yTest, yHat,"mse");
        }
        Log log = new Log(
                ann.getID(),
                cell.params.getInt("generation"),
                G.evaluations,
                errorTrain,
                errorTest,
                0,
                ann.getNumberOfNeurons(),
//                ann.toDot());
                "");
        cell.logger.pushLog(log);
        cell.logger.flush();
        cell.params.increment("generation");
    }

    @Override
    public int intParameter(String param) {
        if(cell.params.containsKey(param)){
            return cell.params.getInt(param);
        }
        return G.getIntegerParam(param);
    }

    @Override
    public boolean live() {

        if (G.evaluations > G.getMaxEvaluations()) {
            return false;
        }
        if (!cell.params.containsKey("max_epochs")) {
            return true;
        }
        return cell.params.getInt("epoch") < cell.params.getInt("max_epochs");

    }
}
