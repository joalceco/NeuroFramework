package pcell.algorithm;


import pcell.ann.ANN;
import pcell.Population;
import pcell.evaluator.Evaluator;

public class Base<T extends ANN> extends Algorithm<T>{

    public Base(Evaluator evaluator) {
        this.evaluator = evaluator;
        pseudoCode="Base";
    }

    @Override
    public Population<T> apply(Population<T> pop) {
        return pop;
    }
}
