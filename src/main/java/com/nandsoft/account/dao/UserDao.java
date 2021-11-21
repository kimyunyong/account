package com.nandsoft.account.dao;

import com.nandsoft.account.model.User;

public interface UserDao {
    public void insertUser(User user);
    public int getUserCount(User user);
    public User getUser(String username);
    public void updateUser(User user);
}
