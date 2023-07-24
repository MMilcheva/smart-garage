package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> getRoles();

    Role updateRole(Role role);

    void deleteRole(long roleId);

    List<Role> getAllRoles(Optional<String> search);

    Role getRoleById(Long roleId);

    Role getRoleByName(String name);

    Role createRole(Role role);
}
