package utils;


import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;

public class Log {
    private int id;
    private int generation;
    private int epoch;
    private double trainError;
    private double testError;
    private int neurons;
    private String dot;
    private int index;

    public Log(int id, int generation, int epoch,  double trainError, double testError, int index, int neurons, String dot) {
        this.id = id;
        this.generation = generation;
        this.epoch = epoch;
        this.testError = testError;
        this.trainError = trainError;
        this.index = index;
        this.neurons = neurons;
        this.dot = dot;
    }

    public static void csvHeadertoPrinter(CSVPrinter printer) throws IOException {
        printer.print("id");
        printer.print("generation");
        printer.print("epoch");
        printer.print("train_error");
        printer.print("test_error");
        printer.print("sol_index");
        printer.print("neurons");
        printer.print("dot");
        printer.println();
    }

    private String asCsvRow() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(",");
        sb.append(generation);
        sb.append(",");
        sb.append(epoch);
        sb.append(",");
        sb.append(trainError);
        sb.append(",");
        sb.append(testError);
        sb.append(",");
        sb.append(index);
        sb.append(",");
        sb.append(neurons);
        sb.append(",");
        sb.append(dot);
        return sb.toString();
    }

    @Override
    public String toString() {
        return asCsvRow();
    }

    public void csvToPrinter(CSVPrinter printer) throws IOException {
        printer.print(id);
        printer.print(generation);
        printer.print(epoch);
        printer.print(trainError);
        printer.print(testError);
        printer.print(index);
        printer.print(neurons);
        printer.print(dot);
        printer.println();
    }
}