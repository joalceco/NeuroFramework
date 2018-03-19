package pcell.evaluator;

import pcell.model.Model;
import utils.Data;
import utils.G;
import utils.Utils;

import java.util.LinkedHashSet;

public class ErrorBatch extends Error {

    Data tempX, tempY;
    double batchPercent;


    public ErrorBatch() {
        batchPercent = G.paramD("batch_percent");
    }

    public void prepareData(Data x, Data y) {
        this.X = x;
        this.Y = y;
        prepareNextBatch();
    }

    @Override
    public double evaluate(Model model) {
//        G.evaluations++;
        Data y_predicted = model.epoch(tempX);
        return computeError(tempY, y_predicted, G.getStringParam("error"));
    }

    @Override
    public void prepareNextBatch() {
        int sampleSize = Utils.cutSize(X.nRows(), batchPercent);
        LinkedHashSet<Integer> mask = G.r.sample(sampleSize, X.nRows());
        tempX = X.applyMask(mask);
        tempY = Y.applyMask(mask);
    }

}
