package pcell.evaluator;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import pcell.model.Model;
import utils.Data;
import utils.G;

public class Error extends Evaluator {

    public Error() {
//        this.cell = cell;
    }

    public static double computeError(Data y_real, Data y_predicted) {
        return computeError(y_real, y_predicted,G.getStringParam("error"));
    }

    public static double computeError(Data y_real, Data y_predicted, String error) {
        DoubleMatrix2D y_real_data = y_real.getRawMatrix();
        DoubleMatrix2D y_predicted_data = y_predicted.getRawMatrix();
        switch (error.toLowerCase()) {
            case "mae":
                return mae(y_real_data, y_predicted_data);
            case "m3e":
                return m3e(y_real_data, y_predicted_data);
            case "mse100":
                return m3e(y_real_data, y_predicted_data);
            case "r2":
                return r2(y_real_data, y_predicted_data);
            case "mse":
            default:
                return mse(y_real_data, y_predicted_data);
        }
    }

    public static double mae(DoubleMatrix2D y_real_data, DoubleMatrix2D y_predicted_data) {
        y_real_data = y_real_data.copy();
        y_real_data.assign(y_predicted_data, (x, y) -> Math.abs(x - y));
        return y_real_data.zSum() / y_real_data.size();
    }

    public static double mse(DoubleMatrix2D y_real_data, DoubleMatrix2D y_predicted_data) {
        y_real_data = y_real_data.copy();
        y_real_data.assign(y_predicted_data, (x, y) -> Math.pow(x - y, 2));
        return y_real_data.zSum() / y_real_data.size();
    }

    public static double mse100(DoubleMatrix2D y_real_data, DoubleMatrix2D y_predicted_data) {
        y_real_data = y_real_data.copy();
        y_real_data.assign(y_predicted_data, (x, y) -> Math.pow(x*100 - y*100, 2));
        return y_real_data.zSum() / y_real_data.size();
    }

    public static double m3e(DoubleMatrix2D y_real_data, DoubleMatrix2D y_predicted_data) {
        y_real_data = y_real_data.copy();
        y_real_data.assign(y_predicted_data, (x, y) -> Math.abs(Math.pow(x - y, 3)));
        return y_real_data.zSum() / y_real_data.size();
    }

    public static double r2(DoubleMatrix2D y_real_data, DoubleMatrix2D y_predicted_data) {
        //SSres
        DoubleMatrix2D SSres = y_real_data.copy();
        SSres.assign(y_predicted_data, (x, y) -> Math.pow(x - y, 2));
        //SStot
        double mean = y_real_data.zSum() / y_real_data.size();
        DoubleMatrix2D SStot = y_real_data.copy();
        DoubleMatrix2D y2 = SStot.like().assign(mean);
        SStot.assign(y2, (x, y) -> Math.pow(x - y, 2));

        return (SSres.zSum()-SStot.zSum());
    }

    @Override
    public double evaluate(Model model) {
//        G.evaluations++;
        Data y_predicted = model.epoch(X);
        double error = computeError(Y, y_predicted, G.getStringParam("error"));
        return error;
    }

    @Override
    public double evaluate(Model model, Data X, Data Y) {
//        G.evaluations++;
        Data y_predicted = model.epoch(X);
        return computeError(Y, y_predicted,G.getStringParam("error"));
    }

    @Override
    public void prepareNextBatch() {

    }

}
