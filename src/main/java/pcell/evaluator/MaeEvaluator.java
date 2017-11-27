package pcell.evaluator;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import pcell.model.ANN;
import pcell.model.Model;
import utils.Data;
import utils.Global;

public class MaeEvaluator extends Evaluator{

    @Override
    public double evaluate(Model model) {
        Global.evaluations++;
        Data y_predicted = model.epoch(X);
        return computeError(Y,y_predicted);
    }

    @Override
    public double evaluate(Model model,Data X,Data Y) {
        Global.evaluations++;
        Data y_predicted = model.epoch(X);
        return computeError(Y,y_predicted);
    }

    public static double computeError(Data y_real, Data y_predicted){
        DoubleMatrix2D y_real_data = y_real.getRawMatrix();
        DoubleMatrix2D y_predicted_data = y_predicted.getRawMatrix();
        return mae(y_real_data,y_predicted_data);
    }

    public static double mae(DoubleMatrix2D y_real_data,DoubleMatrix2D y_predicted_data){
        y_real_data=y_real_data.copy();
        y_real_data.assign(y_predicted_data, (x, y) -> Math.abs(x-y));
        return y_real_data.zSum() / y_real_data.size();
    }

}
