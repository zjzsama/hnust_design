package com.hnust.service;

import com.hnust.domian.User;

import java.util.List;


public interface UserService {
    User selectByUsername(String username);
    Boolean insertUser(User user);
    List<User> selectAll();
    Boolean updatePassword(User user);
    Boolean deleteUser(String username);
}
