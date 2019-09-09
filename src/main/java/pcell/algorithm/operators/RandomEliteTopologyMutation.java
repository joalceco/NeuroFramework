package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;
import utils.Parameters;

import java.util.LinkedHashSet;

public class RandomEliteTopologyMutation<T extends ANN> extends Operator<T> {

    Algorithm<T> algorithm;
    int random_mutation_countdown;

    public RandomEliteTopologyMutation(Algorithm<T> algorithm) {
        this.algorithm = algorithm;
        params = new Parameters();

        random_mutation_countdown = G.getIntegerParam("topology_mutation_frequency");
        params.setParam("topology_mutation_frequency", G.getIntegerParam("topology_mutation_frequency"));
        params.setParam("topology_mutation_rate", G.getDoubleParam("topology_mutation_rate"));
        params.setParam("elite_size", G.getDoubleParam("elite_size"));
    }

    public RandomEliteTopologyMutation(Algorithm<T> algorithm, double prob, double eliteSize) {
        this.algorithm = algorithm;
        params = new Parameters();
        random_mutation_countdown = G.getIntegerParam("topology_mutation_frequency");
        params.setParam("topology_mutation_rate", prob);
        params.setParam("elite_size", eliteSize);
    }

    public RandomEliteTopologyMutation(Algorithm<T> algorithm, Parameters parameters) {
        this.algorithm = algorithm;
        random_mutation_countdown = G.getIntegerParam("topology_mutation_frequency");
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
        if(random_mutation_countdown <= 0) {
            LinkedHashSet<Integer> sample = G.r.sample(
                    (int) Math.ceil(pop.size() * params.getDouble("topology_mutation_rate")),
                    (int) Math.ceil(pop.size() * params.getDouble("elite_size")),
                    pop.size() - 1);
            for (int sol : sample) {
                mutate(sol, pop);
                evaluate(pop.get(sol), evaluator);
            }
            random_mutation_countdown = params.getInt("topology_mutation_frequency");
        }else{
            random_mutation_countdown--;
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
