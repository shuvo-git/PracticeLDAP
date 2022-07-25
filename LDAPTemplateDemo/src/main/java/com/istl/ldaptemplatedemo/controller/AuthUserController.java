package com.istl.ldaptemplatedemo.controller;

import com.istl.ldaptemplatedemo.dto.Person;
import com.istl.ldaptemplatedemo.service.LdapUserService;
import com.istl.ldaptemplatedemo.service.PersonService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthUserController {

    private final LdapUserService ldapUserService;
    private final PersonService personService;

    @PostMapping("/authenticate")
    public boolean userAuthenticate(@RequestBody AuthRequest authRequest){
        return ldapUserService.authenticate(authRequest.username,authRequest.password);
    }

    @PostMapping("/create")
    public String userCreate(@RequestBody Person person) throws Exception{
        return personService.create(person);
    }

    @PostMapping("/update")
    public String userUpdate(@RequestBody Person person) throws Exception{
        return personService.update(person);
    }

    @GetMapping("/list")
    public List<Person> retrieve() {
        return personService.retrieve();
    }

    @DeleteMapping("/{id}")
    public String retrieve(@PathVariable String id) {
        return personService.remove(id);
    }



    @NoArgsConstructor
    @Getter
    @Setter
    private static class AuthRequest {
        String username;
        String password;
    }
}
