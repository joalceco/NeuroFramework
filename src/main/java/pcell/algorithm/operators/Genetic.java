package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;
import utils.Parameters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class Genetic<T extends ANN> extends OperatorDecorator<T> {

    Algorithm<T> algorithm;

    public Genetic(Algorithm<T> algorithm, double cp) {
        this.algorithm = algorithm;
        this.params = algorithm.params;
        params.setParam("crossover_rate", cp);
    }

    public Genetic(Algorithm<T> algorithm, Parameters localParams) {
        this.algorithm = algorithm;
        this.params = algorithm.params;
        params = localParams;
    }

    @Override
    public String getPseudoCode() {
        return algorithm.getPseudoCode() + " -> " + this.getClass().getSimpleName();
    }

    private LinkedHashSet<Integer> selectParents(Population<T> pop) {
        return G.r.sample(2, 0, pop.size());
    }

    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
        pop = algorithm.apply(pop, evaluator);
        int pop_size = pop.size();
        for (int i = 0; i < pop_size; i++) {
            if (G.r.nextDouble() < params.getDouble("crossover_rate")) {
                HashSet<Integer> parents = selectParents(pop);
                Iterator<Integer> it = parents.iterator();
                T original = pop.get(i);
                T candidate = (T) original.cloneEmpty();
                ;
                T daddy = pop.get(it.next());
                T mommy = pop.get(it.next());
                candidate = onePointCrossover(candidate, daddy, mommy);
                WeightMutation.mutate(candidate);
                evaluate(candidate, evaluator);
                pop.add(candidate);
            }
        }
        pop.sort(ANN::compareTo);
        while (pop.size() > pop_size) {
            pop.removeLast();
        }

        return pop;
    }


    @Override
    public double evaluate(T ann, Evaluator evaluator) {
        return algorithm.evaluate(ann, evaluator);
    }

    /**
     * One Point Crossover calculation
     * The crossover edges are based of solution @param a.
     *
     * @param candidate clone of original solution
     * @param a         first parent
     * @param b         second parent
     * @return candidate solution for convenience
     */
    private T onePointCrossover(T candidate, T a, T b) {
        ArrayList<Integer> usedNeurons = new ArrayList();
        usedNeurons.add(a.bias_id);
        for (Integer node : a.getNodes()) {
            if (!usedNeurons.contains(node)) {
                usedNeurons.add(node);
                if (G.r.nextDouble() < params.getDouble("crossover_rate")) {

                    for (int destiny : a.getDestiniesSet(node)) {
                        candidate.addConnection(node, destiny, a.weight(node, destiny));
                        candidate.addConnection(candidate.bias_id, node, a.weight(candidate.bias_id, node));
                    }
                } else if (b.getNodes().contains(node)) {
                    for (int destiny : b.getDestiniesSet(node)) {
                        candidate.addConnection(node, destiny, b.weight(node, destiny));
                        candidate.addConnection(candidate.bias_id, node, b.weight(candidate.bias_id, node));
                    }
                }
            }
        }
        for (Integer node : b.getNodes()) {
            if (!usedNeurons.contains(node)) {
                usedNeurons.add(node);
                if (G.r.nextDouble() < params.getDouble("crossover_rate")) {
                    for (int destiny : b.getDestiniesSet(node)) {
                        candidate.addConnection(node, destiny, b.weight(node, destiny));
                        candidate.addConnection(candidate.bias_id, node, b.weight(candidate.bias_id, node));
                    }
                }
            }
        }
        return candidate;
    }

}
