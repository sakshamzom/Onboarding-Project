package com.example.sync.kafka;

import com.example.sync.exception.ResourceNotFoundException;
import com.example.sync.model.User;
import com.example.sync.model.UserWithReq;
import com.example.sync.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JsonKafkaConsumer {


    private static final Logger LOGGER= LoggerFactory.getLogger(JsonKafkaConsumer.class);
    public UserRepository userRepository;
    @KafkaListener(topics = "javaguides_json_2",groupId = "myGroup")
    public void consumer(UserWithReq ObjUser)
    {
        // Put
        String type=ObjUser.getType();
        User ToBeChanged=new User(ObjUser.getId(),ObjUser.getName(),ObjUser.getAddress());

        if(type=="save")
        {
            userRepository.save(ToBeChanged);
        }
        if(type=="GetAll")
        {
            List<User> UserList=userRepository.findAll();

        }
        if(type=="GetById")
        {
            int id=ToBeChanged.getId();
            User userFound= userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User","id",id));
        }
        if(type=="Update")
        {
            int id=ToBeChanged.getId();
            User existingUser=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","id",id));
            existingUser.setAddress(ToBeChanged.getAddress());
            existingUser.setName(ToBeChanged.getName());
            userRepository.save(existingUser);

        }
        if(type=="Delete")
        {
            int id=ToBeChanged.getId();
            User existingUser=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","id",id));
            userRepository.deleteById(id);
        }



    }
}
