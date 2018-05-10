package utils;

import java.util.Collection;

public class ArrayUtils {


    public static int[] toPrimitive(Collection<Integer> original) {
        int[] primitive = new int[original.size()];
        Integer[] array = new Integer[original.size()];
        original.toArray(array);
        for (int i = 0; i < array.length; i++) {
            primitive[i] = array[i];
        }
        return primitive;
    }
}
