package com.hnust.service;

import com.hnust.dao.ChatDao;
import com.hnust.dao.UserDao;
import com.hnust.domian.ChatRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 长夜
 * @date 2023/5/20 15:31
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogicTest {
    @Autowired
    UserDao userDao;
    @Autowired
    ChatDao chatDao;
    @Test
    public void test()
    {
        System.out.println(userDao.selectAll());
    }
}
