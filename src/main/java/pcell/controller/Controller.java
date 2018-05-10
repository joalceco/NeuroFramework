package pcell.controller;

import pcell.Population;
import pcell.model.ANN;
import pcell.types.ProcessingCell;
import utils.Data;

public abstract class Controller {

    protected Data yTest;
    protected Data xTest;
    protected Data yTrain;
    protected Data xTrain;
    //    LogManager history;
    ProcessingCell cell;

    public Controller() {
    }

    public void setTestData(Data xTrain, Data yTrain, Data xTest, Data yTest){
        this.xTest = xTest;
        this.yTest = yTest;
        this.yTrain = yTrain;
        this.xTrain = xTrain;
    }

    public abstract void reportStatistics(Population<ANN> population);

    public abstract int intParameter(String param);

    public abstract boolean live();

}
