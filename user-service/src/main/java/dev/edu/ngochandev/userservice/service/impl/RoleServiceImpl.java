package dev.edu.ngochandev.userservice.service.impl;

import dev.edu.ngochandev.sharedkernel.exception.DuplicateResourceException;
import dev.edu.ngochandev.userservice.dto.req.CreateRoleRequestDto;
import dev.edu.ngochandev.userservice.dto.res.RoleResponseDto;
import dev.edu.ngochandev.userservice.entity.RoleEntity;
import dev.edu.ngochandev.userservice.mapper.RoleMapper;
import dev.edu.ngochandev.userservice.repository.RoleRepository;
import dev.edu.ngochandev.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponseDto createRole(CreateRoleRequestDto req) {
        if(roleRepository.existsByName(req.getName())) throw new DuplicateResourceException("error.role.exists");

        RoleEntity role = new RoleEntity();
        role.setDescription(req.getDescription());
        role.setName(req.getName());

        return roleMapper.toResponseDto(roleRepository.save(role));
    }
}
