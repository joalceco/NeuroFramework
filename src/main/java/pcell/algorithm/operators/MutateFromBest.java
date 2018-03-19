package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;
import utils.Parameters;

public class MutateFromBest<T extends ANN> extends OperatorDecorator<T> {

    private Algorithm<T> algorithm;
    private double selectRandomProb = 0.25;
    private double topologyMutationProb = 0.30;


    private MutateFromBest() {
    }

    public MutateFromBest(Algorithm<T> algorithm, double prob) {
        this.algorithm = algorithm;
        params = new Parameters();
        params.setParam("w_mutation_rate", prob);
    }

    public MutateFromBest(Algorithm<T> algorithm, Parameters parameters) {
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
        T model = pop.getBestModel();
        if (G.r.nextDouble() < selectRandomProb) {
            model = pop.get(G.r.nextInt(pop.size()));
        }
        for (int i = 1; i < pop.size(); i++) {
            pop.replaceSolution(i, mutate(model));
            evaluate(pop.get(i), evaluator);
        }
//        for (int sol : sample) {
//            mutate(sol, pop);
//            evaluate(pop.get(sol), evaluator);
//        }
        return pop;
    }

    private T mutate(T sol) {
        T t = (T) sol.clone();
        if (G.r.nextDouble() < topologyMutationProb) {
            mutateTopology(t);
        } else {
            mutateWeigth(t);
        }
        return t;
    }

    private T mutateWeigth(T t) {
        int origin = t.selectRandomActiveOrInputNeuron();
        int destiny = t.selectRandomUpperNeuron(origin, true);
        t.addConnection(origin, destiny);
        return t;
    }

    private T mutateTopology(T t) {
        int origin, newNeuron;
        do {
            origin = t.selectRandomActiveOrInputNeuron();
            newNeuron = t.selectRandomUpperNeuron(origin, false);
        }
        while (newNeuron == -1);
        int destiny = t.selectRandomUpperNeuron(newNeuron, true);
        t.addConnection(origin, newNeuron);
        t.addConnection(t.bias_id, newNeuron);
        t.addConnection(newNeuron, destiny);
        return t;
    }


}
