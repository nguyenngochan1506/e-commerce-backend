package dev.edu.ngochandev.userservice.mapper;

import dev.edu.ngochandev.sharedkernel.common.BaseEntity;
import dev.edu.ngochandev.userservice.dto.res.RoleResponseDto;
import dev.edu.ngochandev.userservice.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissionIds", expression = "java(mapPermissionIds(entity))")
    RoleResponseDto toResponseDto(RoleEntity entity);

    default List<String> mapPermissionIds(RoleEntity entity) {
        if (entity.getPermissions() == null || entity.getPermissions().isEmpty()) {
            return List.of();
        }
        return entity.getPermissions().stream()
                .map(BaseEntity::getId)
                .toList();
    }
}
