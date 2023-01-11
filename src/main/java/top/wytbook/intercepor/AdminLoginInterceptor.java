package top.wytbook.intercepor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.wytbook.constant.AttributeKey;
import top.wytbook.exceptions.NotAdminException;
import top.wytbook.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Value("#{userServiceImpl}")
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long uid = (Long)request.getAttribute(AttributeKey.UID);
        if (uid == null) {
            throw new NotAdminException();
        }
        if (!userService.isAdmin(uid)) {
            throw new NotAdminException();
        }
        return true;
    }
}
