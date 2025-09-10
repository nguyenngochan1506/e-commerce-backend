package dev.edu.ngochandev.sharedkernel.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return Optional.of("SYSTEM");
    }
}
