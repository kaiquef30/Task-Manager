package com.projectmicrosoft.microsoft.api.html;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class signup {


    @GetMapping("/signup")
    public String signup() {
        return "signup.html";
    }
}
