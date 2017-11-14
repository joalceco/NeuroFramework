package pcell.controller;

import pcell.ann.ANN;
import pcell.Population;
import utils.Global;

public class StaticController extends Controller {

    @Override
    public void feed(Population population) {

    }

    @Override
    public double realParameter(String param) {
        return Global.getDoubleParam(param);
    }

    @Override
    public int intParameter(String param) {
        return Global.getIntegerParam(param);
    }

    @Override
    public String stringParameter(String param) {
        return null;
    }

    @Override
    public void addLog(int epoch, double fitness) {

    }

    @Override
    public void nextCycle() {

    }

    @Override
    public boolean live() {
        return Global.evaluations<Global.maxEvaluations;
    }
}
