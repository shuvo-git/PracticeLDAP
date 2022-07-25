package com.istl.ldaptemplatedemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LdapUserService {
    private final LdapTemplate ldap;

    public boolean authenticate(String username, String password) {


//        List<String> ldapId = ldap.search(LdapQueryBuilder.query().base("ou=People").where("designation").like("*," + "DG" + ",*").and("accountstatus").is("1"), new UserAttributesLdapIdMapper());
//        if (ldapId != null && !ldapId.isEmpty()) {
//            String ldapData = ldapId.get(0);
//            System.out.println("ldapData: "+ldapData);
//        }
//        return true;


//        if(ldap.getContextSource()!=null){
//            return true;
//        } else {
//            return false;
//        }

        DirContext dirContext = ldap.getContextSource().getContext(
            "cn=" + username + ",ou=People," , //+ env.getRequiredProperty("ldap.partitionSuffix"),
            password
        );

        return dirContext!=null;
    }

    private class UserAttributesLdapIdMapper implements AttributesMapper<String> {
        @Override
        public String mapFromAttributes(Attributes attributes) throws NamingException {
            return attributes.get("id").get().toString();
        }
    }
}
