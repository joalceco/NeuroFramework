package pcell.algorithm.operators;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import utils.G;

public class SimulatingAnnealing<T extends ANN> extends Operator<T> {

    private Algorithm<T> algorithm;
    //    private double selectRandomProb = 0.25;
    private double topologyMutationProb;

    // Set initial temperature
    private double temperature;
    private double temperature_initial;
    private double temperature_final;
    // Cooling rate

    private double alpha;
    private double maximum_difference;
    private double minimum_difference;
    private double acceptance_probability_high;
    private double acceptance_probability_low;
    private double longuitud;
    private double n;
    private double beta;
    private int repetitions;
    private int metropolis_length_initial;
    private int metropolis_length_final;
    private double metropolis_length;

//    T best;
    public SimulatingAnnealing(Algorithm<T> algorithm) {
        this.algorithm = algorithm;
        cell = algorithm.cell;
        topologyMutationProb = G.getDoubleParam("topology_mutation_prob");
        alpha = G.getDoubleParam("sa", "alpha");
        maximum_difference = G.getDoubleParam("sa", "maximum_difference");
        minimum_difference = G.getDoubleParam("sa", "minimum_difference");
        acceptance_probability_high = G.getDoubleParam("sa", "acceptance_probability_high");
        acceptance_probability_low = G.getDoubleParam("sa", "acceptance_probability_low");
        metropolis_length_initial = G.getIntegerParam("sa", "metropolis_length_initial");
        metropolis_length_final = G.getIntegerParam("sa", "metropolis_length_final");
        initialCalculations();
    }

    // Calculate the acceptance probability
    private static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
//        SEGUN LALO
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    private void initialCalculations() {
        temperature_initial = -maximum_difference / Math.log(acceptance_probability_high);
        temperature_final = -minimum_difference / Math.log(acceptance_probability_low);
        temperature = temperature_initial;
        metropolis_length = metropolis_length_initial;
        repetitions = (int) metropolis_length;
        n = ((Math.log(temperature_final) - Math.log(temperature_initial)) / Math.log(alpha));
        beta = Math.exp((Math.log(metropolis_length_final) - Math.log(metropolis_length_initial)) / n);
//        coolSystem();
    }

    @Override
    public double evaluate(T ann, Evaluator evaluator) {
        return algorithm.evaluate(ann, evaluator);
    }

    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
        pop = algorithm.apply(pop, evaluator);
        for (int i = 0; i < repetitions; i++) {
            T currentSolution = pop.getBestModel();
            T newSolution = mutate(currentSolution);
            double currentEnergy = currentSolution.getFitness();
            double neighbourEnergy = evaluate(newSolution, evaluator);
//        // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temperature) > G.r.nextDouble()) {
                pop.replaceSolution(0, newSolution);
                currentSolution = newSolution;
            }
//
            // Keep track of the best solution found
            if (cell.bestModel == null) {
                cell.bestModel = currentSolution.clone();
            } else if (currentSolution.getFitness() < cell.bestModel.getFitness()) {
                cell.bestModel = currentSolution.clone();
            }
        }
        // Cool system
        coolSystem();
        return pop;
    }

    private void coolSystem() {
        temperature *= alpha;
        metropolis_length *= beta;
        repetitions = (int) metropolis_length;

    }

    private T mutate(T sol) {
        T t = (T) sol.clone();
        if (G.r.nextDouble() < topologyMutationProb) {
            return mutateTopology(t);
        } else {
            return mutateWeight(t);
        }
//        return t;
    }

    private T mutateWeight(T t) {
        int origin = t.selectRandomActiveOrInputNeuron();
        int destiny = t.selectRandomUpperNeuron(origin, true);
        if (t.connectionExist(origin, destiny)) {
            double weight = t.getWeight(origin, destiny);
            double newWeight = weight * G.r.nextDouble(-2, 2);
            t.addConnection(origin, destiny, newWeight);
        } else {
            t.addConnection(origin, destiny);
        }
        return t;
    }

    private T mutateTopology(T t) {
        switch (G.r.nextInt(2)) {
            case 0:
                return addNeuron(t);
            case 1:
                return removeWeigth(t);
        }
        return t;
    }

    private T addNeuron(T t) {
        int origin, newNeuron;
        int maxTries = 0;
        do {
            origin = t.selectRandomActiveOrInputNeuron();
            newNeuron = t.selectRandomUpperNeuron(origin, false);
            if (maxTries++ > 5) {
                return mutateWeight(t);
            }
        } while (newNeuron == -1);
        int destiny = t.selectRandomUpperNeuron(newNeuron, true);
        t.addConnection(origin, newNeuron);
        t.addConnection(t.bias_id, newNeuron);
        t.addConnection(newNeuron, destiny);
        return t;
    }

    private T removeWeigth(T t) {
        int origin, newNeuron;
        int maxTries = 0;
        for (int i = 0; i < 5; i++) {
            origin = t.selectRandomActiveOrInputNeuron();
            newNeuron = t.selectRandomUpperNeuron(origin, true);
            if(t.connectionExist(origin, newNeuron)){
                if(t.getPredecessorNeuronsOf(newNeuron).size()>2){
                    if(t.getSucessorNeuronsOf(origin).size()>2){
                      t.removeConnection(origin, newNeuron);
                      break;
                    }
                }
            }

        }
        return t;
    }

}
