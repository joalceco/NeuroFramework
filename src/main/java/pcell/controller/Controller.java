package pcell.controller;

import pcell.Population;
import pcell.model.ANN;
import pcell.types.ProcessingCell;

public abstract class Controller {

    //    LogManager history;
    ProcessingCell cell;

    public Controller() {
    }

    public abstract void reportStatistics(Population<ANN> population);

    public abstract int intParameter(String param);

    public abstract boolean live();

}
