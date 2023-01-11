package top.wytbook.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.jetbrains.annotations.Nullable;
import top.wytbook.constant.AttributeKey;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public class TokenUtils {
    public static final String TOKEN_KEY = "visitor_token";

    static final String TOKEN_SECRET = "377641Wyt";
    static final String ISSUER = "wutao";
    public static void createToken(Long uid, HttpServletResponse response, int day) {
        String token = JWT.create().withIssuer(ISSUER)
                .withClaim(AttributeKey.UID, uid)
                .sign(Algorithm.HMAC256(TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)));
        Cookie cookie = new Cookie(TOKEN_KEY, token);
        cookie.setPath("/api");
        cookie.setMaxAge(day * 60 * 60 * 24);
        response.addCookie(cookie);
    }

    private static Long verifyToken(String tokenString) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
                .withIssuer(ISSUER)
                .build();
        DecodedJWT decodedJWT = verifier.verify(tokenString);
        return decodedJWT.getClaim(AttributeKey.UID).asLong();
    }

    @Nullable
    public static Long getTokenInfo(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return null;
            }
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(TOKEN_KEY)) {
                    return verifyToken(cookie.getValue());
                }
            }
        }
        catch (Exception e) {
            return null;
        }
        return null;
    }

    public static void clearToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_KEY, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
