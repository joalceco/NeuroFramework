package com.itcm;

import pcell.model.Model;
import pcell.types.svr.SVRPCell;
import utils.Data;
import utils.ProblemReader;

import java.io.IOException;

public class SVRRunner {

    public static void main(String[] args) throws IOException {
        //DAme datos de x
        Data x = ProblemReader.getX("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/testX.csv");
        //Dame datos de y
        Data y = ProblemReader.getY("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/testY.csv");
        //Dame un BasicNeuroPCell
        SVRPCell svrpCell = SVRPCell.buildBasicSVR();
        svrpCell.fit(x, y);
        Data x_train = ProblemReader.getX("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/trainX.csv");
        Data y_train = ProblemReader.getY("/home/joalceco/Dropbox/Doctorado/Datasets/20171001-ILKatritzky2002b/3-Uploaded Data/20171010/trainY.csv");

        Model svr = svrpCell.getBestModel();
        double error = svrpCell.evaluator.evaluate(svr, x_train, y_train);
        System.out.println(error);


    }
}
