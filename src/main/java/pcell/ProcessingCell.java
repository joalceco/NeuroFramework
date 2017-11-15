package pcell;

import pcell.ann.ANN;
import pcell.ann.ANNFactory;
import pcell.algorithm.Algorithm;
import pcell.algorithm.Base;
import pcell.algorithm.operators.Differential;
import pcell.controller.Controller;
import pcell.controller.StaticController;
import pcell.evaluator.Evaluator;
import pcell.evaluator.MaeEvaluator;
import utils.Data;

public class ProcessingCell {

    public Population<ANN> population;
    public Algorithm<ANN> algorithm;
    public Controller control;
    public Evaluator evaluator;

    private ProcessingCell(){

    }

    public static ProcessingCell buildBasicPCell(){
        ProcessingCell pCell = new ProcessingCell();
        pCell.evaluator = new MaeEvaluator();
        pCell.algorithm = new Base<>(pCell.evaluator);
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
        evaluator.prepareData(X,Y);
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
