package by.nosevich.internship.task3.service.jpa;

import by.nosevich.internship.task3.dto.User;
import by.nosevich.internship.task3.dto.enums.UserRole;
import by.nosevich.internship.task3.repository.UserRepository;
import by.nosevich.internship.task3.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JPAUserService implements UserService, InitializingBean {

    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll() {
        return repo.findAll();
    }

    @Override
    public void save(User entity) {
        repo.save(entity);
    }

    @Override
    public void delete(User entity) {
        repo.delete(entity);
    }

    @Override
    public User getById(int id) {
        return repo.findById(id).get();
    }

    @Override
    public User getOneByUsername(String username) {
        return repo.findOneByUsername(username);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getAll().isEmpty()){
            save(new User(null, "Client", passwordEncoder.encode("client"), UserRole.CLIENT));
            save(new User(null, "Admin", passwordEncoder.encode("admin"), UserRole.ADMIN));
        }
    }
}
