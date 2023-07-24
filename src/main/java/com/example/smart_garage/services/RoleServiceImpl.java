package com.example.smart_garage.services;

import com.example.smart_garage.models.Role;
import com.example.smart_garage.repositories.contracts.RoleRepository;
import com.example.smart_garage.services.contracts.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    public RoleServiceImpl(RoleRepository roleRepository)  {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles(Optional<String> search) {

        return roleRepository.getAllRoles(search);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.get();
    }
    @Override
    public Role getRoleById(Long roleId) {
        Role role = roleRepository.getById(roleId);
        return role;
    }
    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.getByField("roleName", name);
        return role;
    }
    @Override
    public Role createRole(Role role) {
        roleRepository.create(role);
        return role;
    }
    @Override
    public Role updateRole(Role role) {
        roleRepository.update(role);
        return role;
    }

    @Override
    public void deleteRole(long roleId) {

        roleRepository.delete(roleId);
    }
  }
