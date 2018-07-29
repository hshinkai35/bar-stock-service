package api.security;

import api.repository.LoginDeviceRepository;
import api.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

//@Slf4j
public class BarTokenFilter extends GenericFilterBean {

    final private UserRepository userRepository;
    final private LoginDeviceRepository loginDeviceRepository;
    final private Algorithm algorithm;

    public BarTokenFilter(UserRepository userRepository, LoginDeviceRepository loginDeviceRepository, String secretKey) {
        Objects.requireNonNull(secretKey, "secret key must be not null");
        this.userRepository = userRepository;
        this.loginDeviceRepository = loginDeviceRepository;
        try {
            this.algorithm = Algorithm.HMAC256(secretKey);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String token = resolveToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            authentication(verifyToken(token));
        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(ServletRequest request) {
        String token = ((HttpServletRequest) request).getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }

    private DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private void authentication(DecodedJWT jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        String device = jwt.getClaim("device").asString();
        String userAgent = jwt.getClaim("userAgent").asString();
        userRepository.findById(userId).ifPresentOrElse(user -> {
            loginDeviceRepository.findByUserIdAndDevice(userId, device, userAgent).ifPresentOrElse(loginDevice -> {
                BarLoginUser simpleLoginUser = new BarLoginUser(user);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(simpleLoginUser, null, simpleLoginUser.getAuthorities()));
            }, () -> {
                throw new RuntimeException("loginDevice does not exist");
            });
        }, () -> {
            throw new RuntimeException("user does not exist");
        });

    }

}