package cn.npy.beemaths.web.rest.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by wangshichang on 2018/8/22.
 */
@Slf4j
public class MyDesUtil {

    private static final String PASSWORD = "3258047879";

    private static final String TOKEN_SIGN = "82M0q5oFnc";

    private static final int BIT = 7;

    public MyDesUtil() {
    }

    public static byte[] encrypt(byte[] datasource) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(PASSWORD.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, random);
            return cipher.doFinal(datasource);
        } catch (Throwable var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(byte[] src) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(PASSWORD.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, random);
        return cipher.doFinal(src);
    }

    public static String customEncrypt(String inputStr) {
        return getItemID(BIT) + (new BASE64Encoder()).encode(encrypt(inputStr.getBytes())).replaceAll("[\\s*\t\n\r]", "");
    }

    public static String customDecrypt(String inputStr) {
        try {
            return new String(decrypt((new BASE64Decoder()).decodeBuffer(inputStr.substring(BIT))));
        } catch (Exception e) {
            log.error("customDecrypt failed", e);
        }
        return null;
    }

    private static String getItemID(int n) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
            if ("char".equalsIgnoreCase(str)) { // 产生字母
                int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) (nextInt + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(str)) { // 产生数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     *
     * @param data
     * @return
     */
    public static String md5(String data) {
        return DigestUtils.md5Hex(data);
    }

}
