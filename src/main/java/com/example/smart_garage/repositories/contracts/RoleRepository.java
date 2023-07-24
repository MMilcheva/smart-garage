package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends BaseCRUDRepository<Role>  {
    List<Role> getAllRoles(Optional<String> search);
    List<Role> get();
}
