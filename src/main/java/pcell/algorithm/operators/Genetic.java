package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;
import utils.Parameters;

import java.util.*;

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
                T daddy = pop.get(it.next());
                T mommy = pop.get(it.next());
                T candidate = (T) daddy.clone();
                candidate = conventionalGNPCrossover(candidate, daddy, mommy);
                if(G.r.nextDouble() < G.getDoubleParam("weight_mutation_prob")){
                    WeightMutation.mutate(candidate);
                }
                if(G.r.nextDouble() < G.getDoubleParam("add_mutation_prob")){
                    AddConection.mutate(candidate);
                }
                if(G.r.nextDouble() < G.getDoubleParam("add_node_prob")){
                    AddNeuron.mutate(candidate);
                }
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
     * conventional GNP Crossover
     * The crossover edges are based of solution @param a.
     * Paper: "Comparing some graph crossover in genetic network programming", doi: 10.1109/SICE.2002.1195369
     *
     * @param candidate clone of original solution
     * @param a         first parent
     * @param b         second parent
     * @return candidate solution for convenience
     */
    private T conventionalGNPCrossover(T candidate, T a, T b) {
//        List<Integer> hiddenNeurons = b.getHiddenNeurons();
        int origin = G.r.selectRandomElement(b.getActiveInputs());
        int destiny;
        double u = G.r.nextDouble();
        do {
            Set<Integer> destiniesSet = b.getDestiniesSet(origin);
            destiny = G.r.selectRandomElement(destiniesSet);
            if(!candidate.isOutput(destiny)) {
                if (candidate.isActive(destiny)) {
                    double bias = crossoverWeigth(a.getBias(destiny), b.getBias(destiny), u);
                    candidate.addBias(destiny, bias);
                } else {
                    candidate.addBias(destiny, b.getBias(destiny));
                }
            }
            double P2 = b.getWeight(origin, destiny);
            if(a.connectionExist(origin,destiny)){
                double P1 = a.getWeight(origin, destiny);

                double H = crossoverWeigth(P1, P2, u);

                candidate.addConnection(origin, destiny, H);

            }else{
                candidate.addConnection(origin,destiny,P2);
            }
            origin = destiny;
        } while (!candidate.isOutput(destiny));
        return candidate;
    }

    private double crossoverWeigth(double P1, double P2, double u) {
        double nc = 2;
        double b = 0;
        double exponent = 1 / (nc + 1);
        if (u <= 0.5) {
            b = Math.pow(2 * u, exponent);
        } else {
            b = Math.pow(1 / (2 * (1 - u)), exponent);
        }
        if (G.r.nextBoolean()) {
            return 0.5 * ((P1 + P2) - b * (P2 - P1));
        } else {
            return 0.5 * ((P1 + P2) + b * (P2 - P1));
        }

    }

}
