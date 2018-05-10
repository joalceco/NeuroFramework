package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;
import utils.Parameters;

import java.util.LinkedHashSet;

public class RandomEliteTopologyMutation<T extends ANN> extends OperatorDecorator<T> {

    Algorithm<T> algorithm;
    int generation;

    public RandomEliteTopologyMutation(Algorithm<T> algorithm) {
        this.algorithm = algorithm;
        params = new Parameters();
        generation=0;
        params.setParam("topology_mutation_rate", G.getDoubleParam("topology_mutation_rate"));
        params.setParam("elite_size", G.getDoubleParam("elite_size"));
    }

    public RandomEliteTopologyMutation(Algorithm<T> algorithm, double prob, double eliteSize) {
        this.algorithm = algorithm;
        params = new Parameters();
        generation=0;
        params.setParam("topology_mutation_rate", prob);
        params.setParam("elite_size", eliteSize);
    }

    public RandomEliteTopologyMutation(Algorithm<T> algorithm, Parameters parameters) {
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
        pop.sort(ANN::compareTo);
        if(generation%1000==0) {
            LinkedHashSet<Integer> sample = G.r.sample(
                    (int) Math.ceil(pop.size() * params.getDouble("topology_mutation_rate")),
                    (int) Math.ceil(pop.size() * params.getDouble("elite_size")),
                    pop.size() - 1);
            for (int sol : sample) {
                mutate(sol, pop);
                evaluate(pop.get(sol), evaluator);
            }
        }
        generation++;
        return pop;
    }

    private void mutate(int sol, Population<T> pop) {
        T t = pop.get(sol);
        int origin = t.selectRandomActiveOrInputNeuron();
        int destiny = t.selectRandomUpperNeuron(origin, true);
        t.addConnection(origin, destiny);
    }


}
