package com.itcm;

import org.apache.commons.cli.*;

import java.io.IOException;

public class Main {

    public static CommandLine parser(String args []){
        Options options = new Options();
        Option input;
        input = new Option("id", "run_id", true, "run id");
        options.addOption(input);
        input = new Option("seed", "seed", true, "seed");
        options.addOption(input);
        input = new Option("i", "input_path", true, "Set the input path");
        options.addOption(input);
        input = new Option("o", "output_path", true, "Set the output path");
        options.addOption(input);
        input = new Option("d", "datetime", true, "Set the datetime");
        options.addOption(input);
        input = new Option("e", "epoch", true, "Set the maximum number of epochs");
        options.addOption(input);
        input = new Option("s", "max_size", true, "Set the maximum size of neurons");
        options.addOption(input);
        input = new Option("p", "population_size", true, "Set the maximum size of the populations");
        options.addOption(input);
        input = new Option("a", "algorithm", true, "Not implemented yet");
        options.addOption(input);
        input = new Option("m", "error_metric", true, "error metric used");
        options.addOption(input);
        input = new Option("r", "run_ann", false, "Determines if runs a model");
        options.addOption(input);
        input = new Option("t", "train_ann", false, "Trains an ann and saves in outputpath");
        options.addOption(input);
        input = new Option("model", "model_input", true, "sets the model path");
        options.addOption(input);
//        input.setRequired(true);
        CommandLineParser parser = new DefaultParser();
//        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
//            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
        return cmd;
    }

    public static void main(String[] args) throws IOException {

        CommandLine cmd = parser(args);
        if (cmd.hasOption("run_ann")){
            AnnLoader.main(args);
        }else{
            GeneticRunner.main(args);
        }

//        //DAme datos de x
//        Data x = ProblemReader.getX("");
//        //Dame datos de y
//        Data y = ProblemReader.getY("");
//        //Dame un BasicNeuroPCell
////        double cr=0.6,wm=0.8;
//        for (double cr = 0.6; cr < 0.61; cr += 0.05) {
//            for (double wf = 0.2; wf < 0.21; wf += 0.05) {
//                for (double wm = 0.8; wm < 0.81; wm += 0.05) {
//                    G.evaluations = 0;
//                    G.setParam("crossover_rate", cr);
//                    G.setParam("weigthing_factor", wf);
//                    G.setParam("weight_mutation_p", wm);
//                    ProcessingCell pcell = PCellFactory.buildBasicDifferential();
//                    pcell.fit(x, y);
//                    Model bestSolution = pcell.getBestModel();
//                    pcell.setLogManager(new CsvManager(pcell, Paths.get("/home/joalceco/Dropbox/Doctorado/Datasets/20171014-CurveSet3D/4-Analysis/historico_ann/history")
//                            .resolve(Utils.formatDouble(cr) + "-"
//                            + Utils.formatDouble(wf) + "-"
//                            + Utils.formatDouble(wm) + "-"
//                            + ".csv")));
//
//                    double fitness = bestSolution.getFitness();
//                    System.out.print(cr + "," + wf + "," + wm + ",");
//                    System.out.print(fitness);
//                    Data xTest = ProblemReader.getX("/home/joalceco/Dropbox/Doctorado/Datasets/20171014-CurveSet3D/1-OriginalData/testX.csv");
//                    Data yTest = ProblemReader.getY("/home/joalceco/Dropbox/Doctorado/Datasets/20171014-CurveSet3D/1-OriginalData/testY.csv");
//                    Data yHat = bestSolution.epoch(xTest);
//                    double mae = Error.computeError(yTest, yHat);
//                    System.out.println(", " + mae);
//                }
//            }
//        }
//
    }
}
