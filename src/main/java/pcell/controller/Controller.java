package pcell.controller;

import pcell.model.ANN;
import pcell.Population;
import utils.Log;

import java.util.LinkedList;
import java.util.List;

public abstract class Controller {

    public abstract void reportStatistics(Population<ANN> population);

    List<Log> history;

    public List<Log> getHistory() {
        return history;
    }

    public Controller() {
        history = new LinkedList<>();
    }
    public abstract int intParameter(String param);
    public abstract boolean live();

}
