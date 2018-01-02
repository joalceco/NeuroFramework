package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parameters {
    HashMap<String, Object> params;

    public Parameters(Map<String, Object> params) {
        this.params = new HashMap<>();
        this.params.putAll(params);
    }

    public Parameters() {
        params = new HashMap<>();
    }

    public static Parameters initializeParameters() {
        Parameters params = new Parameters();
        params.setParam("generation", 0);
        params.setParam("epoch", 0);
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public void setParam(String key, Object obj) {
        params.put(key, obj);
    }

    public double getDouble(String key) {
        return (Double) getValue(key);
    }

    public ArrayList<String> getArrayListString(String key) {
        if (containsKey(key))
            return (ArrayList<String>) getValue(key);
        else
            return null;
    }

    public void increment(String key) {
        setParam(key, getInt(key) + 1);
    }

    public int getInt(String key) {
        Number n = (Number) getValue(key);
        return n.intValue();
    }

    public String getString(String key) {
        return (String) getValue(key);
    }

    private Object getValue(String key) {
        if (params.containsKey(key)) {
            return params.get(key);
        } else if (G.params.containsKey(key)) {
            return G.params.get(key);
        }
        return null;
    }

    public boolean containsKey(String key) {
        return params.containsKey(key);
    }

    public Parameters copy() {
        Gson gson = new Gson();
        Parameters copy = new Parameters();
        copy.setParams(gson.fromJson(gson.toJson(params), new TypeToken<HashMap<String, Object>>() {
        }.getType()));
        return copy;
    }

    @Override
    public String toString() {
        return params.toString();
    }
}
