package dev.edu.ngochandev.userservice.entity;

import dev.edu.ngochandev.sharedkernel.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_roles", schema = "user_schema")
@SQLRestriction("is_deleted = false")
public class RoleEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_role_permissions",
        schema = "user_schema",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissions = new HashSet<>();
}
