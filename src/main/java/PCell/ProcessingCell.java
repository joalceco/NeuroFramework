package PCell;

import PCell.ANN.ANN;
import PCell.ANN.ANNFactory;
import PCell.ANN.GuavaANN;
import PCell.Algorithm.Algorithm;
import PCell.Algorithm.Base;
import PCell.Algorithm.Operators.Differential;
import PCell.ParameterController.Controller;
import PCell.ParameterController.StaticController;
import utils.Data;

public class ProcessingCell {

    public Population<ANN> population;
    public Algorithm<ANN> algorithm;
    public Controller control;

    private ProcessingCell(){

    }

    public static ProcessingCell buildBasicPCell(){
        ProcessingCell pCell = new ProcessingCell();
        pCell.algorithm = new Base<>();
        pCell.algorithm = new Differential<>(pCell.algorithm);
        pCell.control = new StaticController();
        return pCell;
    }



//    public static ProcessingCell buildPCell(int numberInputs, int numberOutputs){
//        ProcessingCell pCell = new ProcessingCell();
//
//        return new ProcessingCell();
//    }


    public ProcessingCell fit(Data X, Data Y){
        // TODO: 8/11/17 Agregar validaciones
        buildPopulation(X,Y);
        System.out.println(population.toString());
        while (control.live()){
            algorithm.apply(population);
            control.feed(population);
        }
        return this;
    }

    private void buildPopulation(Data x, Data y) {
        ANNFactory factory = ANNFactory.buildANNFactory(x, y,control);
        population = Population.emptyPopulation();
        int population_size = control.intParameter("population_size");
        for (int i = 0; i < population_size; i++) {
            population.add(factory.buildANN());
        }
    }

}
