package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;
import utils.Parameters;

import java.util.LinkedHashSet;

public class RandomMutation<T extends ANN> extends OperatorDecorator<T> {

    Algorithm<T> algorithm;

    private RandomMutation() {
    }

    public RandomMutation(Algorithm<T> algorithm, double prob) {
        this.algorithm = algorithm;
        params = new Parameters();
        params.setParam("w_mutation_rate", prob);
    }

    public RandomMutation(Algorithm<T> algorithm, Parameters parameters) {
        this.algorithm = algorithm;
        params = parameters;
    }

    @Override
    public double evaluate(T ann, Evaluator evaluator) {
        return algorithm.evaluate(ann, evaluator);
    }


    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
        pop = algorithm.apply(pop, evaluator);
        LinkedHashSet<Integer> sample = G.r.sample(
                (int) (pop.size() * params.getDouble("w_mutation_rate"))
                , 0,
                pop.size() - 1);
        for (int sol : sample) {
            mutate(sol, pop);
            evaluate(pop.get(sol), evaluator);
        }
        return pop;
    }

    private void mutate(int sol, Population<T> pop) {
        T t = pop.get(sol);
        int origin = t.selectRandomActiveOrInputNeuron();
        int destiny = t.selectRandomUpperNeuron(origin, true);
        t.addConnection(origin, destiny);
    }


}
