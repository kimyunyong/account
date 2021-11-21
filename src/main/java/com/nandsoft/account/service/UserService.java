package com.nandsoft.account.service;

import com.nandsoft.account.model.User;

public interface UserService {
    public int insertUser(User user);
    public User getUser(User user);
    public void updateUser(User user);
    public int idConfirm(User user);
}
