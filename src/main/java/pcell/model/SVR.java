package pcell.model;

import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import libsvm.*;
import utils.Data;

import java.util.LinkedList;

public class SVR extends Model{

    svm_parameter param;
    svm_problem problem;
    svm_model model;


    public void buildParameters(){
        param = new svm_parameter();
        param = new svm_parameter();
        // default values
        param.svm_type = svm_parameter.NU_SVR;
        param.kernel_type = svm_parameter.RBF;
        param.degree = 3;
        param.gamma = 0.2;	// 1/num_features
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 100;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];
    }

    public void buildProblem(Data X, Data Y){
        problem = new svm_problem();
        problem.l=X.nRows();
        problem.y = Y.getRawOutput();
        problem.x = new svm_node[X.nRows()][X.nColumns()];
        for (int row = 0; row < problem.l; row++) {
            for (int column = 0; column < X.nColumns(); column++) {
                double value = X.getRawData(row,column);
//                if(value != 0.0){
                    svm_node node = new svm_node();
                    node.index = column+1;
                    node.value = value;
                    problem.x[row][column] = node;
//                }
            }
        }
    }

    public void fit(){
        model = svm.svm_train(problem, param);
    }

    @Override
    public Data epoch(Data x) {
        LinkedList<Double> results=new LinkedList<>();
        for (int i = 0; i < x.nRows(); i++) {
            svm_node row[] = new svm_node[x.nColumns()];
            for (int j = 0; j < x.nColumns(); j++) {
                row[j] = new svm_node();
                row[j].index = j+1;
                row[j].value = x.getRawData(i,j);
            }
            double v = svm.svm_predict(model, row);
            results.addLast(v);
        }
        double y[][]= new double[results.size()][1];
        for (int i = 0; i < results.size(); i++) {
            y[i][0] = results.get(i);
        }
        DenseDoubleMatrix2D y2 = new DenseDoubleMatrix2D(y);
        Data salida = new Data("y",y2);

        return salida;
    }
}
