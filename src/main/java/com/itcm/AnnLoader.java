
package com.itcm;

import org.apache.commons.cli.CommandLine;
import pcell.model.GuavaANN;
import utils.Data;
import utils.ProblemReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AnnLoader {


    public static void main(String[] args) throws IOException {
//        Path model = Paths.get("C:\\Users\\joalc\\Dropbox\\Doctorado\\paper2018\\results_30_sumar456null\\models\\2\\genetic.ann");
        CommandLine cmd = Main.parser(args);
        Path model = Paths.get(cmd.getOptionValue("model"));
//        Path model = Paths.get(args[0]);
        GuavaANN ann = GuavaANN.buildANN(model);
//        Data x = ProblemReader.read_csv(Paths.get("C:\\Users\\joalc\\Dropbox\\Doctorado\\paper2018\\results_30_sumar456null\\dataset\\2\\testX.csv"),true);
        String in = cmd.getOptionValue("input_path");
        if (in == null) {
            System.err.println("Argument input_path not set");
            System.exit(1);
        }
        String out = cmd.getOptionValue("output_path");
        if (out == null) {
            System.err.println("Argument output_path not set");
            System.exit(1);
        }
        Data x = ProblemReader.read_csv(in, true);
        Data epoch = ann.epoch(x);
        epoch.toCsv(Paths.get(out));
//        System.out.println(epoch);
    }


}
