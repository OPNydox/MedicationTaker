package com.example.takemeds.presentationModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPresentationModel {

    String email;

    String name;

    String password;

    public static UserPresentationModelBuilder builder() {
        return new UserPresentationModelBuilder();
    }

    public static class UserPresentationModelBuilder {
        private String email;
        private String name;
        private String password;

        UserPresentationModelBuilder() {
        }

        public UserPresentationModelBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserPresentationModelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserPresentationModelBuilder password(String name) {
            this.name = name;
            return this;
        }

        public UserPresentationModel build() {
            return new UserPresentationModel(this.email, this.name, password);
        }

        public String toString() {
            return "UserPresentationModel.UserPresentationModelBuilder(email=" + this.email + ", name=" + this.name + ")";
        }
    }

    public UserPresentationModel(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
