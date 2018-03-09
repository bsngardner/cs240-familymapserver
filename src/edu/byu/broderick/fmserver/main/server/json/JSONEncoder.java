package edu.byu.broderick.fmserver.main.server.json;

import com.google.gson.*;
import edu.byu.broderick.fmserver.main.server.result.EventResult;
import edu.byu.broderick.fmserver.main.server.result.PersonResult;

import java.lang.reflect.Type;

/**
 * Class for encoding and decoding JSON objects
 *
 * @author Broderick Gardner
 */
public class JSONEncoder {


    /**
     * Singleton instance of JSONEncoder
     */
    public static JSONEncoder encoder = new JSONEncoder();
    Gson gson;

    //Custom deserializer for person, detects whether the JSON is a single person or multiple
    private JsonDeserializer<PersonResult> personDecoder = (json, typeOfT, context) -> {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement dataField = jsonObject.get("data");

        PersonResult result = null;

        if (dataField != null) {
            result = context.deserialize(json, PersonResult.AllPersons.class);
        } else {
            result = context.deserialize(json, PersonResult.SinglePerson.class);
        }

        return result;
    };

    //Custom deserializer for event, detects whether the JSON is a single event or multiple
    private JsonDeserializer<EventResult> eventDecoder = (json, typeOfT, context) -> {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement dataField = jsonObject.get("data");

        EventResult result;

        if (dataField != null) {
            result = context.deserialize(json, EventResult.AllEvents.class);
        } else {
            result = context.deserialize(json, EventResult.SingleEvent.class);
        }

        return result;
    };

    /**
     * Constructor
     */
    public JSONEncoder() {
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        gsonBuilder.registerTypeAdapter(PersonResult.class, personDecoder);
        gsonBuilder.registerTypeAdapter(EventResult.class, eventDecoder);
        gson = gsonBuilder.create();
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
