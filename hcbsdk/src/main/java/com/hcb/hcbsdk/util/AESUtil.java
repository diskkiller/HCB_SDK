package com.hcb.hcbsdk.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author wangguowei
 * @ClassName: AES
 * @Description: 加密解密
 * @date 15/10/27 下午5:15
 */
public class AESUtil {

    private static final String TAG = "AESUtil";

    private static final String TRANSFORMATION = "AES/CBC/NoPadding";

    private static final String ALGO_AES            = "AES";
    private static final String ALGO_RIJNDAEL       = "Rijndael";

    //解密
    public static String decrypt(String data) {
//        L.debug("NetTask",data);
        return decrypt(data,C.key, StringUtils.md5(C.key).substring(0, 16));
    }

    //加密
    public static String encrypt(String data) {
        return encrypt(data,C.key, StringUtils.md5(C.key).substring(0, 16));
    }

    //解密
    public static String decrypt(String encryptedData, String secretKey, String initialVectorString) {
        String decryptedData = null;

        try
        {

            byte[] encrypted1 = Base64.decode(encryptedData.getBytes(), Base64.NO_WRAP);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(StringUtils.getK(secretKey.getBytes()), ALGO_RIJNDAEL);
            IvParameterSpec ivspec = new IvParameterSpec(initialVectorString.getBytes("utf-8"));

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            decryptedData = new String(original);
            return decryptedData;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }




//        try {
//
//            SecretKeySpec skeySpec = new SecretKeySpec(SYStringUtils.getK(secretKey.getBytes()), ALGO_RIJNDAEL);
//
//            IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes("utf-8"));
//
//            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec, initialVector);
//            byte[] encryptedByteArray = Base64.decode(encryptedData.getBytes(), Base64.NO_WRAP);
//            byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
//
//            decryptedData = SYStringUtils.getRealData(decryptedByteArray);
//
//        } catch (Exception e) {
//            L.debug(TAG,e.getLocalizedMessage());
//        }
//
//        return decryptedData;
    }

    //加密
    public static String encrypt(String data, String secretKey, String initialVectorString) {
        String encryptedData = null;
        try {

            SecretKeySpec skeySpec = new SecretKeySpec(StringUtils.getK(secretKey.getBytes()), ALGO_RIJNDAEL);
            IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initialVector);

            byte[] encryptedByteArray = cipher.doFinal(StringUtils.getEncrytedData(data.getBytes()));
            encryptedData = Base64.encodeToString(encryptedByteArray, Base64.NO_WRAP);

        } catch (Exception e) {
            L.debug(TAG,e.getLocalizedMessage());
        }

        return encryptedData;
    }

}