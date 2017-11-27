package utils;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;

import java.util.HashMap;
import java.util.Map;

public class Data{

    Map<String, Integer> headerMap;
    DoubleMatrix2D data;

    public int nColumns(){
        return data.columns();
    }

    public Data(Map<String, Integer> headerMap, DoubleMatrix2D data) {
        this.headerMap = headerMap;
        this.data = data;
    }

    public Data(String autoLabel, DoubleMatrix2D data) {
        Map<String, Integer> headerMap = new HashMap<>();
        for (int column=0;column<data.columns();column++) {
            headerMap.put(autoLabel+"_"+column,column);
        }
        this.headerMap = headerMap;

        this.data = data;
    }

    public DoubleMatrix2D getRawMatrix() {
        return data;
    }

    public int nRows() {
        return data.rows();
    }

    public double[] getRawOutput() {
        return data.viewColumn(0).toArray();
    }

    public double getRawData(int row, int column) {
        return data.get(row,column);
    }

    public static Data sumData(Map<String, Data> results) {
        // TODO: 22/11/17 There has to be a better way!!!
        // https://i.imgur.com/GT7yitp.gif
        int rows = results.values().iterator().next().data.rows();
        int columns = results.values().iterator().next().data.columns();
        DoubleMatrix2D yHat = new DenseDoubleMatrix2D(rows,columns);
        results.values().forEach(yFromCell -> yHat.assign(yFromCell.getRawMatrix(),(a, b) -> a+b));
        Data yHatData = new Data("yHat", yHat);
        return yHatData;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    //    public Data dropColumn(String header){
//        int column = headerMap.get(header);
//        DoubleMatrix1D matrix1D = data.viewColumn(column).copy();
//        data.viewSelection(null, headerMap.)
//        DoubleMatrix2D data = matrix1D.like2D((int) matrix1D.size(), 1);
//        data.viewSelection(null, )
//        new Data()
//    }


}
