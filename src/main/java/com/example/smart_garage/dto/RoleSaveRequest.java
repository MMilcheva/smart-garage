package com.example.smart_garage.dto;

import com.example.smart_garage.models.Brand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

@Component
public class RoleSaveRequest {
    @NotNull
    @Size(min = 3, max = 15, message = "Role name must be between 3 and 15 symbols")
    private String roleName;

    public RoleSaveRequest() {
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
