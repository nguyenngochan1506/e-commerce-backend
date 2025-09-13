package dev.edu.ngochandev.userservice.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.userservice.entity.PermissionEntity;
import dev.edu.ngochandev.userservice.entity.RoleEntity;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import dev.edu.ngochandev.userservice.repository.PermissionRepository;
import dev.edu.ngochandev.userservice.repository.RoleRepository;
import dev.edu.ngochandev.userservice.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "DATA-INITIALIZER")
public class DataInitializer implements CommandLineRunner {
    public static final String CUSTOMER_ROLE = "customer";
    public static final String SUPER_ADMIN_ROLE = "super_admin";

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Value("${app.super-admin.username}")
    private String superAdminUsername;
    @Value("${app.super-admin.password}")
    private String superAdminPassword;
    @Value("${app.super-admin.email}")
    private String superAdminEmail;
    @Value("${app.super-admin.full-name}")
    private String superAdminFullName;

    private List<PermissionMigrationDto> permissionsFromFile;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("===== Starting Data Initialization =====");
        synchronizePermissionsFromFile();
        createDefaultRoles();
        createSuperAdmin();
        log.info("===== Data Initialization Finished =====");
    }

    private void createDefaultRoles() {
        log.info("Step 2: Creating default roles if they don't exist...");

        if (!roleRepository.existsByName(CUSTOMER_ROLE)) {
            RoleEntity customerRole = new RoleEntity();
            customerRole.setName(CUSTOMER_ROLE);
            customerRole.setDescription("Default role for new users");

            List<String> defaultPermissionNames = this.permissionsFromFile.stream()
                    .filter(PermissionMigrationDto::isDefault)
                    .map(PermissionMigrationDto::getName)
                    .collect(Collectors.toList());

            if (!defaultPermissionNames.isEmpty()) {
                List<PermissionEntity> defaultPermissions = permissionRepository.findByNameIn(defaultPermissionNames);
                customerRole.setPermissions(new HashSet<>(defaultPermissions));
                log.info("  -> Assigning {} default permissions to role {}", defaultPermissions.size(), CUSTOMER_ROLE);
            }

            roleRepository.save(customerRole);
            log.info("  -> CREATED Role: {}", CUSTOMER_ROLE);
        }

        if (!roleRepository.existsByName(SUPER_ADMIN_ROLE)) {
            RoleEntity superAdminRole = new RoleEntity();
            superAdminRole.setName(SUPER_ADMIN_ROLE);
            superAdminRole.setDescription("Super administrator with all permissions");
            List<PermissionEntity> allPermissions = permissionRepository.findAll();
            superAdminRole.setPermissions(new HashSet<>(allPermissions));
            roleRepository.save(superAdminRole);
            log.info("  -> CREATED Role: {}", SUPER_ADMIN_ROLE);
        }
        log.info("Step 2: Finished creating default roles.");
    }

    private void createSuperAdmin() {
        log.info("Step 3: Creating super admin account if it doesn't exist...");
        if (!userRepository.existsByUsername(superAdminUsername)) {
            UserEntity superAdmin = new UserEntity();
            superAdmin.setUsername(superAdminUsername);
            superAdmin.setPassword(passwordEncoder.encode(superAdminPassword));
            superAdmin.setEmail(superAdminEmail);
            superAdmin.setFullName(superAdminFullName);
            superAdmin.setStatus(UserStatus.ACTIVE);
            superAdmin.setPhoneNumber("0123456789");

            RoleEntity superAdminRole = roleRepository.findByName(SUPER_ADMIN_ROLE)
                    .orElseThrow(() -> new RuntimeException("Super admin role not found!"));

            superAdmin.setRoles(Set.of(superAdminRole));
            userRepository.save(superAdmin);
            log.info("  -> CREATED Super Admin Account: {}", superAdminUsername);
        }
        log.info("Step 3: Finished creating super admin account.");
    }


    private void synchronizePermissionsFromFile() {
        log.info("Step 1: Synchronizing permissions from 'permissions.json'...");
        try (InputStream inputStream = new ClassPathResource("migrations/permissions.json").getInputStream()) {
            this.permissionsFromFile = objectMapper.readValue(inputStream, new TypeReference<>() {});

            Map<String, PermissionEntity> existingPermissionsMap = permissionRepository.findAll().stream()
                    .collect(Collectors.toMap(p -> p.getMethod().name() + ":" + p.getPath(), Function.identity()));

            for (PermissionMigrationDto dto : this.permissionsFromFile) {
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