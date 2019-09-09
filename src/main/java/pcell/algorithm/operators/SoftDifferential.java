package pcell.algorithm.operators;

import com.google.common.graph.EndpointPair;
import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;
import utils.Parameters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class SoftDifferential<T extends ANN> extends Operator<T> {

    private Algorithm<T> algorithm;

    public SoftDifferential(Algorithm<T> algorithm, double cr, double wf) {
        this.algorithm = algorithm;
        this.params = algorithm.params;
        params.setParam("crossover_rate", cr);
        params.setParam("weighting_factor", wf);

    }

    public SoftDifferential(Algorithm<T> algorithm, Parameters parameters) {
        this.algorithm = algorithm;
        params = parameters;
    }

    @Override
    public String getPseudoCode() {
        return algorithm.getPseudoCode() + " -> DifferentialCrossover";
    }

    private LinkedHashSet<Integer> selectParents(Population<T> pop) {
        return G.r.sample(3, 0, pop.size());
    }

    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
        pop = algorithm.apply(pop, evaluator);
        for (int i = 0; i < pop.size(); i++) {
            if (G.r.nextDouble() < params.getDouble("crossover_rate")) {
                HashSet<Integer> parents = selectParents(pop);
                Iterator<Integer> it = parents.iterator();
                T original = pop.get(i);
                T candidate = (T) original.clone();
                T a = pop.get(it.next());
                T b = pop.get(it.next());
                T c = pop.get(it.next());
                candidate = differential_crossover(candidate, a, b, c);
                evaluate(candidate, evaluator);
                if (candidate.getFitness() < original.getFitness()) {
                    pop.remove(original);// TODO: 17/11/17 Reemplazar no quitar
                    pop.add(candidate);
                }
                //ELSE- candidate slowly perish while the rest of his friends get successful jobs y beautiful chicks,
                // he was born alone and will die alone, nothing with his name on it other that his tombstone. RIPeperonis SO SAD :(
            }
        }

        return pop;
    }


    @Override
    public double evaluate(T ann, Evaluator evaluator) {
        return algorithm.evaluate(ann, evaluator);
    }

    /**
     * Differential Crossover calculation
     * The crossover edges are based of solution @param a.
     *
     * @param candidate clone of original solution
     * @param a         first parent
     * @param b         second parent
     * @param c         third parent
     * @return candidate solution for convenience
     */
    private T differential_crossover(T candidate, T a, T b, T c) {
        double wf = params.getDouble("weighting_factor");
//        G.r.selectRandomElement(a.getEdges());
        for (EndpointPair<Integer> edge :
                a.getEdges()) {
            Integer source = edge.source();
            Integer target = edge.target();
            double aw = a.weight(source, target);
            double bw = b.weight(source, target);
            double cw = c.weight(source, target);
            candidate.addConnection(source, target, aw + wf * (bw - cw));
        }
        return candidate;
    }

}
