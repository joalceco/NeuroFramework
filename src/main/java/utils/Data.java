package utils;

import cern.colt.matrix.tdouble.DoubleMatrix2D;

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


//    public Data dropColumn(String header){
//        int column = headerMap.get(header);
//        DoubleMatrix1D matrix1D = data.viewColumn(column).copy();
//        data.viewSelection(null, headerMap.)
//        DoubleMatrix2D data = matrix1D.like2D((int) matrix1D.size(), 1);
//        data.viewSelection(null, )
//        new Data()
//    }


}
