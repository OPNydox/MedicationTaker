package com.example.takemeds.services;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.MedicationPresentationModel;
import com.example.takemeds.presentationModels.RegistrationPresentationModel;
import com.example.takemeds.presentationModels.RolePresentationModel;
import com.example.takemeds.presentationModels.UserPresentationModel;
import com.example.takemeds.repositories.UserRepository;
import com.example.takemeds.utils.mappers.MedicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final MedicationMapper medicationMapper;

    @Autowired
    public UserService(UserRepository repository, RoleService roleService, PasswordEncoder passwordEncoder, MedicationMapper medicationMapper) {
        this.roleService = roleService;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.medicationMapper = medicationMapper;

        roleService.createRole("USER");

        RegistrationPresentationModel baseUser = new RegistrationPresentationModel("user", "user", "123");

        createUser(baseUser);
    }

    public User createUser(RegistrationPresentationModel model) {
        User newUser = new User();

        newUser.setEmail(model.getEmail());
        newUser.setName(model.getName());
        newUser.setPassword(passwordEncoder.encode(model.getPassword()));
        newUser.setRole(roleService.findRole("USER"));

        repository.save(newUser);

        return newUser;
    }

    public User getUser(String username) {
        User foundUser = repository.findByEmail(username);

        if (foundUser == null) {
            throw new UsernameNotFoundException("User with username " + username + "not found.");
        }

        return foundUser;
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
                                                                 .roles(List.of(new RolePresentationModel(foundUser.getRole().toString())))
                                                                 .password(foundUser.getPassword()).build();
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public List<MedicationPresentationModel> showMedicationForUser(String username) {
        User user = getUser(username);
        List<Medication> medications = user.getMedicationToTake();

        return medicationMapper.mapMedicationsToPM(medications);
    }
}
