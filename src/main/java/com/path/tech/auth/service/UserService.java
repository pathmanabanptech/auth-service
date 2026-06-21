package com.path.tech.auth.service;

import com.path.tech.auth.dto.UserSignupRequest;
import com.path.tech.auth.model.UserProfile;
import com.path.tech.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public UserProfile save(UserSignupRequest userSignupRequest) {
        UserProfile profile = new UserProfile();
        profile.setFirstName(userSignupRequest.getFirstName());
        profile.setLastName(userSignupRequest.getLastName());
        profile.setSex(userSignupRequest.getSex());
        profile.setPhoneNumber(userSignupRequest.getPhoneNumber());
        profile.setDob(userSignupRequest.getDob());
        profile.setRole(userSignupRequest.getRole());
        return repository.save(profile);

    }
    public UUID getUserIdByPhoneNumber(String phoneNumber) {
        return repository.findUserIdByPhoneNumber(phoneNumber);
    }
    
    public List findAll() {            
        System.out.println("hello findall!");   
        return repo.findAll();
    }
}
