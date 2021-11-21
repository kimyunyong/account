package com.nandsoft.account.controller;

import com.nandsoft.account.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    private Logger logger = LogManager.getLogger(MainController.class);

    @GetMapping({"/index", "/", ""})
    public String index(){
       /* User user = new User("kyy8", "1234", "email@gmail.com", "Role_User", null);
        userService.join(user);*/

        logger.debug("[debug] log!");

        return "/index";
    }

    @GetMapping(value = "/admin/index")
    public String admin() {
        return "/index";
    }

    @GetMapping(value = "/user/index")
    public String userIndex() {
        return "/index";
    }
}