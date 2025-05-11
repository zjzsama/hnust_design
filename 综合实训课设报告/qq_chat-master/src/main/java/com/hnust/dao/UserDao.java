package com.hnust.dao;

import com.hnust.domian.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserDao {
    User selectByUsername(String username);
    Boolean insertUser(User user);
    List<User> selectAll();
    Boolean updatePassword(User user);
    Boolean deleteUser(String username);
}
