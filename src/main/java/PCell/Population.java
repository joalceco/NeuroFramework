/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCell;


import PCell.ANN.ANN;
import PCell.ANN.ANNFactory;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joceco
 */
public class Population<T extends ANN> extends LinkedList<T> {

    boolean isStagnated = false;
    protected T topology;
//    int size=100;

    private Population(){

    }

    public static Population emptyPopulation(){
        return new Population();
    }

//    private Population buildPopulation(){
//        for (int i = 0; i < size; i++) {
//            add((T) ANNFactory.buildANN(Global.getANNType()));
//        }
//        return this;
//    }
//
//    private Population buildPopulation(){
//        return this;
//    }
//
//    private Population(Regression problem) {
//        f=problem;
//        buildPopulation(problem);
//    }
//
//    private Population() {
//        buildPopulation();
//    }
//
//    public static Population makePopulation(){
//        return new Population<>(G.problem);
//    }

//    public Population(int modelsize, SolutionConstructor<T> constructor, int size) {
//        this.modelsize = modelsize;
//        this.size = size;
//    }
//
//    public Population(Regression ev, SolutionConstructor<T> constructor, int size) {
//        for (int i = 0; i < size; i++) {
//            add(constructor.buildOne(ev));
//        }
//    }
//
//    public Population(T template, SolutionConstructor<T> constructor, int size) {
//        for (int i = 0; i < size; i++) {
//            add(constructor.buildOneFromTemplate(template));
//        }
//    }


    public T getBestModel() {
        return this.get(0);
    }

    public List<T> getPop() {
        return this;
    }



    public void setTopology(T topology) {
        this.topology = topology;
    }

    public T getTopology() {
        return topology;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder= new StringBuilder();
        for (T a:
             this) {
            stringBuilder.append(a.toString());
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }


    public T replaceSolution(int i, T solution) {
        return this.set(i, solution);
    }
}
