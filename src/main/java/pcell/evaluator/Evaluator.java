package pcell.evaluator;

import pcell.model.Model;
import utils.Data;

public abstract class Evaluator {

    Data X, Y;
//    ProcessingCell cell;

    public void prepareData(Data x, Data y) {
        this.X = x;
        this.Y = y;
    }

    public abstract double evaluate(Model model);

    public abstract double evaluate(Model model, Data X, Data Y);

//    public static Evaluator getErrorType(String errorType) {
//        switch (errorType.toLowerCase()) {
//            case "mae":
//            default:
//                return new Error();
//            case "rmse":
//                return new RMSError();
//        }
//    }
}
