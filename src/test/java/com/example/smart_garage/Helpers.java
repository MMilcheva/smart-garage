package com.example.smart_garage;

import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.Role;
import com.example.smart_garage.models.User;

import java.util.UUID;

public class Helpers {

    public static Brand createMockBrand() {
        Brand brand = new Brand();
        brand.setBrandId(1L);
        brand.setBrandName("Tesla");
        return brand;
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setPhoneNumber("0890123456");
        mockUser.setEmail("mock@user.com");
        mockUser.setRole(new Role(1L, "user"));
        return mockUser;
    }
    public static User createMockUserAdmin() {
        var mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUsername("MockAdminUsername");
        mockUser.setPassword("MockAdminPassword");
        mockUser.setLastName("MockAdminLastName");
        mockUser.setFirstName("MockAdminFirstName");
        mockUser.setPhoneNumber("0890123456");
        mockUser.setEmail("mockAdmin@user.com");
        mockUser.setRole(new Role(1L, "admin"));
        return mockUser;
    }
}
