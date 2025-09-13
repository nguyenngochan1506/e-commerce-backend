package dev.edu.ngochandev.userservice.controller;

import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;
import dev.edu.ngochandev.sharedkernel.dto.res.SuccessResponseDto;
import dev.edu.ngochandev.userservice.dto.res.PermissionResponseDto;
import dev.edu.ngochandev.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final RoleService roleService;
    private final Translator translator;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<PageResponseDto<PermissionResponseDto>> getALlPermissions() {
        return SuccessResponseDto.<PageResponseDto<PermissionResponseDto>>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("fetch.permissions.success"))
                .data(roleService.getAllPermissions())
                .build();
    }
}
