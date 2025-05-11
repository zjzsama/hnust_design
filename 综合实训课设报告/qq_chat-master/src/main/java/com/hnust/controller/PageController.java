package com.hnust.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {
    private static final Logger logger = LoggerFactory.getLogger(PageController.class);
    @GetMapping("/login_page")
    public String login()
    {
        logger.info("i'm coming------login");
        return "login";
    }
    @GetMapping("/enroll_page")
    public String enroll()
    {
        logger.info("i'm coming------enroll");
        return "enroll";
    }
    @GetMapping("/chat/main_page")
    public String main()
    {
        logger.info("i'm coming------main");
        return "main";
    }
    @GetMapping("/chat/account_page")
    public String account()
    {
        logger.info("i'm coming------account");
        return "account";
    }
}