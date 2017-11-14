package pcell.ann;


import pcell.controller.Controller;
import utils.Data;
import utils.Global;

public class ANNFactory {

    int nInputs;
    int nOutputs;
    Controller control;

    private ANNFactory(){}

    public ANNFactory(int nInputs, int nOutputs, Controller control) {
        this.nInputs = nInputs;
        this.nOutputs = nOutputs;
        this.control = control;
    }

    public static ANNFactory buildANNFactory(Data x, Data y, Controller control){
        return new ANNFactory(x.nColumns(),y.nColumns(),control);

    }


    public ANN buildANN(){
        ANN ann = null;
        switch (Global.getStringParam("ann_type").toLowerCase()) {
            case "guava_ann":
                ann=GuavaANN.buildANN().buildRandomANN(nInputs,nOutputs,control.intParameter("max_size"));
                break;
        }
//        if(singleton.regression == null){
//            return null;
//        }
//        ann ann= null;
//        switch (annType.toLowerCase()){
//            case "guava_ann":
//                ann = new GuavaANN();
//                ann.buildFromFitnessFunction(singleton.regression);
//                break;
//            case "free_ann":
//                ann = new FreeFormANN(singleton.regression);
//                break;
//        }
//        return ann;

        return ann;
    }


}
