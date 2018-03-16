package utils;

public class Functions {

    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public static double relu(double x) {
        return Math.log(1+Math.exp(x));
    }

    public static double linear(double x) {
        return x;
    }

    public static double tanh(double x) {
        return (1-Math.exp(-2*x)) / (1+Math.exp(-2*x));
    }



}
