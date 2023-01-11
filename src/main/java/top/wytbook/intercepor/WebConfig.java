package top.wytbook.intercepor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.wytbook.util.CacheCountUtils;

import java.util.concurrent.TimeUnit;

@Component
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    AdminLoginInterceptor adminLoginInterceptor;

    @Autowired
    LoginInterceptor loginInterceptor;

    @Autowired
    CacheCountUtils cacheCountUtils;

    @Autowired
    InterceptorProperties interceptorProperties;
    private int order = 0;
    public void addIpAttackInterceptor(InterceptorProperties.IpAttackProperty property, CacheCountUtils utils, InterceptorRegistry registry) {
        IpAttackInterceptor interceptor = IpAttackInterceptor.create(utils).intervalSeconds(property.getInterval()).maxTimes(property.getMaxTimes());
        if (property.getBan() != null && property.getBan().equals(Boolean.TRUE)) {
            interceptor.ban(TimeUnit.DAYS.toSeconds(property.getBanTime()));
        }
        InterceptorRegistration registration = registry.addInterceptor(interceptor);
        for (String path : property.getPath()) {
            registration.addPathPatterns(path);
        }
        registration.order(order++);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] needLoginPaths = interceptorProperties.getNeedLoginPath();
        String[] needAdminPaths = interceptorProperties.getNeedAdminPath();
        InterceptorRegistration needLoginRegistration = registry.addInterceptor(loginInterceptor);
        InterceptorRegistration needAdminRegistration = registry.addInterceptor(adminLoginInterceptor);
        for (String needLoginPath : needLoginPaths) {
            needLoginRegistration.addPathPatterns(needLoginPath);
        }
        for (String needAdminPath : needAdminPaths) {
            needAdminRegistration.addPathPatterns(needAdminPath);
        }
        for (InterceptorProperties.IpAttackProperty ipAttackProperty : interceptorProperties.getIpAttackProperties()) {
            addIpAttackInterceptor(ipAttackProperty, cacheCountUtils, registry);
        }
        needLoginRegistration.order(order++);
        needAdminRegistration.order(order++);
    }
}
