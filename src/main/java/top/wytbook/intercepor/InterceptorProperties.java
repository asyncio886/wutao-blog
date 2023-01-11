package top.wytbook.intercepor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "blog-interceptors")
@Component
@Data
public class InterceptorProperties {
    String[] needLoginPath = new String[]{};
    String[] needAdminPath = new String[]{};
    IpAttackProperty[] ipAttackProperties = new IpAttackProperty[]{};
    @Data
    public static class IpAttackProperty {
        String[] path;
        Long interval;
        Long maxTimes;
        Boolean ban = false;
        Long banTime;
    }
}
