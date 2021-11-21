package com.nandsoft.account.security;

import com.nandsoft.account.controller.AccountController;
import com.nandsoft.account.model.User;
import com.nandsoft.account.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        PrincipalDetail principalDetail = userDetailsService.loadUserByUsername(username);
        log.info("==============================>", "가져온 아이디 : " + principalDetail.getUsername() + ", 로그인 아이디 : " + username );
        log.info("==============================>", "가져온 패스워드 : " + principalDetail.getPassword() + ", 로그인 패스워드 : " + password );

        // 비밀번호가 같은지 체크
        if(!matchPassword(password, principalDetail.getPassword())) {
            throw new BadCredentialsException(username);
        }

        if(!principalDetail.isEnabled()) {
            throw new BadCredentialsException(username);
        }

        // 롤처리는 하지 않음.
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username, password, principalDetail.getAuthorities());

        result.setDetails(principalDetail);

        return result;
    }

    private boolean matchPassword(String loginPwd, String password) {
        return loginPwd.equals(password);
    }
}
