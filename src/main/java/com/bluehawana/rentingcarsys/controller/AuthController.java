package com.bluehawana.rentingcarsys.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class AuthController {

    @GetMapping("/login/github")
    public RedirectView githubLogin() {
        return new RedirectView("/oauth2/authorization/github");
    }

    @GetMapping("/login/google")
    public RedirectView googleLogin() {
        return new RedirectView("/oauth2/authorization/google");
    }
}
