package com.itcm;

import pcell.ProcessingCell;
import utils.Data;
import utils.ProblemReader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        //DAme datos de x
        Data x = ProblemReader.getX("");
        //Dame datos de y
        Data y = ProblemReader.getY("");
        //Dame un ProcessingCell
        ProcessingCell model = ProcessingCell.buildBasicPCell().fit(x,y);
        //ajusta el ProcessingCell con X y Y
        //dame datos de y prueba
        //testea con y prueba
    }
}
