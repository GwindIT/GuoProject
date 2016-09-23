package com.idsmanager.mytest;

/**
 * Created by wind on 2016/5/11.
 */
public class NumberUtil {
    public static byte[] StringToByte(String number) {
        String[] numbers = number.split(":");
        byte[] bytes = new byte[number.length()];
        for (int i = 0; i < number.length(); i++) {
            try {
                bytes[i] = Byte.decode("0x"+numbers[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
