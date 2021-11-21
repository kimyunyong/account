package com.nandsoft.account.dao;

import com.nandsoft.account.model.User;
import com.nandsoft.account.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoImpl implements UserDao {
    @Autowired
    private SqlSessionTemplate session;

    @Override
    public void insertUser(User user) {
        session.insert("UserDao.insertUser", user);
    }

    @Override
    public int getUserCount(User user) {
        return session.selectOne("UserDao.getUserCount", user);
    }

    @Override
    public User getUser(String username) {
        return session.selectOne("UserDao.getUser", username);
    }

    @Override
    public void updateUser(User user) {
        session.update("UserDao.updateUser", user);
    }

}
