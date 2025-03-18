package com.example.takemeds.services;

import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.UserPresentationModel;
import com.example.takemeds.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    public User createUser(UserPresentationModel model) {
        User newUser = new User();

        newUser.setEmail(model.getUsername());
        newUser.setName(model.getName());
        newUser.setPassword(model.getPassword());

        repository.save(newUser);

        return newUser;
    }

    public UserPresentationModel getUser(String email) {
        User foundUser = repository.findByEmail(email);

        return UserPresentationModel.builder().username(foundUser.getEmail())
                                              .name(foundUser.getName()).build();
    }

    public UserPresentationModel getUser(Long id) {
        User foundUser = repository.findById(id).get();

        return UserPresentationModel.builder().username(foundUser.getEmail())
                                              .name(foundUser.getName()).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = repository.findByEmail(username);

        return UserPresentationModel.builder().username(foundUser.getEmail())
                                                                 .name(foundUser.getName())
                                                                 .role(foundUser.getRole().toString())
                                                                 .password(foundUser.getPassword()).build();
    }
}
