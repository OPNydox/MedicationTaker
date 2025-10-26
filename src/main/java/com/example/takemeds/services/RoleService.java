package com.example.takemeds.services;

import com.example.takemeds.entities.Role;
import com.example.takemeds.presentationModels.RolePresentationModel;
import com.example.takemeds.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRole(String name) throws RoleNotFoundException {
        Optional<Role> role = roleRepository.findByName(name);

        if (role.isEmpty()) {
            throw new RoleNotFoundException("Role with name " + name + " does not exist.");
        }

        return role.get();
    }

    public Role createRole(String name) {
        Role role = new Role();
        role.setName(name);

        return this.roleRepository.saveAndFlush(role);
    }
}
