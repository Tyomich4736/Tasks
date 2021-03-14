package by.nosevich.internship.task3.repository;

import by.nosevich.internship.task3.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findOneByUsername(String username);
}
