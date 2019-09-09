package pcell.types;

import pcell.Population;
import pcell.algorithm.Algorithm;
import pcell.controller.Controller;
import pcell.evaluator.Evaluator;
import pcell.model.ANN;
import pcell.model.Model;
import utils.Data;
import utils.Parameters;
import utils.loggers.LogManager;
import utils.loggers.NoManager;

import java.nio.file.Path;

public abstract class ProcessingUnit {
    public Population<ANN> population;
    public Algorithm<ANN> algorithm;
    public Controller control;
    public Evaluator evaluator;
    public Parameters params;
    public LogManager logger = new NoManager(this);
    public ANN bestModel;

    public abstract ProcessingUnit fit(Data X, Data Y);
    public abstract ProcessingUnit fit();

    public Data predict(Data X) {
        return getBestModel().epoch(X);
    }

    public abstract Model getBestModel();

    public void setParam(String key, Object obj) {
        params.setParam(key, obj);
    }

//    public abstract void historyToCSV(String s) throws IOException;

    public void setLogManager(LogManager logManager) {
        this.logger = logManager;
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }


    public void setController(Controller control) {
        this.control = control;
    }

    public abstract void saveAs(Path models);
}
