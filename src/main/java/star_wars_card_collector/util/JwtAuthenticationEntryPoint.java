package star_wars_card_collector.util;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * JWT Authentication Entry Point for handling unauthorized access.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    /**
     * Invoked when authentication is required but fails due to unauthorized access.
     *
     * @param request       The HTTP request that resulted in an authentication failure.
     * @param response      The HTTP response to be sent to the client.
     * @param authException The authentication exception that caused the access failure.
     * @throws IOException If an I/O error occurs while handling the authentication failure.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
