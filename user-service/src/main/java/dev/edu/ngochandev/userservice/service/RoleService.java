package dev.edu.ngochandev.userservice.service;

import dev.edu.ngochandev.userservice.dto.req.CreateRoleRequestDto;
import dev.edu.ngochandev.userservice.dto.res.RoleResponseDto;

public interface RoleService {
    RoleResponseDto createRole(CreateRoleRequestDto req);
}
