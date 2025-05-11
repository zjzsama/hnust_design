package com.hnust.service.impl;

import com.hnust.dao.UserDao;
import com.hnust.domian.User;
import com.hnust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户service实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User selectByUsername(String username) {
        return userDao.selectByUsername(username);
    }

    @Override
    public Boolean insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public List<User> selectAll() {
        return userDao.selectAll();
    }

    @Override
    public Boolean updatePassword(User user) {
        return userDao.updatePassword(user);
    }

    @Override
    public Boolean deleteUser(String username) {
        return userDao.deleteUser(username);
    }
}
