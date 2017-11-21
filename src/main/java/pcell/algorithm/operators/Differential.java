package pcell.algorithm.operators;

import pcell.ann.ANN;
import pcell.algorithm.Algorithm;
import pcell.Population;
import cern.colt.list.tint.IntArrayList;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import com.google.common.graph.EndpointPair;
import utils.Global;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class Differential<T extends ANN> extends OperatorDecorator<T> {

    private double CROSSOVER_RATE = 0.8;
    private double WEIGTHING_FACTOR = 0.1;

    Algorithm<T> algorithm;

    public Differential(Algorithm<T> algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String getPseudoCode() {
        return algorithm.getPseudoCode() + " -> DifferentialCrossover";
    }

    private LinkedHashSet<Integer> selectParents(Population<T> pop){
        return Global.r.sample(3,0,pop.size());
    }

    @Override
    public Population<T> apply(Population<T> pop) {
        pop = algorithm.apply(pop);
        for (int i = 0; i<pop.size();i++){
            if(Global.r.nextDouble()<CROSSOVER_RATE){
            HashSet<Integer> parents = selectParents(pop);
            Iterator<Integer> it = parents.iterator();
            T original = pop.get(i);
            T candidate = (T) original.clone();
            T a = pop.get(it.next().intValue());
            T b = pop.get(it.next().intValue());
            T c = pop.get(it.next().intValue());
//                System.out.println(a);
//                System.out.println(b);
//                System.out.println(c);
            candidate = differential_crossover(candidate, a, b, c);
            evaluate(candidate);
//                System.out.println(candidate);
//                System.out.println();
            if(candidate.getFitness() < original.getFitness()){
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
    public double evaluate(T ann) {
        return algorithm.evaluate(ann);
    }

    /**
     * Differential Crossover calculation
     * The crossover edges are based of solution @param a.
     * @param candidate clone of original solution
     * @param a first parent
     * @param b second parent
     * @param c third parent
     * @return candidate solution for convenience
     */
    private T differential_crossover(T candidate, T a, T b, T c) {
        for (EndpointPair<Integer> edge:
             a.getEdges()) {
            Integer source = edge.source();
            Integer target = edge.target();
            double aw=a.weight(source, target);
            double bw=a.weight(source, target);
            double cw=a.weight(source, target);
            candidate.addConnection(source,target,aw+WEIGTHING_FACTOR*(bw-cw));
        }
        return candidate;
    }

}
