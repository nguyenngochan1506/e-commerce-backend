package dev.edu.ngochandev.userservice.mapper;

import dev.edu.ngochandev.userservice.dto.res.RoleResponseDto;
import dev.edu.ngochandev.userservice.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponseDto toResponseDto(RoleEntity entity);
}
