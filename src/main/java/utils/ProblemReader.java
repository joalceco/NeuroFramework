package utils;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.encog.util.csv.CSVFormat;

/**
 * @author joceco
 */
public class ProblemReader {

    public static Data read_csv(Path csvPath, boolean index_column) throws IOException {
        return read_csv(csvPath.toString(),index_column);
    }

    public static Data read_csv(String csvPath, boolean index_column) throws IOException {
        Reader in = new FileReader(csvPath);
        List<CSVRecord> records = null;
        Map<String, Integer> headerMap = null;
        CSVParser parser = null;
        parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        records = parser.getRecords();
        headerMap = parser.getHeaderMap();
        if(index_column){
            headerMap.remove("");
        }
        HashMap<String,Integer> indexMap = new HashMap<>();
        int shift = index_column? 1:0;
        DoubleMatrix2D data = new DenseDoubleMatrix2D(records.size(), headerMap.size());
        for (int i = 0; i < data.rows(); i++) {
            indexMap.put(index_column?records.get(i).get(0):""+i,i);
            for (int j = 0; j < data.columns(); j++) {
                data.setQuick(i, j, Double.parseDouble(records.get(i).get(j + shift)));
            }
        }
        return new Data(headerMap,indexMap, data);
    }

    public static Data getX(String csvPath) throws IOException {
        if (csvPath == null) {
            csvPath = G.getStringParam("trainingXPath");
        } else if (csvPath == "") {
            csvPath = G.getStringParam("trainingXPath");
        }
        return read_csv(csvPath, true);
    }

    public static Data getY(String csvPath) throws IOException {
        if (csvPath == null) {
            csvPath = G.getStringParam("trainingYPath");
        } else if (csvPath == "") {
            csvPath = G.getStringParam("trainingYPath");
        }
        return read_csv(csvPath, true);
    }

    public DoubleMatrix2D fromCSVtoDoubleMatrix2D() throws IOException {
//        if (!G.params.containsKey("testPath")) {
//            throw new FileNotFoundException("trainingPath param set as null");
//        }
        Reader in = new FileReader(G.getStringParam("testPath").toString());
        List<CSVRecord> records = null;
        Map<String, Integer> headerMap = null;
        CSVParser parser = null;
        parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        records = parser.getRecords();
        headerMap = parser.getHeaderMap();
        int nRecords = records.size();
        int nColumns = headerMap.size() - 1;
        DoubleMatrix2D features = new DenseDoubleMatrix2D(nRecords, nColumns);
        for (int i = 0; i < nRecords; i++) {
            for (int j = 0; j < nColumns; j++) {
                features.setQuick(i, j, Double.parseDouble(records.get(i).get(j + 1)));
            }
        }
        return features;
    }

    public void fromOutputToCSV(DoubleMatrix1D matrix1D) throws IOException {
        if (!G.containsKey("outputPath")) {
            throw new FileNotFoundException("trainingPath param set as null");
        }
        File path = new File(G.getStringParam("outputPath").toString());
        File file = new File(path, "output.csv");
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        CSVPrinter printer = null;
        printer = CSVFormat.RFC4180.print(fileWriter);
        String[][] data = new String[(int) matrix1D.size()][1];
        for (int i = 0; i < matrix1D.size(); i++) {
            data[i][0] = String.valueOf(matrix1D.get(i));
        }
        printer.print("predicted");
        printer.println();
        printer.printRecords(data);
        printer.close();
    }
}
