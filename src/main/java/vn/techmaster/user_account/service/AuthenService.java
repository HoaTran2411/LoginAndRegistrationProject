package vn.techmaster.user_account.service;

import vn.techmaster.user_account.model.CreateUserRequest;
import vn.techmaster.user_account.model.Role;
import vn.techmaster.user_account.model.User;

import java.util.List;

public interface AuthenService {
    //create account
    void createNewUser(CreateUserRequest createUserRequest);

    User findUserByEmail(String email);

    Boolean isAdmin(User user);

    void setDefault();

    User findUserById(long id);

    void updateUserInfo(User user);

    void updatePassword(User user);

    List<Role> getAllRoles();

    void updateRoles(User user);
}
