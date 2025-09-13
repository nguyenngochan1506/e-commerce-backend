package dev.edu.ngochandev.userservice.repository;

import dev.edu.ngochandev.userservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    boolean existsByName(String name);

    @Modifying
    @Query("""
            UPDATE RoleEntity r
            SET r.isDeleted = true, r.updatedAt = CURRENT_TIMESTAMP
            WHERE r.id = :roleId
        """)
    void softDeleteById(@Param("roleId") String roleId);
}
