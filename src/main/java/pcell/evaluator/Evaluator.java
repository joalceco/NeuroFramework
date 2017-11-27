package pcell.evaluator;

import pcell.model.ANN;
import pcell.model.Model;
import utils.Data;

public abstract class Evaluator {

    Data X,Y;

    public void prepareData(Data x, Data y) {
        this.X=x;
        this.Y=y;
    }

    public abstract double evaluate(Model model);

    public abstract double evaluate(Model model, Data X,Data Y);
}
