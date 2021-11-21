package com.nandsoft.account.security;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.nandsoft.account.dao.UserDao;
import com.nandsoft.account.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    public PrincipalDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUser(username);

        if(user==null) {
            throw new UsernameNotFoundException(username);
        }

        return new PrincipalDetail(user);
    }
}
