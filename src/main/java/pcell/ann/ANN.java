package pcell.ann;

import cern.colt.list.tint.IntArrayList;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import com.google.common.graph.EndpointPair;
import utils.Data;

import java.util.Set;


public abstract class ANN {
    double fitness;
    public int bias_id = -1;

    public abstract void buildFromTemplate(ANN copy, boolean clone);

    public abstract ANN buildFromFitnessFunction(int nInputs, int nOutputs);

    public abstract int addRandomNeuron();

    public abstract void addNeuron(int neuronID);

    public abstract void deleteNeuron();

    public abstract void deleteNeuron(int neuronID);

    public abstract void deleteConnection();

    public abstract void deleteConnection(int originID, int destinyID);

    public abstract void addConnection();

    public abstract void addConnection(int originID, int destinyID);

    public abstract void addConnection(int originID, int destinyID, double weight);

    public abstract int selectRandomActiveOrInputNeuron();

    public abstract int addRandomConnection();

    public abstract int selectRandomDisableNeuron();

    public abstract int selectRandomHiddenNeuron(boolean active);

    public abstract int selectRandomUpperNeuron(int id, boolean active);

    public abstract int selectRandomLowerNeuron(int id, boolean active);

    public abstract int selectRandomActiveLowerNeurons(int id, boolean active);

//    public abstract boolean isInput();

    public abstract boolean isInput(int id);

    public abstract double getBias(int id);

//    public abstract double getActives(int id);

    public abstract IntArrayList getDestinies(int neuronID);

    public abstract void setBias(int id, double bias);

    public abstract void update();

    public abstract String toDot();

    public abstract void changeConnection();

    public abstract void changeConnection(int originID, int destinyID);

    public abstract DoubleMatrix2D getMatrixWeights();

    public abstract Data epoch(Data x);

    public abstract int getNumberOfNeurons();

    public abstract Object clone();

    public abstract ANN buildRandomANN(int nInputs, int nOutputs,int maxSize);

    public abstract Set<EndpointPair<Integer>> getEdges();

    public abstract double weight(Integer source, Integer target);

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
            return fitness;
    }
}
