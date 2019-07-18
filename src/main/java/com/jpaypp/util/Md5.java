package com.jpaypp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

    private static final String KEY_MD5 = "MD5";
    /** 全局数组 */
    private static final String[] STRING_DIGITS = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     *  返回形式为数字跟字符串
     * @param bByte
     * @return
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return STRING_DIGITS[iD1] + STRING_DIGITS[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     * @param bByte
     * @return
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
    /**
     * MD5加密
     * @param strObj
     * @return
     * @throws Exception
     */
    public static String md5(String strObj){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(KEY_MD5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteToString(md.digest(strObj.getBytes()));
    }
}
