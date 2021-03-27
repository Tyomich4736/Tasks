package by.nosevich.internship.task3.controllers;

import by.nosevich.internship.task3.dto.User;
import by.nosevich.internship.task3.dto.enums.UserRole;
import by.nosevich.internship.task3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Artyom Nosevich
 *  This controller is responsible for registering new user
 */
@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method create and save new user if database doesn't have a user with the same username
     * @param username the username of user which should be created
     * @param password the password of user which should be created
     * @param model the model for transfer attributes between controller and page
     * @return login page path if user was created, else return registration page path
     */
    @PostMapping("/registerUser")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model){
        if(userService.getOneByUsername(username)==null){
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(UserRole.CLIENT);
            userService.save(user);
            return "auth/login";
        }
        model.addAttribute("userExistError", true);
        return "auth/register";
    }
}
