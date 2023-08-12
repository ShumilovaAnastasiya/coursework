package com.example.frontendcinema.controllers;

import com.example.frontendcinema.dto.UserRegistrationDTO;
import com.example.frontendcinema.pojoes.User;
import com.example.frontendcinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void interceptor(Authentication authentication, Model model) {
        if (authentication != null) {
            String role = ((User) authentication.getPrincipal()).getStatus();
            model.addAttribute("admin", role.equals("ADMIN"));
        } else {
            model.addAttribute("admin", false);
        }
    }

    @GetMapping
    public String main() {
        return "main";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("registration")
    public String doLogin(UserRegistrationDTO userRegistrationDTO, Model model) {
        ResponseEntity registrate = userService.registrate(userRegistrationDTO);
        model.addAttribute("notification", registrate.getBody().toString());
        model.addAttribute("notificationClass", registrate.getStatusCodeValue() == 200? "success": "error");
        return (registrate.getStatusCodeValue() == 200)? "login": "registration";
    }

    @GetMapping("registration")
    public String registration() {
        return "registration";
    }

    @GetMapping("movies")
    public String movies() {
        return "movies";
    }

    @GetMapping("cashiers")
    public String cashiers() {
        return "cashiers";
    }

    @GetMapping("viewers")
    public String viewers() {
        return "viewers";
    }

    @GetMapping("sales")
    public String sales() {
        return "sales";
    }

    @GetMapping("about")
    public String about() {
        return "about_author";
    }

}
