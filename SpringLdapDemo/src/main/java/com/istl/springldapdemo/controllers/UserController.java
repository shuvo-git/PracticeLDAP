package com.istl.springldapdemo.controllers;

import com.istl.springldapdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.directory.DirContext;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final LdapTemplate ldapTemplate;


    @Autowired
    public UserController(UserService userService, LdapTemplate ldapTemplate) {
        this.userService = userService;
        this.ldapTemplate = ldapTemplate;
    }

    @PostMapping("/authenticate")
    public boolean authenticateUser(@RequestParam("username") final String username, @RequestParam("password") final String password){
        //return userService.authenticate(username,password);
        System.out.println(username);
        System.out.println(password);
        DirContext context = ldapTemplate.getContextSource().getContext(
                        "cn=" +
                                username +
                                ",ou=People"
                                //+env.getRequiredProperty("ldap.partitionSuffix")

                                , password);
        if(context==null)return false;
        else return true;
    }

    @PostMapping("/create")
    public boolean createUser(@RequestParam("username") final String username, @RequestParam("password") final String password){
        userService.create(username,password);
        return true;
    }

    @PostMapping("/modify")
    public boolean modifyUser(@RequestParam("username") final String username, @RequestParam("password") final String password){
        userService.modify(username,password);
        return true;
    }

    @PostMapping("/search")
    public List<String> searchUser(@RequestParam("username") final String username){
        //return userService.search(username);
        return ldapTemplate
                .search(
                        "ou=People",
                        "cn=" + username,
                        (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }
}
