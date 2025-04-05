package com.example.takemeds.presentationModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserPresentationModel implements UserDetails {
    String username;

    String name;

    String password;

    List<RolePresentationModel> roles;

    public static UserPresentationModelBuilder builder() {
        return new UserPresentationModelBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public static class UserPresentationModelBuilder {
        private String username;
        private String name;
        private String password;
        private List<RolePresentationModel> roles;

        public UserPresentationModelBuilder() {
        }

        public UserPresentationModelBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserPresentationModelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserPresentationModelBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserPresentationModelBuilder roles(List<RolePresentationModel> roles) {
            this.roles = roles;
            return this;
        }

        public UserPresentationModel build() {
            return new UserPresentationModel(this.username, this.name, this.password, this.roles);
        }

        public String toString() {
            return "UserPresentationModel.UserPresentationModelBuilder(username=" + this.username + ", name=" + this.name + ")";
        }
    }

    public UserPresentationModel(String username, String name, String password, List<RolePresentationModel> role) {
        setUsername(username);
        setName(name);
        setPassword(password);
        setRoles(role);
    }
}
