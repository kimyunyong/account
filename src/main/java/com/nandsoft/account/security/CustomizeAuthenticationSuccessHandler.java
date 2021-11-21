package com.nandsoft.account.security;

import com.nandsoft.account.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        /*System.out.println(" test :: 로그인 성공 !!");*/
        /*userDao.inserLog();*/
        req.getUserPrincipal();

        String password = req.getParameter("password");

        res.sendRedirect(req.getContextPath() + "/");
    }
}
