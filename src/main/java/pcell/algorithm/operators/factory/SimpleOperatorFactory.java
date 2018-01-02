package pcell.algorithm.operators.factory;

import pcell.algorithm.Algorithm;
import pcell.algorithm.Base;
import pcell.algorithm.operators.Differential;
import pcell.algorithm.operators.Genetic;
import pcell.algorithm.operators.RandomMutation;
import pcell.model.ANN;
import pcell.types.ProcessingCell;
import utils.Parameters;

import java.util.ArrayList;

public class SimpleOperatorFactory<T extends ANN> {

    public Algorithm<T> buildAlgorithm(ArrayList<String> operatorNames, ProcessingCell cell) {
        if (operatorNames == null) {
            return null;
        }
        Algorithm base = buildBase(cell);
        for (String operator : operatorNames) {
            base = appendOperator(base, operator, cell.params);
        }
        return base;
    }

    public Algorithm<T> buildBase(ProcessingCell cell) {
//        Evaluator evaluator = Evaluator.getErrorType(cell.params.getString("error_type"));
        Algorithm<T> algorithm = new Base(cell);
        return algorithm;
    }

    public Algorithm<T> appendOperator(Algorithm<T> algorithm, String operatorName, Parameters parameters) {
        switch (operatorName.toLowerCase()) {
            case "genetic":
                return new Genetic<T>(algorithm, parameters);
            case "differential":
                return new Differential<T>(algorithm, parameters);
            case "random_mutation":
                return new RandomMutation<T>(algorithm, parameters);
        }
        return null;
    }
}
