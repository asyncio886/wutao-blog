package top.wytbook.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class Md5Utils {
    public static String getMd5String(String text, String salt) {
        return DigestUtils.md5DigestAsHex((text + salt).getBytes(StandardCharsets.UTF_8));
    }
}
