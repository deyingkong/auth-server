package com.austin.flashcard.auth.controller;

import com.austin.flashcard.auth.constant.Common;
import com.austin.flashcard.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:
 * @Author: Austin
 * @Create: 3/2/2024 2:16 PM
 */
@Slf4j
@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Authentication authentication, Model model){

/*        var email = userService.retrieveEmailFromAuthentication(authentication);
        if(!userService.isUserExist(email)){
            var username = userService.retrieveUsernameFromAuthentication(authentication);
            userService.registerUser(email, username, Double.valueOf(Math.random()*100000).toString());
            log.info("a new user is registered:{}, {}", email, username);
        }*/

        model.addAttribute(Common.PAGE_TITLE_KEY, "Homepage");
        return "home";
    }
}
