package pcell.types.svr;

import pcell.evaluator.MaeEvaluator;
import pcell.model.Model;
import pcell.model.SVR;
import pcell.types.ProcessingCell;
import utils.Data;

public class SVRPCell extends ProcessingCell {

    SVR model;

    private SVRPCell(){
        model = new SVR();
    }

    public static SVRPCell buildBasicSVR(){
        SVRPCell svr = new SVRPCell();
        svr.evaluator = new MaeEvaluator();
        svr.model.buildParameters();
        return svr;
    }

    @Override
    public ProcessingCell fit(Data X, Data Y) {
        evaluator.prepareData(X,Y);
        model.buildProblem(X,Y);
        model.fit();
        double evaluate = evaluator.evaluate(model);
        System.out.println("fitness: "+evaluate);

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
