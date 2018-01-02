package utils;

public class Functions {

    public static double sigmoid(double x) {
        double y;
        if (x < -10)
            y = 0;
        else if (x > 10)
            y = 1;
        else
            y = 1 / (1 + Math.exp(-x));
        return y;
    }

}
