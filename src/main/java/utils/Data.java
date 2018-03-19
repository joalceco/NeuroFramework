package utils;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.jnlp.IntegrationService;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Data {

    HashBiMap<String,Integer> headerMap;
    HashBiMap<String,Integer> indexMap;
    DoubleMatrix2D data;

    public Data(Map<String,Integer> headerMap, Map<String,Integer> indexMap,DoubleMatrix2D data) {
        this.headerMap = HashBiMap.create(headerMap);
        this.indexMap = HashBiMap.create(indexMap);
        this.data = data;
    }

    public Data(String autoLabel, DoubleMatrix2D data) {
        headerMap = HashBiMap.create();
        for (int column = 0; column < data.columns(); column++) {
            headerMap.put(autoLabel + "_" + column,column);
        }
        this.data = data;
    }

    public static Data sumData(Map<String, Data> results) {
        // TODO: 22/11/17 There has to be a better way!!!
        // https://i.imgur.com/GT7yitp.gif
        int rows = results.values().iterator().next().data.rows();
        int columns = results.values().iterator().next().data.columns();
        DoubleMatrix2D yHat = new DenseDoubleMatrix2D(rows, columns);
        results.values().forEach(yFromCell -> yHat.assign(yFromCell.getRawMatrix(), (a, b) -> a + b));
        Data yHatData = new Data("yHat", yHat);
        return yHatData;
    }

    public int nColumns() {
        return data.columns();
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
        return data.get(row, column);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public void toCsv(String s) {
        try {
            FileWriter fileWriter = new FileWriter(s, false);
            CSVPrinter printer = CSVFormat.RFC4180.print(fileWriter);
            BiMap<Integer, String> inverse = headerMap.inverse();
            printer.print("");
            for (int i = 0; i < inverse.size(); i++) {
                printer.print(inverse.get(i));
            }
            printer.println();
            ImmutableList<double[]> il = ImmutableList.copyOf(data.toArray());
            for (int i = 0; i < il.size(); i++) {
                List<Double> list = Doubles.asList(il.get(i));
                printer.print(i);
                printer.printRecord(list);
            }
            printer.close();
        }catch (IOException e){
            System.err.println(e);
        }
    }

    public void toCsv(Path outputFile) {
        toCsv(outputFile.toString());
    }

    public Data applyMask(LinkedHashSet<Integer> mask) {
//        BiMap<Integer, String> headerMapI = headerMap.inverse();
        BiMap<Integer, String> indexMapI = indexMap.inverse();
        HashMap<Integer, String> index = new HashMap<>();
        for (Integer i: mask) {
            index.put(i,indexMapI.get(i));
        }
        BiMap<String, Integer> indexR = HashBiMap.create(index).inverse();
        int[] primitiveMask = ArrayUtils.toPrimitive(mask);
        DoubleMatrix2D d = data.viewSelection(primitiveMask, null);
        return new Data(headerMap,indexR,d);
    }
}
