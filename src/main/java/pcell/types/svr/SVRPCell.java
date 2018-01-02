package pcell.types.svr;

import pcell.algorithm.Algorithm;
import pcell.controller.Controller;
import pcell.evaluator.Evaluator;
import pcell.evaluator.MAError;
import pcell.model.Model;
import pcell.model.SVR;
import pcell.types.ProcessingCell;
import utils.Data;
import utils.Parameters;

public class SVRPCell extends ProcessingCell {

    SVR model;

    private SVRPCell() {
        model = new SVR();
        model.buildParameters();
    }

    public static SVRPCell buildBasicSVR() {
        SVRPCell svr = new SVRPCell();
        svr.evaluator = new MAError();
        svr.model.buildParameters();
        return svr;
    }

    public static ProcessingCell build(Algorithm algorithm, Evaluator evaluator, Controller controller, Parameters params) {
        SVRPCell svr = new SVRPCell();
//        buildParameters
        svr.evaluator = evaluator;
        svr.algorithm = algorithm;
        svr.control = controller;
        svr.params = params;
        return svr;
    }

    @Override
    public ProcessingCell fit(Data X, Data Y) {
        evaluator.prepareData(X, Y);
        model.buildProblem(X, Y);
        model.fit();
        double evaluate = evaluator.evaluate(model);
        System.out.println("fitness: " + evaluate);

//        model.
//        svm_model model = svm.svm_train(problem, param);
//        svm.svm_predict(model,dataPoint);
        return this;
    }

    @Override
    public Model getBestModel() {
        return model;
    }
}
