package com.example.Login.services;

import com.example.Login.model.MyAppUser;
import com.example.Login.repo.MyAppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MyAppUserService implements UserDetailsService {

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MyAppUser> myAppUser = myAppUserRepository.findByUsername(username);
        if(myAppUser.isPresent()){
            var userObjet = myAppUser.get();
            return User.builder()
                    .username(userObjet.getUsername())
                    .password(userObjet.getPassword())
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
