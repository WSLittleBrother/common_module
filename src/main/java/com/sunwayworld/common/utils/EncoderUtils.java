package com.sunwayworld.common.utils;

import com.sunwayworld.common.utils.encoder.BASE64Decoder;
import com.sunwayworld.common.utils.encoder.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description 加密、解密工具类
 * @author yanghan ${2016/5/13}
 * @version V1.0
 */
public class EncoderUtils {

    private static final String Algorithm = "DESede"; // 定义 加密算法,可用
    // DES,DESede,Blowfish
    /**
     * 加密以string 明文输入，string密文输出;
     *
     * @param strMing
     * @return
     * @throws SQLException
     * @throws UnsupportedEncodingException
     */
    public static String getencString(String strMing,
                                      String deskey) throws SQLException, UnsupportedEncodingException {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";

        BASE64Encoder encoder = new BASE64Encoder();
        try {
            byteMing = strMing.getBytes("utf-8");
            byteMi = encryptMode(deskey, byteMing);
            strMi = encoder.encode(byteMi);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } finally {
            encoder = null;
            byteMi = null;
            byteMing = null;
        }
        return strMi;
    }
    // keybyte为加密密钥，长度为24字节
    // src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(String key, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = createKey(key);

            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public static SecretKey createKey(String key) {
        SecretKey deskey = null;
        try {
            deskey = new SecretKeySpec(key.getBytes("utf-8"), Algorithm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return deskey;
    }
    /**
     * 解密以string 密文输入,String 明文输出;
     *
     * @param strMi
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static String getDecString( String strMi,
                                      String deskey) throws SQLException, IOException {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try {
            byteMi = base64Decoder.decodeBuffer(strMi);
            byteMing = decryptMode(deskey, byteMi);
            strMing = new String(byteMing, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            base64Decoder = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;

    }

    // keybyte为加密密钥，长度为24字节
    // src为加密后的缓冲区
    public static byte[] decryptMode(String key, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = createKey(key);

            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
