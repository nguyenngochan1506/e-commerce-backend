package dev.edu.ngochandev.userservice.repository;

import dev.edu.ngochandev.userservice.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
    List<PermissionEntity> findByNameIn(List<String> names);
}
