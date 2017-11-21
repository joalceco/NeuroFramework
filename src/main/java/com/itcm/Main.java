package com.itcm;

import pcell.ProcessingCell;
import pcell.ann.ANN;
import utils.Data;
import utils.Global;
import utils.ProblemReader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        //DAme datos de x
        Data x = ProblemReader.getX("");
        //Dame datos de y
        Data y = ProblemReader.getY("");
        //Dame un ProcessingCell
        ProcessingCell model = ProcessingCell
                .buildBasicPCell()
                .fit(x,y);
        ANN bestSolution = model.getBestSolution();
        model.historyToCSV("/home/joalceco/Dropbox/Doctorado/Datasets/20171014-CurveSet3D/4-Analysis/historico_ann/history.csv");


        //ajusta el ProcessingCell con X y Y
        //dame datos de y prueba
        //testea con y prueba
    }
}
