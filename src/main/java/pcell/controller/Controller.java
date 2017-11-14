package pcell.controller;

import pcell.ann.ANN;
import pcell.ann.GuavaANN;
import pcell.Population;

import java.util.LinkedList;
import java.util.List;

public abstract class Controller {

    public abstract void feed(Population population);

    class Log{
        int epoch;
        double maxFitness;

    }

    List<Log> history;

    public Controller() {
        history = new LinkedList<Log>();
    }

    public abstract double realParameter(String param);
    public abstract int intParameter(String param);
    public abstract String stringParameter(String param);
    public abstract void addLog(int epoch, double fitness);
    public abstract void nextCycle();
    public abstract boolean live();

}
