package pcell.algorithm;


import pcell.Population;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import pcell.types.ProcessingUnit;
import utils.Parameters;

public class Base<T extends ANN> extends Algorithm<T> {

    public Base() {
        params = new Parameters();
        pseudoCode = "Base";
    }

    public Base(ProcessingUnit cell) {
        this.cell = cell;
        params = cell.params;
        pseudoCode = "Base";
    }

    @Override
    public Population<T> apply(Population<T> pop, Evaluator evaluator) {
        return pop;
    }
}
