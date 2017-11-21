package pcell.evaluator;

import pcell.ann.ANN;
import utils.Data;

public abstract class Evaluator {

    Data X,Y;

    public abstract double evaluate(ANN ann);

    public void prepareData(Data x, Data y) {
        this.X=x;
        this.Y=y;
    }
}
