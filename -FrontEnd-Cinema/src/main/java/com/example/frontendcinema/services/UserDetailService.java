package com.example.frontendcinema.services;

import com.example.frontendcinema.pojoes.User;
import com.example.frontendcinema.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.frontendcinema.details.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        UserDetails userDetails = new com.example.frontendcinema.details.UserDetails( user );
        return userDetails;
    }
}
