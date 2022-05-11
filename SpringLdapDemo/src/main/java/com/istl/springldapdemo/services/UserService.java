package com.istl.springldapdemo.services;

import com.istl.springldapdemo.models.User;
import com.istl.springldapdemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;

import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;

    private final LdapTemplate ldapTemplate;

    @Autowired
    public UserService(UserRepository repository, LdapTemplate ldapTemplate) {
        this.repository = repository;
        this.ldapTemplate = ldapTemplate;
    }


    public Boolean authenticate(String u, String p) {
        return repository.findByUsernameAndPassword(u, p) != null;
    }

    public void create(String username, String password) {
        User newUser = new User(username, password); //digestSHA(password)
        newUser.setId(LdapUtils.emptyLdapName());
        /*User newUser = User.builder()
                .username(username)
                .password(password)
                .id(LdapUtils.emptyLdapName())
                .build();*/
        repository.save(newUser);
    }

    private void saveInLdap(String id, UserType userType, String userId,
                            Integer countryId, String additionalCountry, Integer agency,
                            String encodedPassword, AccountStatus status,
                            String designation, String officeId, Integer otpFlag, Integer otpType, String contact) {
        Attribute objectClass = new BasicAttribute("objectClass");
        {
            objectClass.add("top");
            objectClass.add("customuserclass");
        }
        Attributes userAttributes = new BasicAttributes();
        userAttributes.put(objectClass);
        userAttributes.put("id", id);
        userAttributes.put("usertype", userType.name());
        userAttributes.put("uid", userId);
        userAttributes.put("password", encodedPassword);
        userAttributes.put("contactnumber", contact);
        userAttributes.put("otpflag", Integer.toString(otpFlag));
        userAttributes.put("otptype", Integer.toString(otpType));
        if (Utils.isOk(countryId)) {
            if (Utils.isOk(additionalCountry)) {
                userAttributes.put("country", additionalCountry);
            } else {
                userAttributes.put("country", Integer.toString(countryId));
            }
        } else {
            userAttributes.put("country", String.valueOf(0));
        }
        if (Utils.isOk(agency)) {
            userAttributes.put("agency", Integer.toString(agency));
        } else {
            userAttributes.put("agency", String.valueOf(0));
        }

        userAttributes.put("accountstatus", Integer.toString(status.getValue()));
        userAttributes.put("designation", designation);
        if (Utils.isOk(officeId)) {
            userAttributes.put("officeid", officeId);
        } else {
            userAttributes.put("officeid", String.valueOf(0));
        }
        userAttributes.put("firstlogin", String.valueOf(0));
        try {
            ldapTemplate.bind(bindDN(userId), null, userAttributes);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("Error in saving user in User Storage");
        }
    }

    public void modify(String u, String p) {
        User user = repository.findByUsername(u);
        user.setPassword(p);
        repository.save(user);
    }

    public List<String> search(String u) {
        List<User> userList = repository
                .findByIdLikeIgnoreCase(u);

        if (userList == null) {
            return Collections.emptyList();
        }

        return userList.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }
}
