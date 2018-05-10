package com.itcm;

import utils.G;

import java.io.IOException;

public class Experiment {

    public static void main ( String args[]) throws IOException {
        String[] params = new String[0];
        for (int i = 0; i < 30; i++) {
            G.resetR(i);
            G.evaluations=0;
            G.runid++;
            //netbeans test
            new SARunner(i).main(params);
        }


    }



}
