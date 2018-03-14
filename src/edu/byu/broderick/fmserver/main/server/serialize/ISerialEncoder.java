package edu.byu.broderick.fmserver.main.server.serialize;

import java.lang.reflect.Type;

/**
 * Interface for defining the bidirectional interface between data model objects and arbitrary string formats
 *
 * @author Broderick Gardner
 */
public interface ISerialEncoder {

    /**
     * Serialize the object to a string
     *
     * @param data Object with type among the data model classes
     * @return Returns the serialized string
     */
    public String serialize(Object data);

    /**
     * Deserialize the data back to an object
     *
     * @param str String data to be converted to an object
     * @param type Model object type that will be returned
     * @return Object is of class 'type'
     */
    public Object deserialize(String str, Type type);

}
