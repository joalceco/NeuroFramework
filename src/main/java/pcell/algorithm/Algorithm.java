package pcell.algorithm;

import pcell.Population;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import pcell.types.ProcessingUnit;
import utils.G;
import utils.Parameters;

public abstract class Algorithm<T extends ANN> {

    public Parameters params;
    public ProcessingUnit cell;
    String pseudoCode = "No Code";

    public String getPseudoCode() {
        return pseudoCode;
    }

    public abstract Population<T> apply(Population<T> pop, Evaluator evaluator);

    public double evaluate(T ann, Evaluator evaluator) {
        params.increment("epoch");
        G.evaluations++;
        double fitness = evaluator.evaluate(ann);
        fitness = (fitness*1) +
                (fitness*0.005) * ann.getNumberOfNeurons() +
                (fitness*0.005) * ann.getNumberOfConnections();
        ann.setFitness(fitness);
        return fitness;
    }

}
