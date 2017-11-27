package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.model.ANN;

public class SortPopulation<T extends ANN> extends OperatorDecorator<T>{

    Algorithm<T> algorithm;
    double prob;

    private SortPopulation(){
    }

    public SortPopulation(Algorithm<T> algorithm, double prob) {
        this.algorithm = algorithm;
        this.prob = prob;
    }

    @Override
    public double evaluate(T ann) {
        return algorithm.evaluate(ann);
    }


    @Override
    public Population<T> apply(Population<T> pop) {
        pop = algorithm.apply(pop);
        pop.sort(ANN::compareTo);
        return pop;
    }


}
