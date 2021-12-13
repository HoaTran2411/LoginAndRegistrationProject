package vn.techmaster.user_account.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.techmaster.user_account.model.CreateUserRequest;
import vn.techmaster.user_account.model.Event;
import vn.techmaster.user_account.model.Role;
import vn.techmaster.user_account.model.User;
import vn.techmaster.user_account.repository.EventRepo;
import vn.techmaster.user_account.repository.RoleRepo;
import vn.techmaster.user_account.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenServiceImpl implements AuthenService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    EventRepo eventRepo;

    @Autowired
    RoleRepo roleRepo;

    @Override
    public void createNewUser(CreateUserRequest createUserRequest) {
        //encode password
        String password = BCrypt.hashpw(createUserRequest.getPassword(), BCrypt.gensalt(12));

        //create new user (lưu ý k dùng builder sẽ xảy ra lỗi nullPointerExp vs event)
        User user = new User();
        user.setPassword(password);
        user.setEmail(createUserRequest.getEmail());
        user.setFullName(createUserRequest.getFullName());

        //save event
        Event event = new Event();
        event.setName("Create account");
        user.addEvent(event);

        //save new User into database
        userRepo.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Boolean isAdmin(User user) {
        return true;
    }

    @Override
    public void setDefault() {
        User admin = userRepo.findByEmail("admin@techmaster.vn");
        admin.setPassword(BCrypt.hashpw("r@0T", BCrypt.gensalt(12)));
        Role adminRole = roleRepo.findByName("admin");
        admin.addRole(adminRole);
        userRepo.save(admin);
    }

    @Override
    public User findUserById(long id) {
        return userRepo.findById(id).get();
    }

    @Override
    public void updateUserInfo(User user) {
        Optional<User> opExistUser = userRepo.findById(user.getId());

        if (opExistUser.isPresent()) {
            User updatedUser = opExistUser.get();
            updatedUser.setFullName(user.getFullName());
            updatedUser.setEmail(user.getEmail());
            //add update event
            Event updateEvent = new Event();
            updateEvent.setName("Update Full Name or Email");
            updatedUser.addEvent(updateEvent);
            userRepo.save(updatedUser);
        }
    }

    @Override
    public void updatePassword(User user) {
        Optional<User> opUser = userRepo.findById(user.getId());
        if (opUser.isPresent()) {
            User updatedUser = opUser.get();
            updatedUser.setPassword(user.getPassword());
            Event updateEvent = new Event();
            updateEvent.setName("Update Password");
            updatedUser.addEvent(updateEvent);
            userRepo.save(updatedUser);
        }
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public void updateRoles(User user) {
        if (userRepo.findById(user.getId()).isPresent()){
            User existUser = userRepo.findById(user.getId()).get();
            existUser.setRoles(user.getRoles());
            userRepo.save(existUser);
        }
    }
}
