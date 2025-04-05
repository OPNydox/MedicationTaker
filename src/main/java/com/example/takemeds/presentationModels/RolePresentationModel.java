package com.example.takemeds.presentationModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class RolePresentationModel implements GrantedAuthority {

    private String role;

    @Override
    public String getAuthority() {
        return role;
    }

    public RolePresentationModel(String role) {
        this.role = role;
    }
}
