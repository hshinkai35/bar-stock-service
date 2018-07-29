package api.security;

import api.entitiy.LoginDevice;
import api.repository.LoginDeviceRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Objects;


/**
 * 認証が成功した時の処理
 */
public class BarAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    final private Algorithm algorithm;
    final private LoginDeviceRepository loginDeviceRepository;

    public BarAuthenticationSuccessHandler(String secretKey, LoginDeviceRepository loginDeviceRepository) {
        Objects.requireNonNull(secretKey, "secret key must be not null");
        try {
            this.algorithm = Algorithm.HMAC256(secretKey);
            this.loginDeviceRepository = loginDeviceRepository;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException, ServletException {
        Long userId = ((BarLoginUser) auth.getPrincipal()).getUser().getId();
        String device = getDevice(request.getHeader("user-agent"));
        String userAgent = request.getHeader("user-agent");
        loginDeviceRepository.delete(userId, device);
        loginDeviceRepository.create(new LoginDevice(userId, device, userAgent));
        if (response.isCommitted()) {
            return;
        }
        setToken(response, generateToken(userId, device, userAgent));
        response.setStatus(HttpStatus.OK.value());
        clearAuthenticationAttributes(request);
    }

    private static final Long EXPIRATION_TIME = 1000L * 60L * 10L;

    private String generateToken(Long userId, String device, String userAgent) {
        Date issuedAt = new Date();
        Date notBefore = new Date(issuedAt.getTime());
        Date expiresAt = new Date(issuedAt.getTime() + EXPIRATION_TIME);
        return JWT.create()
                .withClaim("device", device)
                .withClaim("userAgent", userAgent)
                .withIssuedAt(issuedAt)
                .withNotBefore(notBefore)
                .withExpiresAt(expiresAt)
                .withSubject(userId.toString())
                .sign(this.algorithm);
    }

    private void setToken(HttpServletResponse response, String token) {
        response.setHeader("Authorization", String.format("Bearer %s", token));
    }

    /**
     * Removes temporary authentication-related data which may have been stored in the
     * session during the authentication process.
     */
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private String getDevice(String userAgent) {

        if (userAgent.contains("Windows NT")
                || userAgent.contains("Macintosh; Intel Mac OS")
                || userAgent.contains("Linux x86_64")
                || userAgent.contains("Linux i686")
                ) {
            return "PC";
        } else if (userAgent.contains("iPhone")
                || (userAgent.contains("Android") && userAgent.contains("Mobile"))
                || (userAgent.contains("BB10") && userAgent.contains("Mobile"))
                || userAgent.contains("Windows Phone")
                ) {
            return "MOBILE";
        } else {
            return "PC";
        }
    }
}