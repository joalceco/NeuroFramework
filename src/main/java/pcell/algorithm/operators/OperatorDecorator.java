package pcell.algorithm.operators;

import pcell.ann.ANN;
import pcell.algorithm.Algorithm;
import pcell.Population;

public abstract class OperatorDecorator<T extends ANN> extends Algorithm<T> {

    public abstract Population<T> apply(Population<T> pop);

}
