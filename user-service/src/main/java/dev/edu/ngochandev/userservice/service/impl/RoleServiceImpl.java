package dev.edu.ngochandev.userservice.service.impl;

import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;
import dev.edu.ngochandev.sharedkernel.exception.DuplicateResourceException;
import dev.edu.ngochandev.sharedkernel.exception.ResourceNotFoundException;
import dev.edu.ngochandev.userservice.common.MyUtils;
import dev.edu.ngochandev.userservice.dto.req.CreateRoleRequestDto;
import dev.edu.ngochandev.userservice.dto.res.PermissionResponseDto;
import dev.edu.ngochandev.userservice.dto.res.RoleResponseDto;
import dev.edu.ngochandev.userservice.entity.PermissionEntity;
import dev.edu.ngochandev.userservice.entity.RoleEntity;
import dev.edu.ngochandev.userservice.mapper.PermissionMapper;
import dev.edu.ngochandev.userservice.mapper.RoleMapper;
import dev.edu.ngochandev.userservice.repository.PermissionRepository;
import dev.edu.ngochandev.userservice.repository.RoleRepository;
import dev.edu.ngochandev.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final Translator translator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleResponseDto createRole(CreateRoleRequestDto req) {
        if(roleRepository.existsByName(req.getName())) throw new DuplicateResourceException("error.role.exists");

        RoleEntity role = new RoleEntity();
        role.setDescription(req.getDescription());
        role.setName(req.getName());
        if(req.getPermissionIds() != null && !req.getPermissionIds().isEmpty()) {
            List<PermissionEntity> permissions = permissionRepository.findAllById(req.getPermissionIds());
            if(permissions.size() != req.getPermissionIds().size()) throw new ResourceNotFoundException("error.permission.notfound");
            role.setPermissions(new HashSet<>(permissions));
        }

        return roleMapper.toResponseDto(roleRepository.save(role));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteRole(String roleId) {
        RoleEntity role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("error.role.notfound"));

        if(role.getUsers() != null && !role.getUsers().isEmpty()) throw new IllegalStateException("error.role.assigned.users");

        roleRepository.softDeleteById(roleId);

        return roleId;
    }

    @Override
    public PageResponseDto<PermissionResponseDto> getAllPermissions() {
        List<PermissionResponseDto> permissions = permissionRepository.findAll().stream().map(p -> {
                    PermissionResponseDto dto = permissionMapper.toResponseDto(p);
                    dto.setName(translator.translate(p.getName()));
                    return dto;
                })
                .toList();

        return PageResponseDto.<PermissionResponseDto>builder()
                .totalPages(1)
                .currentPage(1)
                .totalItems(permissions.size())
                .items(permissions)
                .build();
    }

    @Override
    public PageResponseDto<RoleResponseDto> getAllRoles(Integer page, Integer size, String sort, String search) {
        Pageable pageable = MyUtils.createPageable(page, size, sort);

        Page<RoleEntity> pageRoles;

        if(StringUtils.hasText(search)) {
            pageRoles = roleRepository.findAllByName(search, pageable);
        }else{
            pageRoles = roleRepository.findAll(pageable);
        }
        List<RoleResponseDto> roles = pageRoles.getContent().stream()
                .map(roleMapper::toResponseDto)
                .toList();
        return PageResponseDto.<RoleResponseDto>builder()
                .totalPages(pageRoles.getTotalPages())
                .currentPage(page)
                .totalItems(pageRoles.getTotalElements())
                .items(roles)
                .build();
    }
}
