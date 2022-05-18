package org.marehman.rideSharing.utils;

import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class CommonHelper {

    public static String getStringValue(Object object) {
        return object != null ? object.toString() : "";
    }

    public static long getLongValue(Object object) {
        try {
            return (long) object;
        }catch (Exception e){
            return 0L;
        }
    }
    public static int getIntegerValue(Object object) {
        try {
            return (int) object;
        }catch (Exception e){
            return 0;
        }
    }

    public static ResponseEntity<?> buildResponseEntity(Response response){
        return new ResponseEntity<Object>( response, response.getStatus());
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String getMD5Encrypted(String value) {
        try {
            return byteToHex(MessageDigest.getInstance("MD5").digest(value.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }
}
