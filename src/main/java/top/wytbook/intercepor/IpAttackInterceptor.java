package top.wytbook.intercepor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.wytbook.exceptions.IpAttackException;
import top.wytbook.exceptions.IpNotFoundException;
import top.wytbook.util.CacheCountUtils;
import top.wytbook.util.IpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 阻止ip强刷接口，通过build构造ip攻击拦截器
 */

public class IpAttackInterceptor implements HandlerInterceptor {
    // 检测ip时间
    private long timeInterval;
    // 最大攻击数
    private long intervalAttackMaxTime;

    private final CacheCountUtils cacheCountUtils;

    private boolean ifOutBanIp = false;

    private long banSeconds;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddr = IpUtils.getIpAddr(request);
        if (ipAddr == null || ipAddr.equals("")) {
            throw  new IpNotFoundException();
        }
        if (cacheCountUtils.isBanKey(ipAddr)) {
            throw new IpAttackException();
        }
        if (!cacheCountUtils.increment(ipAddr, timeInterval, intervalAttackMaxTime)) {
            if (ifOutBanIp) {
                cacheCountUtils.banKey(ipAddr, banSeconds);
            }
            throw new IpAttackException();
        }
        return true;
    }

    private void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    private void setIntervalAttackMaxTime(long intervalAttackMaxTime) {
        this.intervalAttackMaxTime = intervalAttackMaxTime;
    }

    private IpAttackInterceptor(CacheCountUtils cacheCountUtils) {
        this.cacheCountUtils = cacheCountUtils;
    }

    public static IpAttackInterceptor create(CacheCountUtils cacheCountUtils) {
        return new IpAttackInterceptor(cacheCountUtils);
    }

    public IpAttackInterceptor intervalSeconds(Long timeInterval) {
        this.setTimeInterval(timeInterval);
        return this;
    }

    public IpAttackInterceptor maxTimes(Long maxTimes) {
        this.setIntervalAttackMaxTime(maxTimes);
        return this;
    }

    public IpAttackInterceptor ban(long banSeconds) {
        this.ifOutBanIp = true;
        this.banSeconds = banSeconds;
        return this;
    }
}
