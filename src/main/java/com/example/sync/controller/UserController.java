package com.example.sync.controller;

import com.example.sync.model.User;
import com.example.sync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    public UserController(UserService userService) {
        super();
        this.userService = userService;

    }

    @PostMapping()
    public ResponseEntity<User> saveUser(@RequestBody User user){

        return  new ResponseEntity<User>(userService.saveUser(user), HttpStatus.CREATED);

    }

    @GetMapping()

    public List<User>getALLUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    @Cacheable(value = "users",key = "#id")
    public ResponseEntity<User> getUserById(@PathVariable("id") int Userid){
        return new ResponseEntity<User>(userService.getElementbyId(Userid),HttpStatus.OK);
    }

    @PutMapping("{id}")
    @CachePut(value = "users",key = "#id")
    public ResponseEntity<User>updateUser(@PathVariable("id") int UserId,@RequestBody User newUser)
    {
     return new ResponseEntity<User>(userService.updateUserById(newUser,UserId),HttpStatus.OK);
    }

    @CacheEvict(value = "users", allEntries = true)
    @DeleteMapping("{id}")
    public ResponseEntity<String>updateUser(@PathVariable("id") int UserId)
    {
        userService.deleteUserById(UserId);
        return new ResponseEntity<String>("Deleted",HttpStatus.OK);
    }

}
