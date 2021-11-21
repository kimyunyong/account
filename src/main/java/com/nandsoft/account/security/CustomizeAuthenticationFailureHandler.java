package com.nandsoft.account.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private BCryptPasswordEncoder encoder;


    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        encoder = new BCryptPasswordEncoder();

        res.sendRedirect(req.getContextPath() + "/login?error=true");
    }
}
