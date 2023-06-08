package com.example.sync.controller;

import com.example.sync.kafka.JsonKafkaProducer;
import com.example.sync.model.User;
import com.example.sync.model.UserWithReq;
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
@RequestMapping("/api/async/users")
public class Async_Controller {

    @Autowired
    private UserService userService;
    private JsonKafkaProducer jsonKafkaProducer;

    public Async_Controller(UserService userService,JsonKafkaProducer jsonKafkaProducer) {
        super();
        this.userService = userService;
        this.jsonKafkaProducer = jsonKafkaProducer;

    }

    @PostMapping()
    public ResponseEntity<User> saveUser(@RequestBody User user){
        UserWithReq newObj=new UserWithReq(user.getId(),user.getName(),user.getAddress(),"save");
        jsonKafkaProducer.sendMessage(newObj);
        return  new ResponseEntity<User>(userService.saveUser(user), HttpStatus.CREATED);

    }

    @GetMapping()
    public List<User> getALLUsers(){
        UserWithReq newObj=new UserWithReq();
        newObj.setType("GetAll");
        jsonKafkaProducer.sendMessage(newObj);
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    @Cacheable(value = "users",key = "#id")
    public ResponseEntity<User> getUserById(@PathVariable("id") int Userid){
        UserWithReq newObj=new UserWithReq();
        newObj.setType("GetById");
        newObj.setId(Userid);
        jsonKafkaProducer.sendMessage(newObj);
        return new ResponseEntity<User>(userService.getElementbyId(Userid),HttpStatus.OK);
    }

    @PutMapping("{id}")
    @CachePut(value = "users",key = "#id")
    public ResponseEntity<User>updateUser(@PathVariable("id") int UserId,@RequestBody User user)
    {
        UserWithReq newObj=new UserWithReq(user.getId(),user.getName(),user.getAddress(),"Update");
        jsonKafkaProducer.sendMessage(newObj);
        return new ResponseEntity<User>(userService.updateUserById(user,UserId),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<String>updateUser(@PathVariable("id") int UserId)
    {
        UserWithReq newObj=new UserWithReq();
        newObj.setType("Delete");
        newObj.setId(UserId);
        jsonKafkaProducer.sendMessage(newObj);
        userService.deleteUserById(UserId);
        return new ResponseEntity<String>("Deleted",HttpStatus.OK);
    }

}
