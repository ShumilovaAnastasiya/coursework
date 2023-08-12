package com.example.frontendcinema.services;

import com.example.frontendcinema.dto.UserRegistrationDTO;
import com.example.frontendcinema.pojoes.User;
import com.example.frontendcinema.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity registrate(UserRegistrationDTO userRegistrationDTO) {
        if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getRepeatPassword())) {
            return new ResponseEntity("Пароли не совпадают", HttpStatus.BAD_REQUEST);
        } else if (alreadyExists(userRegistrationDTO.getUsername())) {
            return new ResponseEntity("Такой аккаунт уже существует", HttpStatus.BAD_REQUEST);
        }

        User user = new User(userRegistrationDTO.getUsername(), userRegistrationDTO.getPassword(), "USER");
        System.out.println(user);
        userRepository.save(user);
        return new ResponseEntity("'"+user.getUsername()+"' зарегистрирован в справочной системе", HttpStatus.OK);
    }

    private boolean alreadyExists(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

}
