package com.ciicsh.caldispatchjob.compute.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * Created by bill on 18/1/18.
 */
public class DESUtils {

    /**
     * DES密钥
     */
    private static String Key1 = "ZHONGZHI201502";
    private static String Key2 = "joying##";

    /**
     * DES加密
     * @param sInputString:需要加密的数据
     * @return
     */
    public static String encryptDES(String sInputString) throws Exception
    {
        String encryptStr = "";
        if (sInputString != null )
        {
            //DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            //从原始密钥数据创建DESKeySpec对象
            DESKeySpec desKeySpec = new DESKeySpec(Key2.getBytes());
            //创建一个密钥工厂，用它将DESKeySpec转化成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKeySpec);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            //将加密后的数据编码成字符串
            byte[] result = cipher.doFinal(sInputString.getBytes());

            encryptStr = new String(result);
        }
        return encryptStr;
    }

    /**
     * DES解密
     * @param data:需要解密的数据
     * @return
     * @throws Exception
     */
    public static String decryptDES(String data) throws Exception
    {
        String s = null;
        if ( data != null )
        {
            //DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            //从原始密钥数据创建DESKeySpec对象
            DESKeySpec desKeySpec = new DESKeySpec(Key2.getBytes());
            //创建一个密钥工厂，用它将DESKeySpec转化成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKeySpec);
            //Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            //将加密后的数据解码再解密
            byte[] buf = cipher.doFinal(data.getBytes());
            s = new String(buf);
        }
        return s;
    }
}
