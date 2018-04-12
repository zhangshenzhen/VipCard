
package com.bjypt.vipcard.utils;


public class StringBytes
{

    public StringBytes()
    {
    }

    public static String toHexString(byte bytes[])
    {
        char buf[] = new char[bytes.length * 2];
        int radix = 16;
        int mask = radix - 1;
        for(int i = 0; i < bytes.length; i++)
        {
            buf[2 * i] = digits[bytes[i] >>> 4 & mask];
            buf[2 * i + 1] = digits[bytes[i] & mask];
        }

        return new String(buf);
    }

    static final char digits[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
        'u', 'v', 'w', 'x', 'y', 'z'
    };

}

