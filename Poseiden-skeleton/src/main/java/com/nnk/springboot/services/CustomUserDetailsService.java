package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userService.findByUsername(username);
        if(opt.isPresent()){
            User user = opt.get();
            return user;

        }else{
            throw new UsernameNotFoundException("User not found");
        }
    }

}
