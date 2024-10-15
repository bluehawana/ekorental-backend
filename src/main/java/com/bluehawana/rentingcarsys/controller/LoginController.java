package com.bluehawana.rentingcarsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/github";
    }

    @GetMapping("/login/githubAuth")
    public String githubLogin() {
        return "redirect:/oauth2/authorization/github";
    }

    @GetMapping("/login/googleAuth")
    public String googleLogin() {
        return "redirect:/oauth2/authorization/google";
    }
}
