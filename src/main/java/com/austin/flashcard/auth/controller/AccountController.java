package com.austin.flashcard.auth.controller;

import com.austin.flashcard.auth.constant.Common;
import com.austin.flashcard.auth.entity.User;
import com.austin.flashcard.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 12:55 AM
 */
@Slf4j
@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpRequest;


    @GetMapping("/signIn")
    public String signIn(Model model){

        String errorMsg = getLoginErrorMessage(httpRequest);
        log.info("error message:{}", errorMsg);
        if(!StringUtils.isEmpty(errorMsg)){
            model.addAttribute("error", errorMsg);
        }
        model.addAttribute(Common.PAGE_TITLE_KEY, "Sign-in");
        return "login";
    }

    @GetMapping("/signUp")
    public String openSignUpPage(Model model){
        model.addAttribute(Common.PAGE_TITLE_KEY, "Sign-up");
        return "signup";
    }

    @PostMapping("/signUpProcess")
    public String doSignUp(@RequestParam(name = "email", required = true) String email,
                           @RequestParam(name = "username", required = true) String username,
                           @RequestParam(name = "password", required = true) String password,
                           Model model){
        log.info("email:{},username:{},password:{}", email, username, password);
        if(userService.isUserExist(email)){
            //throw new UserExistException(email);
            model.addAttribute("error", "email address is already exist");
            return "signup";
        }
        userService.registerUser(email.trim(), username.trim(), password.trim());
        return "redirect:/signIn";
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model){
        var email = UserService.retrieveEmailFromAuthentication(authentication);
        log.info("user {} is viewing his/her profile", email);

        Optional<User> userOptional = userService.findUserByEmail(email);
        if(userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        }
        model.addAttribute(Common.PAGE_TITLE_KEY, "Profile");
        return "profile";
    }

    private String getLoginErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) instanceof AuthenticationException exception) {
            log.error("", exception);
            return exception.getMessage();
        }
        return null;
    }

}
