package pcell.controller;

import pcell.Population;
import pcell.model.ANN;
import pcell.types.ProcessingCell;
import utils.G;
import utils.Log;

public class StaticController extends Controller {

//    Parameters params;

//    public StaticController(LogManager logger) {
//        params = Parameters.initializeParameters();
//        this.history = logger;
//    }

    public StaticController(ProcessingCell cell) {
//        this.params = cell.params;
        this.cell = cell;
        cell.params.setParam("generation", 0);
        cell.params.setParam("epoch", 0);
    }

    @Override
    public void reportStatistics(Population<ANN> population) {
        for (int i = 0; i < population.size(); i++) {
            ANN ann = population.get(i);
            Log log = new Log(
                    ann.getID(),
                    cell.params.getInt("generation"),
                    G.evaluations,
                    ann.getFitness(),
                    i,
                    ann.getNumberOfNeurons(),
                    ann.toDot());
            cell.logger.pushLog(log);
        }
        cell.logger.flush();
        cell.params.increment("generation");
    }

    @Override
    public int intParameter(String param) {
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
