package dev.edu.ngochandev.userservice.mapper;

import dev.edu.ngochandev.userservice.dto.res.PermissionResponseDto;
import dev.edu.ngochandev.userservice.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponseDto toResponseDto(PermissionEntity entity);
}
