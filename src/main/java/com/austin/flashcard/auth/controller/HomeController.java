package com.austin.flashcard.auth.controller;

import lombok.extern.slf4j.Slf4j;
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
    @RequestMapping("/")
    public String index(Model model){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        return "home";
    }
}
