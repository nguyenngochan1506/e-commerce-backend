package dev.edu.ngochandev.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.ErrorResponseDto;
import dev.edu.ngochandev.userservice.entity.PermissionEntity;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final AntPathMatcher antPathMatcher;
    private final Translator translator;
    private final ObjectMapper objectMapper;

    private final String[] publicEndpoints = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/categories",
            "/api/v1/products/**",
            "/api/v1/products/category/**",
            "/api/v1/auth/verify-email",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/reset-password"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        for (String endpoint : publicEndpoints) {
            if (antPathMatcher.match(endpoint, requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserEntity currentUser)) {
            filterChain.doFilter(request, response);
            return;
        }

        Set<PermissionEntity> userPermissions = currentUser.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .collect(Collectors.toSet());

        boolean hasPermission = userPermissions.stream()
                .anyMatch(permission ->
                        permission.getMethod().name().equalsIgnoreCase(method) &&
                                antPathMatcher.match(permission.getPath(), requestURI)
                );

        if (hasPermission) {
            filterChain.doFilter(request, response);
        } else {
            sendErrorResponse(response, requestURI);
        }

    }

    private void sendErrorResponse(HttpServletResponse response, String path) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
        errorResponse.setPath(path);
        errorResponse.setMessage(translator.translate("error.access.denied"));
        errorResponse.setError(HttpStatus.FORBIDDEN.getReasonPhrase());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
