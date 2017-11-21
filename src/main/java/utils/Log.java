package utils;


import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;

public class Log{
    int id;
    int generation;
    int epoch;
    double fitness;
    int neurons;
    String dot;
    int index;

    public Log(int id, int generation, int epoch, double fitness, int index, int neurons, String dot) {
        this.id = id;
        this.generation = generation;
        this.epoch = epoch;
        this.fitness = fitness;
        this.index = index;
        this.neurons= neurons;
        this.dot = dot;
    }

    public static void csvHeadertoPrinter(CSVPrinter printer) throws IOException {
        printer.print("id");
        printer.print("generation");
        printer.print("epoch");
        printer.print("fitness");
        printer.print("sol_index");
        printer.print("neurons");
        printer.print("dot");
        printer.println();
    }

    public String asCsvRow(){
        StringBuilder sb= new StringBuilder();

        sb.append(id);sb.append(",");
        sb.append(generation);sb.append(",");
        sb.append(epoch);sb.append(",");
        sb.append(fitness);sb.append(",");
        sb.append(index);sb.append(",");
        sb.append(neurons);sb.append(",");
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
        printer.print(fitness);
        printer.print(index);
        printer.print(neurons);
        printer.print(dot);
        printer.println();
    }
}