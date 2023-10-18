package com.projectmicrosoft.microsoft.api.html;

import com.projectmicrosoft.microsoft.api.security.AuthenticatedUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class index {
    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @AuthenticatedUser(requiredRole = "ADMIN")
    @GetMapping("/sla")
    public String sla() {
        return "Está authenticado";
    }
}
