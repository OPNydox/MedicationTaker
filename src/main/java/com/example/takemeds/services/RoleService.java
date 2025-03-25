package com.example.takemeds.services;

import com.example.takemeds.entities.Role;
import com.example.takemeds.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRole(String name) {
        return roleRepository.findByName(name);
    }

    public Role createRole(String name) {
        Role role = new Role();
        role.setName(name);

        return this.roleRepository.saveAndFlush(role);
    }
}
