package pcell.algorithm.operators;

import cern.colt.list.tint.IntArrayList;
import com.google.common.graph.EndpointPair;
import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.model.ANN;
import utils.Global;

import java.util.*;

public class Genetic<T extends ANN> extends OperatorDecorator<T> {

    private double CROSSOVER_P = 0.8;

    Algorithm<T> algorithm;

    public Genetic(Algorithm<T> algorithm, double cp) {
        this.algorithm = algorithm;
        CROSSOVER_P = cp;

    }

    @Override
    public String getPseudoCode() {
        return algorithm.getPseudoCode() + " -> " + this.getClass().getSimpleName();
    }

    private LinkedHashSet<Integer> selectParents(Population<T> pop){
        return Global.r.sample(2,0,pop.size());
    }

    @Override
    public Population<T> apply(Population<T> pop) {
        pop = algorithm.apply(pop);
        int pop_size=pop.size();
        for (int i = 0; i<pop_size;i++){
            if(Global.r.nextDouble()<CROSSOVER_P){
            HashSet<Integer> parents = selectParents(pop);
            Iterator<Integer> it = parents.iterator();
            T original = pop.get(i);
            T candidate = (T)original.cloneEmpty();;
            T daddy = pop.get(it.next());
            T mommy = pop.get(it.next());
            candidate = onePointCrossover(candidate, daddy, mommy);
            WeightMutation.mutate(candidate);
            evaluate(candidate);
            pop.add(candidate);
            }
            }
            pop.sort(ANN::compareTo);
        while(pop.size()>pop_size){
            pop.removeLast();
        }

        return pop;
    }


    @Override
    public double evaluate(T ann) {
        return algorithm.evaluate(ann);
    }

    /**
     * One Point Crossover calculation
     * The crossover edges are based of solution @param a.
     * @param candidate clone of original solution
     * @param a first parent
     * @param b second parent
     * @return candidate solution for convenience
     */
    private T onePointCrossover(T candidate, T a, T b) {
        ArrayList<Integer> usedNeurons= new ArrayList();
        usedNeurons.add(a.bias_id);
        for (Integer node : a.getNodes()) {
            if(!usedNeurons.contains(node)){
                usedNeurons.add(node);
                if(Global.r.nextDouble()<CROSSOVER_P){

                    for (int destiny : a.getDestiniesSet(node)) {
                        candidate.addConnection(node,destiny,a.weight(node,destiny));
                        candidate.addConnection(candidate.bias_id,node,a.weight(candidate.bias_id,node));
                    }
                }else if(b.getNodes().contains(node)){
                    for (int destiny : b.getDestiniesSet(node)) {
                        candidate.addConnection(node,destiny,b.weight(node,destiny));
                        candidate.addConnection(candidate.bias_id,node,b.weight(candidate.bias_id,node));
                    }
                }
            }
        }
        for (Integer node : b.getNodes()) {
            if(!usedNeurons.contains(node)){
                usedNeurons.add(node);
                if(Global.r.nextDouble()<CROSSOVER_P){
                    for (int destiny : b.getDestiniesSet(node)) {
                        candidate.addConnection(node,destiny,b.weight(node,destiny));
                        candidate.addConnection(candidate.bias_id,node,b.weight(candidate.bias_id,node));
                    }
                }
            }
        }
        return candidate;
    }

}
