package com.xer97.mymall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @author xer97
 * @date 2019/3/31 14:41
 */
@Slf4j
public class Md5Util {

//    private static Logger logger = LoggerFactory.getLogger(Md5Util.class);

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(byteToHexString(bytes[i]));
        }
        return stringBuilder.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * 返回大写MD5
     *
     * @param origin      需加密的明文
     * @param charsetname 加密字符集
     * @return 加密后的密文
     */
    private static String md5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception exception) {
            log.error("MD5加密出错", exception);
        }
        return StringUtils.upperCase(resultString);
    }

    public static String md5EncodeUtf8(String origin) {
        origin = origin + PropertiesUtil.getProperty("password.salt", "");
        return md5Encode(origin, "utf-8");
    }


    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

}
