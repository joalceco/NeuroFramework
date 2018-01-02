package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;

public class SortPopulation<T extends ANN> extends OperatorDecorator<T> {

    Algorithm<T> algorithm;
    double prob;

    private SortPopulation() {
    }

    public SortPopulation(Algorithm<T> algorithm, double prob) {
        this.algorithm = algorithm;
        this.prob = prob;
    }

    @Override
    public double evaluate(T ann, Evaluator evaluator) {
        return algorithm.evaluate(ann, evaluator);
    }


    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
        pop = algorithm.apply(pop, evaluator);
        pop.sort(ANN::compareTo);
        return pop;
    }


}
