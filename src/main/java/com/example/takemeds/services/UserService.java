package com.example.takemeds.services;

import com.example.takemeds.configurations.SecurityConfig;
import com.example.takemeds.entities.Role;
import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.UserPresentationModel;
import com.example.takemeds.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;

        roleService.createRole("USER");
        UserPresentationModel baseUser = new UserPresentationModel.UserPresentationModelBuilder().name("user")
                .username("user").password("123").role("USER").build();

        createUser(baseUser);
    }

    public User createUser(UserPresentationModel model) {
        User newUser = new User();

        newUser.setEmail(model.getUsername());
        newUser.setName(model.getName());
        newUser.setPassword(passwordEncoder.encode(model.getPassword()));
        newUser.setRole(roleService.findRole("USER"));

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

        if (foundUser == null) {
            throw new UsernameNotFoundException("User with username " + username + "not found.");
        }

        return UserPresentationModel.builder().username(foundUser.getEmail())
                                                                 .name(foundUser.getName())
                                                                 .role(foundUser.getRole().toString())
                                                                 .password(foundUser.getPassword()).build();
    }
}
