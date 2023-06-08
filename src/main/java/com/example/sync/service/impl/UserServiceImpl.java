package com.example.sync.service.impl;

import com.example.sync.exception.ResourceNotFoundException;
import com.example.sync.model.User;
import com.example.sync.repository.UserRepository;
import com.example.sync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);

    }

    @Override
    public List<User> getAllUsers() {
       return userRepository.findAll();
    }

    @Override
    public User getElementbyId(int id) {
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User","id",id));
    }

    @Override
    public User updateUserById(User newUser, int id) {
        User existingUser=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","id",id));
        existingUser.setAddress(newUser.getAddress());
        existingUser.setName(newUser.getName());
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteUserById(int id) {
        User existingUser=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","id",id));
        userRepository.deleteById(id);

    }
}
