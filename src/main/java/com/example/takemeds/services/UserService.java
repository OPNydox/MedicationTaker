package com.example.takemeds.services;

import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.UserPresentationModel;
import com.example.takemeds.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User createUser(UserPresentationModel model) {
        User newUser = new User();

        newUser.setEmail(model.getEmail());
        newUser.setName(model.getName());
        newUser.setPassword(model.getPassword());

        repository.save(newUser);

        return newUser;
    }

    public UserPresentationModel getUser(String email) {
        User foundUser = repository.findByEmail(email);

        return UserPresentationModel.builder().email(foundUser.getEmail())
                                              .name(foundUser.getName()).build();
    }

    public UserPresentationModel getUser(Long id) {
        User foundUser = repository.findById(id).get();

        return UserPresentationModel.builder().email(foundUser.getEmail())
                                              .name(foundUser.getName()).build();
    }
}
