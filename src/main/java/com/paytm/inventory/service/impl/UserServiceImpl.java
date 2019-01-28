package com.paytm.inventory.service.impl;

import com.paytm.inventory.exception.AuthorizationException;
import com.paytm.inventory.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paytm.inventory.repository.UserRepository;
import com.paytm.inventory.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserDetails(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new
                AuthorizationException("SMS-403","Forbidden"));
    }
}
