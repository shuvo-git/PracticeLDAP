package com.istl.springsecurityldap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ldap")
public class AppController {

    @GetMapping("/security")
    public String securityMethod(){
        return "Secure rest endpoints with ldap.";
    }
}
