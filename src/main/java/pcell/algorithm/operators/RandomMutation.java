package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.model.ANN;
import utils.Global;

import java.util.LinkedHashSet;

public class RandomMutation<T extends ANN> extends OperatorDecorator<T>{

    Algorithm<T> algorithm;
    double prob;

    private RandomMutation(){
    }

    public RandomMutation(Algorithm<T> algorithm,double prob) {
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
        LinkedHashSet<Integer> sample = Global.r.sample(
                (int)(pop.size() * prob)
                , 0,
                pop.size() - 1);
        for (int sol: sample) {
            mutate(sol,pop);
            evaluate(pop.get(sol));
        }
        return pop;
    }

    private void mutate(int sol, Population<T> pop) {
        T t = pop.get(sol);
        int origin = t.selectRandomActiveOrInputNeuron();
        int destiny = t.selectRandomUpperNeuron(origin, true);
        t.addConnection(origin,destiny);
    }


}
