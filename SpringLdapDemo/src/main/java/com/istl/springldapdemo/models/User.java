package com.istl.springldapdemo.models;

import lombok.*;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(base = "ou=People", objectClasses = { "person", "inetOrgPerson", "top" })
@Getter
@Setter
@NoArgsConstructor
public final class User {
    @Id
    private Name id;

    private @Attribute(name = "cn") String username;
    private @Attribute(name = "sn") String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
