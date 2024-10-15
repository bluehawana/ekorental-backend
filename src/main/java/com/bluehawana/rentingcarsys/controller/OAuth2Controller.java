package com.bluehawana.rentingcarsys.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    @GetMapping("/oauth2")
    public String oauth2Info() {
        return "OAuth2 Controller is working.";
    }
}
