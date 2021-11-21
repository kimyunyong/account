package com.nandsoft.account.service;

import com.nandsoft.account.dao.UserDao;
import com.nandsoft.account.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public int insertUser(User user){
        int userCount = userDao.getUserCount(user);

        if(userCount == 0){
            if(user.getPassword() != null){
                user.setPassword("{bcrypt}" + encoder.encode(user.getPassword()));
            }

            user.setRole("ROLE_USER");
            userDao.insertUser(user);
            return 1;
        }

        return -1;
    }

    @Override
    public User getUser(User user) {
        return userDao.getUser(user.getUsername());
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public int idConfirm(User user) {
        return userDao.getUserCount(user);
    }

}