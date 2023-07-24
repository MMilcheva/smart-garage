package com.example.smart_garage.dto;

import org.springframework.stereotype.Component;

@Component
public class RoleResponse {

    private Long roleId;
    private String roleName;
    public RoleResponse() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
