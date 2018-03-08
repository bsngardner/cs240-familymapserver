package edu.byu.broderick.fmserver.test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;

public class Testing {

    public static void main(String[] args){
        final int NUM_BITS = 16384;
        BigInteger key = new BigInteger(NUM_BITS, new SecureRandom());
        String keyString = new String(Base64.getEncoder().encode(key.toByteArray()));
        BigInteger decoded = new BigInteger(Base64.getDecoder().decode(keyString));
        System.out.println(key);
        System.out.println(keyString);
        System.out.println(decoded);

    }
}
