package pcell.types.svr;

import pcell.algorithm.Algorithm;
import pcell.controller.Controller;
import pcell.evaluator.Evaluator;
import pcell.evaluator.Error;
import pcell.model.Model;
import pcell.model.SVR;
import pcell.types.ProcessingUnit;
import utils.Data;
import utils.Parameters;

import java.nio.file.Path;

public class SVRPUnit extends ProcessingUnit {

    SVR model;

    private SVRPUnit() {
        model = new SVR();
        model.buildParameters();
    }

    public static SVRPUnit buildBasicSVR() {
        SVRPUnit svr = new SVRPUnit();
        svr.evaluator = new Error();
        svr.model.buildParameters();
        return svr;
    }

    public static ProcessingUnit build(Algorithm algorithm, Evaluator evaluator, Controller controller, Parameters params) {
        SVRPUnit svr = new SVRPUnit();
//        buildParameters
        svr.evaluator = evaluator;
        svr.algorithm = algorithm;
        svr.control = controller;
        svr.params = params;
        return svr;
    }

    @Override
    public ProcessingUnit fit(Data X, Data Y) {
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
    public ProcessingUnit fit() {
        return null;
    }

    @Override
    public Model getBestModel() {
        return model;
    }

    @Override
    public void saveAs(Path models) {

    }
}
