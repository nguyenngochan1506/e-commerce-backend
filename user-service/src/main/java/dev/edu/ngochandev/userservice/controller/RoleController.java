package dev.edu.ngochandev.userservice.controller;

import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.SuccessResponseDto;
import dev.edu.ngochandev.userservice.dto.req.CreateRoleRequestDto;
import dev.edu.ngochandev.userservice.dto.res.RoleResponseDto;
import dev.edu.ngochandev.userservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final Translator translator;
    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<RoleResponseDto> createRole(@RequestBody @Valid CreateRoleRequestDto req){
        return SuccessResponseDto.<RoleResponseDto>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(translator.translate("role.create.success"))
                .data(roleService.createRole(req))
                .build();
    }
}
