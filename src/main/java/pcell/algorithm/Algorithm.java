package pcell.algorithm;

import pcell.ann.ANN;
import pcell.Population;
import pcell.evaluator.Evaluator;

public abstract class Algorithm<T extends ANN> {

    String pseudoCode = "No Code";
    public Evaluator evaluator;

    public String getPseudoCode(){
        return pseudoCode;
    }

    public abstract Population<T> apply(Population<T> pop);

}
