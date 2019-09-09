package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;

public abstract class Operator<T extends ANN> extends Algorithm<T> {

    public abstract Population<T> apply(Population<T> pop, Evaluator evaluator);

}
