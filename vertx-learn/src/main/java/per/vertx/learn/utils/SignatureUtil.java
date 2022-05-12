package per.vertx.learn.utils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tianzy
 * @date 2022年03月03日 16:50
 */
public class SignatureUtil {
    public static String getSignature(Map<String, String> parameterMap, String privateKey) {
        return md5(getParameterString(parameterMap, privateKey));
    }

    private static String getParameterString(Map<String, String> parameterMap, String privateKey) {
        List<String> parameterKeys = parameterMap.keySet().stream().sorted().collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (String parameterKey : parameterKeys) {
            sb.append(parameterKey);
            sb.append("=");
            sb.append(parameterMap.get(parameterKey));
            sb.append("&");
        }
        sb.append(privateKey);
        return sb.toString();
    }

    private static String md5(String input) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] b = md.digest();

            for (int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];

                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i));
            }
        } catch (Exception var6) {
            return null;
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>(16);
        map.put("aaa", "111");
        map.put("bbb", "222");
        String privateKey = getSignature(map, "privateKey");
        System.out.println(privateKey);
    }
}
