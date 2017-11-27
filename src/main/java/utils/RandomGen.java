package utils;

import cern.colt.list.tint.IntArrayList;
import cern.jet.random.tdouble.engine.DoubleMersenneTwister;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by joalceco on 9/06/17.
 */
public class RandomGen extends DoubleMersenneTwister {

    public RandomGen(int seed) {
        super(seed);
    }

    /**
     * Returns a random integer number between min(inclusive) and max(exclusive).
     *
     * @param min Lower limit of random number(inclusive)
     * @param max Upper limit of random number(exclusive)
     * @return int The random number.
     */
    public int nextInt(int min, int max) {
        return (int) (min + (max - min) * super.nextDouble());
    }

    public int nextInt(int max) {
        return (int) ((max) * super.nextDouble());
    }

    /**
     * Returns a random sample number between min(inclusive) and max(exclusive) of size sampleSize.
     *
     * @param sampleSize size of the sample
     * @param min Lower limit of random number(inclusive)
     * @param max Upper limit of random number(exclusive)
     * @return  LinkedHashSet<Integer> The random sample.
     */
    public LinkedHashSet<Integer> sample(int sampleSize, int min, int max) {
        if (max - min < sampleSize) {
            try {
                throw new Exception("SampleSize must be lower than max-min");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LinkedHashSet<Integer> set = new LinkedHashSet<>(sampleSize);
        while (set.size() < sampleSize) {
            set.add(nextInt(min, max));
        }
//        Integer[] y = set.toArray(new Integer[0]);
        return set;

    }

    public boolean nextBoolean() {
        return nextDouble() < 0.5;
    }

    public int selectRandomElement(IntArrayList array) {
        return array.get(nextInt(0, array.size()));
    }

    public int selectRandomElement(List<Integer> array) {
        if (array.size() == 0) return -1;
        if (array == null) return -1;
        return array.get(nextInt(0, array.size()));
    }

    public double nextDouble(double min, double max) {
        return min + nextDouble() * (max - min);
    }

    public double nextWeight() {
        Double min = (Double) Global.paramD("min_weight");
        Double max = (Double) Global.paramD("max_weight");
        return nextDouble(min, max);
    }
}
