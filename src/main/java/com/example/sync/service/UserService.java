package com.example.sync.service;

import com.example.sync.model.User;

import java.util.List;

public interface UserService {
    public User saveUser(User user);
    public List<User>getAllUsers();
    public User getElementbyId(int id);

    public User updateUserById(User newUser,int id);
    public void deleteUserById(int id);
}
