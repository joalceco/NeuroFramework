package pcell.algorithm;


import pcell.ann.ANN;
import pcell.Population;

public class Base<T extends ANN> extends Algorithm<T>{

    public Base() {
        pseudoCode="Base";
    }

    @Override
    public Population<T> apply(Population<T> pop) {
        return pop;
    }
}
