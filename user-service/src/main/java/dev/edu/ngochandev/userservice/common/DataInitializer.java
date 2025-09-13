package dev.edu.ngochandev.userservice.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.userservice.entity.PermissionEntity;
import dev.edu.ngochandev.userservice.repository.PermissionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "DATA-INITIALIZER")
public class DataInitializer implements CommandLineRunner {
    public static final String DEFAULT_ROLE = "customer";
    public static final String SUPER_ADMIN_ROLE = "super_admin";
    private final PermissionRepository permissionRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.super-admin.username}")
    private String superAdminUsername;

    @Value("${app.super-admin.password}")
    private String superAdminPassword;

    @Value("${app.super-admin.email}")
    private String superAdminEmail;

    @Value("${app.super-admin.full-name}")
    private String superAdminFullName;

    @Override
    public void run(String... args) throws Exception {
        log.info("===== Starting Data Initialization =====");
        synchronizePermissionsFromFile();
        log.info("===== Data Initialization Finished =====");
    }

    private void synchronizePermissionsFromFile() {
        log.info("Step 1: Synchronizing permissions from 'permissions.json'...");
        try (InputStream inputStream = new ClassPathResource("migrations/permissions.json").getInputStream()) {
            List<PermissionMigrationDto> permissionsFromFile =
                    objectMapper.readValue(inputStream, new TypeReference<>() {});

            Map<String, PermissionEntity> existingPermissionsMap = permissionRepository.findAll().stream()
                    .collect(Collectors.toMap(p -> p.getMethod().name() + ":" + p.getPath(), Function.identity()));

            for (PermissionMigrationDto dto : permissionsFromFile) {
                String key = dto.getMethod() + ":" + dto.getApiPath();
                String translatedName = dto.getName();

                PermissionEntity existingPermission = existingPermissionsMap.remove(key);

                if (existingPermission == null) {
                    PermissionEntity newPermission = new PermissionEntity();
                    newPermission.setName(translatedName);
                    newPermission.setPath(dto.getApiPath());
                    newPermission.setMethod(HttpMethod.valueOf(dto.getMethod()));
                    newPermission.setModule(dto.getModule());
                    permissionRepository.save(newPermission);
                    log.info("  -> CREATED Permission: [{} {}]", dto.getMethod(), dto.getApiPath());
                } else {
                    boolean needsUpdate = !existingPermission.getName().equals(translatedName)
                            || !existingPermission.getModule().equals(dto.getModule());
                    if (needsUpdate) {
                        existingPermission.setName(translatedName);
                        existingPermission.setModule(dto.getModule());
                        permissionRepository.save(existingPermission);
                        log.info("  -> UPDATED Permission: [{} {}]", dto.getMethod(), dto.getApiPath());
                    }
                }
            }

            if (!existingPermissionsMap.isEmpty()) {
                log.warn("  -> Found {} obsolete permissions to be DELETED (soft).", existingPermissionsMap.size());
                for (PermissionEntity obsoletePermission : existingPermissionsMap.values()) {
                    obsoletePermission.setIsDeleted(true);
                    permissionRepository.save(obsoletePermission);
                    log.info(
                            "  -> DELETED (Soft) Permission: [{} {}]",
                            obsoletePermission.getMethod(),
                            obsoletePermission.getPath());
                }
            }
            log.info("Step 1: Finished synchronizing permissions.");

        } catch (Exception e) {
            log.error("FATAL: Failed to synchronize permissions from file.", e);
            throw new RuntimeException(e);
        }
    }

    @Data
    private static class PermissionMigrationDto {
        private String name;
        private String apiPath;
        private String method;
        private String module;

        @JsonProperty("isDefault")
        private boolean isDefault = false;
    }

}
