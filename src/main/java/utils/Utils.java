package utils;

import java.text.DecimalFormat;

public class Utils {

    static DecimalFormat df = new DecimalFormat("#.00");

    public static String formatDouble(double num){
        return df.format(num);
    }
}
