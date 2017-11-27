package pcell.types;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.model.ANN;
import pcell.model.Model;
import pcell.controller.Controller;
import pcell.evaluator.Evaluator;
import utils.Data;

public abstract class ProcessingCell {
    public Population<ANN> population;
    public Algorithm<ANN> algorithm;
    public Controller control;
    public Evaluator evaluator;

    public abstract ProcessingCell fit(Data X, Data Y);

    public Data test(Data X) {
        return getBestModel().epoch(X);
    }

    public abstract Model getBestModel();
}
