package utils;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import cern.colt.matrix.tdouble.DoubleMatrix2D;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author joceco
 */
public class Global {
    public static final int seed = 35;
    public static int id = 0;
    public static boolean DEBUG = false;
    public static RandomGen r = new RandomGen(3);
    public static int evaluations = 0;
    public static int maxEvaluations = 10000;
    private static Map<String, Object> params;

    public static Map<String, Object> getParams() {
        return params;
    }

    public static String getANNType(){
        return (String)params.get("ann_type");
    }

    public static String getStringParam(String key){
        paramsNotNull();
        if(params.containsKey(key)){
            return (String) params.get(key);
        }
        return "";
    }

    public static Integer getIntegerParam(String key){
        paramsNotNull();
        if(params.containsKey(key)){
            return  ((Double)params.get(key)).intValue();
        }
        return 0;
    }

    public static Double getDoubleParam(String key){
        paramsNotNull();
        if(params.containsKey(key)){
            return (Double) params.get(key);
        }
        return 0.0;
    }

    private static void paramsNotNull(){
        if(params==null){
            try {
                getDefaultsFromFile(new File("defaults.json"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getDefaultsFromFile(File defaultsFile) throws FileNotFoundException {
        params = new HashMap<>();
        FileReader fr = new FileReader(defaultsFile);
        JsonReader reader = new JsonReader(fr);
        Map<String, Object> defaultsM = new Gson().fromJson(reader,
                new TypeToken<HashMap<String, Object>>() {
                }.getType());
        params.putAll(defaultsM);
    }

    public static boolean compare(String key, String value) {
        if (params.containsKey(key)) {
            if (params.get(key).toString().compareToIgnoreCase(value) == 0) {
                return true;
            }
        }
        return false;
    }


    public static boolean containsKey(String key) {
        return params.containsKey(key);
    }



}
