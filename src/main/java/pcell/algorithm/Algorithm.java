package pcell.algorithm;

import pcell.ann.ANN;
import pcell.Population;

public abstract class Algorithm<T extends ANN> {

    String pseudoCode = "No Code";

    public String getPseudoCode(){
        return pseudoCode;
    }

    public abstract Population<T> apply(Population<T> pop);

}
