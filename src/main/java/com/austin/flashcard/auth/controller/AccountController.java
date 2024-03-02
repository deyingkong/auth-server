package com.austin.flashcard.auth.controller;

import com.austin.flashcard.auth.entity.User;
import com.austin.flashcard.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return "login";
    }

    @GetMapping("/signUp")
    public String openSignUpPage(Model model){

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
    public String profilePage(Model model){
        org.springframework.security.core.userdetails.User securityUser = userService.retrieveUserFromContext();
        log.info("user {} is viewing his/her profile", securityUser.getUsername());

        Optional<User> userOptional = userService.findUserByEmail(securityUser.getUsername());
        if(userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        }
        return "profile";
    }

    private String getLoginErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        if (!(session
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) instanceof AuthenticationException exception)) {
            log.info("", session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
            return "Invalid credentials";
        }
        if (!org.springframework.util.StringUtils.hasText(exception.getMessage())) {
            return null;
        }
        return exception.getMessage();
    }


}
