package dev.edu.ngochandev.userservice.controller;

import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;
import dev.edu.ngochandev.sharedkernel.dto.res.SuccessResponseDto;
import dev.edu.ngochandev.userservice.dto.req.CreateRoleRequestDto;
import dev.edu.ngochandev.userservice.dto.req.UpdateRoleRequestDto;
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

    @PatchMapping("/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<RoleResponseDto> updateRole(@PathVariable("roleId") String roleId, @RequestBody UpdateRoleRequestDto req) {
        return SuccessResponseDto.<RoleResponseDto>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("role.update.success"))
                .data(roleService.updateRole(roleId, req))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<RoleResponseDto> createRole(@RequestBody @Valid CreateRoleRequestDto req){
        return SuccessResponseDto.<RoleResponseDto>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(translator.translate("role.create.success"))
                .data(roleService.createRole(req))
                .build();
    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<String> deleteRole(@PathVariable("roleId") String roleId){
        return SuccessResponseDto.<String>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("role.delete.success"))
                .data(roleService.deleteRole(roleId))
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<PageResponseDto<RoleResponseDto>> getAllRoles(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "id:desc") String sort ,
            @RequestParam(value = "search", required = false, defaultValue = "") String search
    ){
        return SuccessResponseDto.<PageResponseDto<RoleResponseDto>>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("role.get.all.success"))
                .data(roleService.getAllRoles(page, size, sort, search))
                .build();
    }
}
