package pcell.model;

import utils.Data;

public abstract class Model {
    double fitness;

    public abstract Data epoch(Data x);

    public double getFitness() {
        return fitness;
    }
}
