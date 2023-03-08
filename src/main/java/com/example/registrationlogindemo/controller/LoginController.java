package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.dto.SignInRequest;
import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.UserService;
import com.example.registrationlogindemo.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import com.example.registrationlogindemo.enums.Gender;


import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;

    SignInRequest request;
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public RedirectView submitLoginForm(SignInRequest request, HttpSession session) {
        String email = request.getEmail();
        String password = request.getPassword();
        log.info("girildi post");
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), userService.findByEmail(email).getPassword());
        log.info(userService.findByEmail(email).getEmail());
        log.info(password);
        log.info(userService.findByEmail(email).getPassword());
//        log.info("girildi post " +  passwordEncoder.encode(password));
        if (isAuthenticated) {
            Long id = userService.findByEmail(email).getId();
            String username = userService.findByEmail(email).getFirstname();
            String firstname = userService.findByEmail(email).getFirstname();
            String sesEmail = userService.findByEmail(email).getEmail();
            String surname = userService.findByEmail(email).getSurname();
            String nickname = userService.findByEmail(email).getNickname();
            String location = userService.findByEmail(email).getLocation();
            Gender gender = userService.findByEmail(email).getGender();
            log.info("giriş doğruysa, kullanıcıyı bir sonraki sayfaya yönlendirin");
            session.setAttribute("userId", id);
            session.setAttribute("username", username); // store the username in the session
            session.setAttribute("firstname", firstname); // store the username in the session
            session.setAttribute("sesEmail", sesEmail);
            session.setAttribute("surname", surname); // store the username in the session
            session.setAttribute("nickname", nickname);
            session.setAttribute("location", location);
            session.setAttribute("gender", gender);
            return new RedirectView("/mainpage");

        } else {
            log.info("yanlış giriş yapıldığını bildiren bir hata mesajı gösterin");
            return new RedirectView("login");
            //           "/login?error=true"
        }
    }
}
