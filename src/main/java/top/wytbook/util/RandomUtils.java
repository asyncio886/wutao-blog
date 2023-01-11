package top.wytbook.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomUtils {
    public String getRandomCode(int len) {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        return s.substring(0, len);
    }
}
