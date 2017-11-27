package pcell.algorithm.operators;

import com.google.common.graph.EndpointPair;
import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.model.ANN;
import utils.Global;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class WeightMutation<T extends ANN> extends OperatorDecorator<T>{

    Algorithm<T> algorithm;
    double prob;

    private WeightMutation(){
    }

    public WeightMutation(Algorithm<T> algorithm, double prob) {
        this.algorithm = algorithm;
        this.prob = prob;
    }

    @Override
    public String getPseudoCode() {
        return algorithm.getPseudoCode() + " -> " + this.getClass().getSimpleName();
    }

    @Override
    public double evaluate(T ann) {
        return algorithm.evaluate(ann);
    }


    @Override
    public Population<T> apply(Population<T> pop) {
//        T original = pop.get(i);
//        T candidate = (T) original.clone();
        pop = algorithm.apply(pop);
        int nElements = (int)(pop.size() * prob);
        LinkedHashSet<Integer> sample = Global.r.sample(nElements, 0, pop.size());
        for (int i:sample) {
            T original = pop.get(i);
            T candidate = (T)original.clone();
            mutate(candidate);
            evaluate(candidate);
            if(candidate.getFitness()<original.getFitness()){
                pop.replaceSolution(i,candidate);
            }
        }
        return pop;
    }


    public static void mutate(ANN candidate) {
        EndpointPair<Integer> connection = candidate.selectRandomWeight();
        double weight = candidate.weight(connection.nodeU(), connection.nodeV());
        weight*=Global.r.nextDouble(-1,1);
        candidate.addConnection(connection.nodeU(),connection.nodeV(),weight);
    }


}
