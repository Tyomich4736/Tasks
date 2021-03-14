package by.nosevich.internship.task3.controllers;

import by.nosevich.internship.task3.dto.User;
import by.nosevich.internship.task3.dto.enums.UserRole;
import by.nosevich.internship.task3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registerUser")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password){
        if(userService.getOneByUsername(username)==null){
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(UserRole.CLIENT);
            userService.save(user);
        }
        return "auth/login";
    }
}
