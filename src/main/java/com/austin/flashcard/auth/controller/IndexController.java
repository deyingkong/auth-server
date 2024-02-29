package com.austin.flashcard.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 12:55 AM
 */
@Controller
public class IndexController {


    @GetMapping("/home")
    public String home(Model model){
        return "home";
    }

}
