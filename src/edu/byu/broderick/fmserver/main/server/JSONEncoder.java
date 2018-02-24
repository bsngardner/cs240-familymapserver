package edu.byu.broderick.fmserver.main.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Class for encoding and decoding JSON objects
 *
 * @author Broderick Gardner
 */
public class JSONEncoder {

    public static JSONEncoder encoder = new JSONEncoder();
    Gson gson;

    /**
     * Constructor
     */
    public JSONEncoder() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public String convertToJSON(Object obj) {
        return gson.toJson(obj);
    }

    public Object convertToObject(String json, Type type) {
        Object obj = null;
        try {
            obj = gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            System.out.print("JSON error: failed to convert to Object: ");
            System.out.println(type);
            e.printStackTrace();
        }
        return obj;
    }

}
