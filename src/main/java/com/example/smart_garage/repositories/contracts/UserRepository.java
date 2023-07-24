package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.UserFilterOptions;
import com.example.smart_garage.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseCRUDRepository<User>  {


    List<User> getAll(Optional<String> search);
    int getNumberOfUsers();

 void updatePassword(Long userId, String newPassword);

    User getUserByFirstName(String firstName);
    User getUserByLastName(String lastName);
    User getUserByEmail(String email);
    User getUserByPhoneNUmber(String phoneNumber);

    List<User> getBlockedUsers();

    void block(User user);
    void unblock(User userToUnBLock);

    void activate(User user);

    void deActivate(User user);

    List<User> filterAllUsers(UserFilterOptions filterOptions);

}
