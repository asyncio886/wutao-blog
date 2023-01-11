package top.wytbook.intercepor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.wytbook.constant.AttributeKey;
import top.wytbook.exceptions.NotLoginException;
import top.wytbook.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long uid = TokenUtils.getTokenInfo(request);
        if (uid == null) {
            throw new NotLoginException();
        }
        else {
            request.setAttribute(AttributeKey.UID, uid);
        }
        return true;
    }
}
