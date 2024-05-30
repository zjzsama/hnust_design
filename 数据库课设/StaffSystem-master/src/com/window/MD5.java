package com.window;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class MD5 {

        public static String encrypt(String s){
        if(s == null)
            return "";
        if(s.length() == 0)
        {
            return "";
        } else
        {
            BigInteger biginteger = new BigInteger(s.getBytes());
            BigInteger biginteger1 = new BigInteger("142536");
            System.out.println("亦或1"+biginteger);
            System.out.println("亦或2"+biginteger1);
            BigInteger biginteger2 = biginteger1.xor(biginteger);
            //System.out.println("亦或3"+s);

            return biginteger2.toString(10);
        }
    }


    public static String decrypt(String s){
        if(s == null)
            return "";
        if(s.length() == 0)
            return "";
        BigInteger biginteger = new BigInteger("142536");
        try
        {
            BigInteger biginteger1 = new BigInteger(s, 10);
            BigInteger biginteger2 = biginteger1.xor(biginteger);
            return new String(biginteger2.toByteArray());
        }
        catch(Exception exception)
        {
            return "";
        }
    }
//    public static String encrypt(String s) {
//        if (s == null)
//            return "";
//        if (s.length() == 0)
//            return "";
//
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hashInBytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
//        String hashString = String.valueOf(hashInBytes, StandardCharsets.UTF_8);
//
//        return hashString;
//    }
//
//    public static void storeHash(String s, String hashString) {
//        if (s == null || hashString == null) {
//            System.out.println("Error: One or both strings are null");
//            return;
//        }
//
//        if (s.length() == 0 || hashString.length() == 0) {
//            System.out.println("Error: One or both strings are empty");
//            return;
//        }
//
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hashInBytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
//        String hashString2 = String.valueOf(hashInBytes, StandardCharsets.UTF_8);
//
//        if (hashString2.equals(hashString)) {
//            System.out.println("Hash values match");
//        } else {
//            System.out.println("Error: Hash values do not match");
//        }
//    }
//    public static void storeHash(String s, String hashString) {
//        if (s == null || hashString == null) {
//            System.out.println("Error: One or both strings are null");
//            return;
//        }
//
//        if (s.length() == 0 || hashString.length() == 0) {
//            System.out.println("Error: One or both strings are empty");
//            return;
//        }
//
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hashInBytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
//        String hashString2 = String.valueOf(hashInBytes, StandardCharsets.UTF_8);
//
//        if (hashString2.equals(hashString)) {
//            System.out.println("Hash values match");
//        } else {
//            System.out.println("Error: Hash values do not match");
//        }
//    }
//    public static String decrypt(String hashString) {
//        if (hashString == null)
//            return "";
//        if (hashString.length() == 0)
//            return "";
//
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hashInBytes = hashString.getBytes(StandardCharsets.UTF_8);
//        byte[] decryptedBytes = md.digest(hashInBytes);
//        String decryptedString = new String(decryptedBytes, StandardCharsets.UTF_8);
//
//        return decryptedString;
//    }


}
