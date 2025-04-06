package com.example.takemeds.presentationModels;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationPresentationModel {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z ]{2,50}$", message = "Name must be 3–50 alphabetic characters")
    private String name;

    @NotBlank(message = "Email is requred")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    public RegistrationPresentationModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public RegistrationPresentationModel(String name, String username) {
        this.name = name;
        this.email = username;
    }

    public RegistrationPresentationModel() {
    }
}
