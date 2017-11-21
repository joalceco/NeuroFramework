package pcell.controller;

import pcell.ann.ANN;
import pcell.Population;
import utils.Global;
import utils.Log;

public class StaticController extends Controller {

    int generation = 0;

    @Override
    public void reportStatistics(Population<ANN> population) {
        for (int i = 0; i < population.size(); i++) {
            ANN ann= population.get(i);
            Log log = new Log(
                    ann.getID(),
                    generation,
                    Global.evaluations,
                    ann.getFitness(),
                    i,
                    ann.getNumberOfNeurons(),
                    ann.toDot());
            history.add(log);
        }
        generation++;
    }

    @Override
    public int intParameter(String param) {
        return Global.getIntegerParam(param);
    }

    @Override
    public boolean live() {
        return Global.evaluations<Global.getMaxEvaluations();
    }
}
