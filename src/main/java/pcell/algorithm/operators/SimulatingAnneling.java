package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;
import utils.Parameters;

public class SimulatingAnneling<T extends ANN> extends OperatorDecorator<T> {

    private Algorithm<T> algorithm;
    private double selectRandomProb = 0.25;
    private double topologyMutationProb = 0.30;

    // Set initial temp
    double temp = 10000;
    // Cooling rate
    double coolingRate = 0.03;

//    T best;


    private SimulatingAnneling() {
    }

    public SimulatingAnneling(Algorithm<T> algorithm, double prob) {
        this.algorithm = algorithm;
        this.cell = algorithm.cell;
        params = new Parameters();
        params.setParam("w_mutation_rate", prob);
    }

    public SimulatingAnneling(Algorithm<T> algorithm, Parameters parameters) {
        this.algorithm = algorithm;
        this.cell = algorithm.cell;
        params = parameters;
    }

    @Override
    public double evaluate(T ann, Evaluator evaluator) {
        return algorithm.evaluate(ann, evaluator);
    }


    // Calculate the acceptance probability
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }


    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
        pop = algorithm.apply(pop, evaluator);
//        // Create new neighbour tour
        T currentSolution = pop.getBestModel();
//        // Get a random positions in the tour
        T newSolution = mutate(currentSolution);

//        // Get energy of solutions
        double currentEnergy = currentSolution.getFitness();
        double neighbourEnergy = evaluate(newSolution, evaluator);
//
//        // Decide if we should accept the neighbour
        if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > G.r.nextDouble()) {
            pop.replaceSolution(0,newSolution);
            currentSolution = newSolution;
        }
//
        // Keep track of the best solution found
        if(cell.bestModel == null){
            cell.bestModel = (T)currentSolution.clone();
        }else if (currentSolution.getFitness() < evaluate((T)cell.bestModel, evaluator)) {
            cell.bestModel = (T)currentSolution.clone();
        }
        // Cool system
        temp *= 1-coolingRate;
        return pop;
    }

    private T mutate(T sol) {
        T t = (T) sol.clone();
        if (G.r.nextDouble() < topologyMutationProb) {
            return mutateTopology(t);
        } else {
            return mutateWeigth(t);
        }
//        return t;
    }

    private T mutateWeigth(T t) {
        int origin = t.selectRandomActiveOrInputNeuron();
        int destiny = t.selectRandomUpperNeuron(origin, true);
        if(t.connectionExist(origin, destiny)){
            double weight = t.getWeight(origin, destiny);
            double newweight=weight*G.r.nextDouble(-2,2);
            t.addConnection(origin,destiny,newweight);
        }else{
            t.addConnection(origin, destiny);
        }
        return t;
    }

    private T mutateTopology(T t) {
        int origin, newNeuron;
        int maxtries = 0;
        do {
            origin = t.selectRandomActiveOrInputNeuron();
            newNeuron = t.selectRandomUpperNeuron(origin, false);
            if(maxtries++>5){
                return mutateWeigth(t);
            }
        }
        while (newNeuron == -1);
        int destiny = t.selectRandomUpperNeuron(newNeuron, true);
        t.addConnection(origin, newNeuron);
        t.addConnection(t.bias_id, newNeuron);
        t.addConnection(newNeuron, destiny);
        return t;
    }


}
