package cn.npy.beemaths.web.rest.util;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class JWTUtil {

    private static final String PASSWORD = "Nms8&z9lgtQ*#RwC";

    public static final int bit = 5;

    public static Algorithm algorithm = Algorithm.HMAC256(PASSWORD);

    public JWTUtil() {
    }

    private static String getItemID(int n) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
            if ("char".equalsIgnoreCase(str)) { // 产生字母
                int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val.append((char) (nextInt + random.nextInt(26)));
            } else { // 产生数字
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString();
    }

    public static String solveToken(String token) {
        return Arrays.stream(token.split("\\.")).map(str -> str.substring(JWTUtil.bit)).collect(Collectors.joining("."));
    }
}
