package com.example.smart_garage.dto;

import com.example.smart_garage.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserSaveRequest {

    @NotNull
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols")
    private String firstName;
    @NotNull
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols")
    private String lastName;
    @Email(message = "Please provide a valid e-mail")
    @NotNull
    private String email;

    @NotNull
    @Size(min = 10, max = 10, message = "Phone number should be 10 symbols and without the country phone code")
    @Pattern(regexp = "^0.*")
    private String phoneNumber;
    private String roleName;

    private boolean isActivated;

    private boolean isArchived;

    public UserSaveRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isActivated() {
        return true;
    }
    public boolean setActivated(boolean IsActivated) {
        isActivated = true;
        return IsActivated;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
