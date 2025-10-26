package com.example.takemeds.utils.inlitializers;

import com.example.takemeds.presentationModels.RegistrationPresentationModel;
import com.example.takemeds.services.RoleService;
import com.example.takemeds.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;

    private final UserService userService;

    public DataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.createRole("PATIENT");
        roleService.createRole("DOCTOR");

        RegistrationPresentationModel baseUser = new RegistrationPresentationModel("user", "user", "123");

        userService.createUser(baseUser, "PATIENT");
    }
}
