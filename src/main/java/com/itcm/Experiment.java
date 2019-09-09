package com.itcm;

import pcell.evaluator.Error;
import utils.G;

import java.io.IOException;

public class Experiment {

    public static void main(String args[]) throws IOException {
        String[] params = new String[0];
        for (int i = 0; i < 30; i++) {
            System.out.println("run " + i );
            G.resetR(i);
            G.evaluations = 0;
            //netbeans test
            new SARunner(i).main(params);
//            new GeneticRunner(i).main(params);
            System.out.println(Error.best);
            System.out.println(Error.worst);
            G.runid++;
//            new GeneticRunner(i).main(params);
        }

    }

}
