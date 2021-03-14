package by.nosevich.internship.task3.service;

import by.nosevich.internship.task3.dto.User;

public interface UserService extends DAO<User>{
    User getOneByUsername(String username);
}
