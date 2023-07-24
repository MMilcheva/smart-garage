package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.RegisterDto;
import com.example.smart_garage.models.Role;
import com.example.smart_garage.models.User;
import com.example.smart_garage.repositories.contracts.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    private final UserRepository userRepository;


    public AuthenticationMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User fromDto(RegisterDto registerDto) {
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());

        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("customer");
        user.setRole(role);
        return user;
    }

}