package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.model.ANN;

public class RandomMutationLastIndividuals<T extends ANN> extends OperatorDecorator<T>{

    Algorithm<T> algorithm;
    double prob;

    private RandomMutationLastIndividuals(){
    }

    public RandomMutationLastIndividuals(Algorithm<T> algorithm, double prob) {
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
        int nelements = (int)(pop.size() * prob);
        for (int i = pop.size()-nelements; i < pop.size(); i++) {
            mutate(i,pop);
            mutate(i,pop);
            evaluate(pop.get(i));
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
