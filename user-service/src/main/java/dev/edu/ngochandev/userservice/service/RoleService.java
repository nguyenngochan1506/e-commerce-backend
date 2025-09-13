package dev.edu.ngochandev.userservice.service;

import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;
import dev.edu.ngochandev.userservice.dto.req.CreateRoleRequestDto;
import dev.edu.ngochandev.userservice.dto.res.PermissionResponseDto;
import dev.edu.ngochandev.userservice.dto.res.RoleResponseDto;

public interface RoleService {
    RoleResponseDto createRole(CreateRoleRequestDto req);

    String deleteRole(String roleId);

    PageResponseDto<PermissionResponseDto> getAllPermissions();
}
