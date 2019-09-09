package pcell.algorithm.operators;

import com.google.common.graph.EndpointPair;
import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;

import java.util.LinkedHashSet;

public class WeightMutation<T extends ANN> extends Operator<T> {

    Algorithm<T> algorithm;
    double prob;

    private WeightMutation() {
    }

    public WeightMutation(Algorithm<T> algorithm, double prob) {
        this.algorithm = algorithm;
        this.prob = prob;
    }

    public static void mutate(ANN candidate) {
        EndpointPair<Integer> connection = candidate.selectRandomWeight();
        double weight = candidate.weight(connection.nodeU(), connection.nodeV());
        weight *= G.r.nextDouble(-1, 1);
        candidate.addConnection(connection.nodeU(), connection.nodeV(), weight);
    }

    @Override
    public String getPseudoCode() {
        return algorithm.getPseudoCode() + " -> " + this.getClass().getSimpleName();
    }

    @Override
    public double evaluate(T ann, Evaluator evaluator) {
        return algorithm.evaluate(ann, evaluator);
    }

    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
//        T original = pop.get(i);
//        T candidate = (T) original.clone();
        pop = algorithm.apply(pop, evaluator);
        int nElements = (int) (pop.size() * prob);
        LinkedHashSet<Integer> sample = G.r.sample(nElements, 0, pop.size());
        for (int i : sample) {
            T original = pop.get(i);
            T candidate = (T) original.clone();
            mutate(candidate);
            evaluate(candidate, evaluator);
            if (candidate.getFitness() < original.getFitness()) {
                pop.replaceSolution(i, candidate);
            }
        }
        return pop;
    }


}
