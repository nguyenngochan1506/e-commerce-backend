package dev.edu.ngochandev.sharedkernel.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.ErrorResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtAuthEntryPoint  implements AuthenticationEntryPoint {
    private final Translator translate;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMessage(translate.translate("error.token.invalid"));
        errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}