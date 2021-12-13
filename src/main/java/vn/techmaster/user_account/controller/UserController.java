package vn.techmaster.user_account.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import vn.techmaster.user_account.model.*;
import vn.techmaster.user_account.repository.UserRepo;
import vn.techmaster.user_account.service.AuthenService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    AuthenService authenService;

    @Autowired
    UserRepo userRepo;


    //Home page, login
    @GetMapping("/")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        model.addAttribute("checkAcc", "");
        return "login";
    }

    //login successfully
    @PostMapping("/user/detail")
    public String loginPageSuccessfully(@ModelAttribute LoginRequest loginRequest, BindingResult result, Model model) {
        if (!result.hasFieldErrors()) {
            //set default (email: 'admin@techmaster.vn' & role is admin & password is 'r@0T')
            authenService.setDefault();

            //check email
            User user = authenService.findUserByEmail(loginRequest.getEmail());

            if (user != null) {
                //check password
                boolean isPasword = BCrypt.checkpw(loginRequest.getPassword(), user.getPassword());
                if (isPasword) {
                    //save login event
                    Event loginEvent = new Event();
                    loginEvent.setName("Login");
                    user.addEvent(loginEvent);
                    userRepo.save(user);
                    model.addAttribute("user", user);

                    //sort event by time
                    List<Event> listEvent = user.getEvents().stream()
                            .sorted(Comparator.comparing(Event::getCreateDate).reversed()).collect(Collectors.toList());

                    //format time of event
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy --HH:mm:ss");
                    List<String> time = new ArrayList<>();
                    for (Event event : listEvent) {
                        time.add(event.getName() + ": " + sdf.format(event.getCreateDate()));
                    }
                    model.addAttribute("time", time);

                    return "account";
                }
            }
        }
        model.addAttribute("loginRequest", new LoginRequest());
        model.addAttribute("checkAcc", "Wrong email or password!");
        return "login";
    }

    //create new account
    @GetMapping("/create-account")
    public String createNewAcc(Model model) {
        model.addAttribute("user", new CreateUserRequest());
        model.addAttribute("checkEmail", " ");
        return "create-form";
    }

    @PostMapping("/create/save")
    public String saveNewAcc(@ModelAttribute CreateUserRequest createUserRequest, BindingResult result, Model model) {
        if (!result.hasFieldErrors()) {
            if (authenService.findUserByEmail(createUserRequest.getEmail()) == null) {
                authenService.createNewUser(createUserRequest);
            } else {
                model.addAttribute("user", new CreateUserRequest());
                model.addAttribute("checkEmail", "Email already exists in the system, pls input other email: ");
                return "create-form";
            }
        }
        return "redirect:/";
    }

    //Update full name and email
    @GetMapping("/account/editInfor/{id}")
    public String editUserInformation(@PathVariable long id, Model model) {
        User user = authenService.findUserById(id);
        model.addAttribute("checkEmail", " ");
        model.addAttribute("user", user);
        return "edit-infor-form";
    }

    @PostMapping("/edit/information")
    public String saveUpdatedUser(User user, BindingResult result, Model model) {
        if (!result.hasFieldErrors()) {
            String updatedEmail = user.getEmail();
            String oldEmail = userRepo.findById(user.getId()).get().getEmail();
            User userFindByUpdatedEmail = userRepo.findByEmail(updatedEmail);
            if (updatedEmail.equalsIgnoreCase(oldEmail) || userFindByUpdatedEmail == null) {
                authenService.updateUserInfo(user);
                return "redirect:/";
            } else {
                model.addAttribute("user", user);
                model.addAttribute("checkEmail", "Email already exists in the system, pls input other email: ");
                return "edit-infor-form";
            }
        }
        return "redirect:/";
    }

    //update password
    @GetMapping("/account/editPassword/{id}")
    public String editPassword(@PathVariable long id, Model model) {
        model.addAttribute("user", authenService.findUserById(id));
        return "edit-password";
    }

    @PostMapping("/edit/password")
    public String saveUpdatedPassword(User user, BindingResult result, HttpServletRequest request) {
        String newPassword = request.getParameter("password");
        if (!result.hasFieldErrors()) {
            //hash password
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
            authenService.updatePassword(user);
        }
        return "redirect:/";
    }

    //Retrieve password
    @GetMapping("/password")
    public String retrievePassword(Model model){
        model.addAttribute("checkEmail", " ");
        return "get-password";
    }

    @PostMapping("/password/retrieve")
    public String saveNewPassword(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        User user = authenService.findUserByEmail(email);
        if (user != null){
            //save event
            Event passwordEvent = new Event();
            passwordEvent.setName("Retrieve password");
            user.addEvent(passwordEvent);

            //create random password
            String newPassword = RandomStringUtils.randomAlphabetic(10);

            //hash new password & save into database
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
            userRepo.save(user);

            model.addAttribute("password", newPassword);
            model.addAttribute("email", email);
            return "get-password-success";
        }
        model.addAttribute("checkEmail", "Email not exist in system! Pls input correct email");
        return "get-password";
    }

    //find user
    @GetMapping("/findUser")
    public String findUser(Model model){
        model.addAttribute("checkEmail", " ");
        return "search";
    }

    //update roles
    @PostMapping("/user/role")
    public String addRolesForUser(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        User user = authenService.findUserByEmail(email);
        if(user == null){
            model.addAttribute("checkEmail", "Email not exist in system! Pls find other email");
            return "search";
        }
        List<Role> roles = authenService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        return "role";
    }

    @PostMapping("/updateRole/save")
    public String saveUpdatedRoles(User user, Model model){
        authenService.updateRoles(user);
        return "redirect:/";
    }



}
