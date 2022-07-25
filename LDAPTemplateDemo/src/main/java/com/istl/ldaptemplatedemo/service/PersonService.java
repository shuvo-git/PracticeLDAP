package com.istl.ldaptemplatedemo.service;

import com.istl.ldaptemplatedemo.dto.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonService {
    public static final String BASE_DN = "dc=rcms,dc=com";
    @Autowired
    private LdapTemplate ldapTemplate;

    public String create(Person p) throws Exception{
        ldapTemplate.bind(buildDn(p), null, buildAttributes(p));
        return p.getUsername() + " created successfully";
    }

    public String update(Person p) {
        Name dn = buildDn(p.getUsername());
        ldapTemplate.rebind(dn, null, buildAttributes(p));
        return p.getUsername() + " updated successfully";
    }

    public List<Person> retrieve() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<Person> people = ldapTemplate.search(query().where("objectclass").is("customuserclass"),
                new PersonAttributeMapper());
        return people;
    }


    public String remove(String userId) {
        Name dn = buildDn(userId);
        // ldapTemplate.unbind(dn, true); //Remove recursively all entries
        ldapTemplate.unbind(dn);
        return userId + " removed successfully";
    }

    public Name buildDn(Person p) {
        return LdapNameBuilder.newInstance()
                .add("ou","People")
                .add("uid", p.getUsername())
//                .add("cn", p.getFullname())
                //.add("uid", p.getUsername())
                //.add("sn",p.getLastname())
                .build();
    }

    public Name buildDn(String userId) {
        return LdapNameBuilder.newInstance()
                .add("ou","People")
                .add("uid", userId)
                //.add("sn",p.getLastname())
                .build();
    }

    private Attributes buildAttributes(Person p) {
        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("customuserclass");
        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put("uid", p.getUsername());
        //attrs.put("cn", p.getFullname());
        attrs.put("password", p.getLastname());
        attrs.put("accountstatus", p.getUsername());
        attrs.put("officeid", p.getUsername());
        attrs.put("usertype", p.getUsername());
        attrs.put("designation", p.getUsername());
        attrs.put("id", p.getUsername());
        //attrs.put("description", p.getDescription());
        return attrs;
    }

    private class PersonAttributeMapper implements AttributesMapper<Person> {
        @Override
        public Person mapFromAttributes(Attributes attributes) throws NamingException {
            Person person = new Person();
            person.setUsername(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
            person.setFullname(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
            person.setLastname(null != attributes.get("password") ? attributes.get("password").get().toString() : null);
            //person.setDescription(
            //        null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            return person;
        }
    }
}
