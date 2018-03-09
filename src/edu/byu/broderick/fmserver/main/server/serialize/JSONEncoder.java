package edu.byu.broderick.fmserver.main.server.serialize;

import com.google.gson.*;
import edu.byu.broderick.fmserver.main.server.result.EventResult;
import edu.byu.broderick.fmserver.main.server.result.PersonResult;

import java.lang.reflect.Type;

/**
 * Class for encoding and decoding JSON objects
 *
 * @author Broderick Gardner
 */
public enum JSONEncoder implements ISerialEncoder{
    inst();

    Gson gson;

    /**
     * Constructor
     */
    JSONEncoder() {
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        gsonBuilder.registerTypeAdapter(PersonResult.class, personDecoder);
        gsonBuilder.registerTypeAdapter(EventResult.class, eventDecoder);
        gson = gsonBuilder.create();
    }

    //Custom deserializer for person, detects whether the JSON is a single person or multiple
    private JsonDeserializer<PersonResult> personDecoder = (json, typeOfT, context) -> {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement dataField = jsonObject.get("data");

        PersonResult result;

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
     * Serialize the object to a string
     *
     * @param data Object with type among the data model classes
     * @return Returns the serialized string
     */
    @Override
    public String serialize(Object data) {
        return gson.toJson(data);
    }

    /**
     * Deserialize the data back to an object
     *
     * @param str String data to be converted to an object
     * @param type Model object type that will be returned
     * @return Object is of class 'type'
     */
    @Override
    public Object deserialize(String str, Type type) {
        Object obj = null;
        try {
            obj = gson.fromJson(str, type);
        } catch (JsonSyntaxException e) {
            System.out.print("JSON error: failed to convert to Object: ");
            System.out.println(type);
            e.printStackTrace();
        }
        return obj;
    }
}
