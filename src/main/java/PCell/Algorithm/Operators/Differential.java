package PCell.Algorithm.Operators;

import PCell.ANN.ANN;
import PCell.Algorithm.Algorithm;
import PCell.Population;
import cern.colt.list.tint.IntArrayList;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
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
        return Global.r.sample(4,0,pop.size());
    }

    @Override
    public Population<T> apply(Population<T> pop) {
        pop = algorithm.apply(pop);
        for (int i =0; i<pop.size();i++){
            HashSet<Integer> parents = selectParents(pop);
            Iterator<Integer> it = parents.iterator();
            T original = pop.get(it.next().intValue());
            T a = pop.get(it.next().intValue());
            T b = pop.get(it.next().intValue());
            T c = pop.get(it.next().intValue());
            System.out.println();
//            T clone = (T) original.clone(true);
//            DoubleMatrix2D weights = clone.getMatrixWeights();
//
//            int neuron = clone.selectRandomActiveOrInputNeuron();
//            IntArrayList destinies = clone.getDestinies(neuron);
//            DoubleMatrix2D aw = clone.getMatrixWeights().viewSelection(new int[]{neuron}, destinies.elements());
//            DoubleMatrix2D bw = a.getMatrixWeights().viewSelection(new int[]{neuron}, destinies.elements());
//            DoubleMatrix2D cw = b.getMatrixWeights().viewSelection(new int[]{neuron}, destinies.elements());
//            DoubleMatrix2D dw = c.getMatrixWeights().viewSelection(new int[]{neuron}, destinies.elements());
//            System.out.println();
////            for (int j = 0; i < aw.size(); j++) {
////                if ( Global.r.nextDouble() < CROSSOVER_RATE) {
////                    clone.(
//                            s3.getQuick(i) + WEIGTHING_FACTOR * (s1.getQuick(i) - s2.getQuick(i)));
//                } else {
//                    child.add(s0.getQuick(i));
//                }
//            }
//            //see if is better than original, if so replace
//            if(fitnessFunction(original)<fitnessFunction(candidate)){
//                population.remove(original)
//                population.add(candidate)
            }
//        }

        return null;
    }

}
