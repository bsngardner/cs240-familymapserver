package edu.byu.broderick.fmserver.main.server.serialize;

/**
 * Globally accessible reference to serializer/deserializer.  Can be any instance of ISerialEncoder
 *
 * @author Broderick Gardner
 */
public class SerialCodec {
    public static final ISerialEncoder inst = JSONEncoder.inst;
}
