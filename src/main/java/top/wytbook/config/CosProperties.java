package top.wytbook.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "cos")
@Component
@Data
public class CosProperties {
    String SecretId;
    String SecretKey;
    String region;
    String bucketName;
    String userAvatarPath;
    String articlePicturePath;
    String baseUrl;
}
