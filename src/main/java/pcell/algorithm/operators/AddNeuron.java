package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;

import java.util.LinkedHashSet;

public class AddNeuron<T extends ANN> extends OperatorDecorator<T> {

    Algorithm<T> algorithm;
    double prob;

    private AddNeuron() {
    }

    public AddNeuron(Algorithm<T> algorithm, double prob) {
        this.algorithm = algorithm;
        this.prob = prob;
    }

    public static void mutate(ANN candidate) {
        candidate.addRandomNeuron();
    }

    @Override
    public String getPseudoCode() {
        return algorithm.getPseudoCode() + " -> " + this.getClass().getSimpleName();
    }

    @Override
    public double evaluate(T ann, Evaluator evaluator) {
       return  algorithm.evaluate(ann, evaluator);
    }

    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
        return pop;
    }


}
