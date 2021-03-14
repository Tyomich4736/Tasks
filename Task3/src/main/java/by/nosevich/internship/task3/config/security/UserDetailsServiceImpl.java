package by.nosevich.internship.task3.config.security;

import by.nosevich.internship.task3.dto.User;
import by.nosevich.internship.task3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = service.getOneByUsername(login);
        return UserDetailsImpl.convertUserToUserDetails(user);
    }

}
