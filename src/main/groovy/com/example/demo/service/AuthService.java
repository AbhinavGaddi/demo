package com.example.demo.service;

import com.example.demo.dao.DataInterface;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.model.Users;
import com.example.demo.schema.LoginDto;
import com.example.demo.schema.RegistrationDto;
import com.example.demo.schema.UnlockDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
@Service
public class AuthService {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private DataInterface dataInterface;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public AuthService(AuthMapper authMapper, PasswordEncoder passwordEncoder, DataInterface dataInterface) {
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.dataInterface = dataInterface;
    }

    public Users registerUser(RegistrationDto registrationDto){
        Optional<Users> users = dataInterface.findByuserName(registrationDto.getUserName());
        if(users.isPresent()){
            throw new RuntimeException("Username Already Exists");

        }
        Users user = authMapper.registrationDtotoUser(registrationDto);
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        return dataInterface.save(user);

    }
    @Transactional
    public String login(@Valid LoginDto loginDto) {
        Optional<Users> users = dataInterface.findByuserName(loginDto.getUserName());
        if(!users.isPresent()){
            return "Invalid Username";
        }
        Users user = users.get();
        if (user.isLocked()) {
            return "Account is Locked due to too many failed attempts";
        }
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            int attempts = user.getLoginAttempts()+1;
            user.setLoginAttempts(attempts);
            if(attempts >= MAX_LOGIN_ATTEMPTS){
                user.setLocked(true);
                dataInterface.saveAndFlush(user);
                return "More than 3 failed attempts . User profile is locked . Please contact customer service";
            }
            dataInterface.saveAndFlush(user);
            return "Incorrect password. Attempt #" + attempts + "of " + MAX_LOGIN_ATTEMPTS +".";
        }
        if (user.getLoginAttempts() > 0){
            user.setLoginAttempts(0);
            dataInterface.saveAndFlush(user);
        }
        return "Login Successful";
    }

    public boolean unlockUser(String userName) {
        Optional<Users> users = dataInterface.findByuserName(userName);
        if(users.isEmpty()){
            throw new RuntimeException("Invalid Username");
        }
        Users user = users.get();
        user.setLocked(false);
        user.setLoginAttempts(0);
        dataInterface.saveAndFlush(user);
        return true;
    }

    }

