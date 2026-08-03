package com.security.authentication.config.Security;

import com.security.authentication.model.ERole;
import com.security.authentication.model.Role;
import com.security.authentication.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        Arrays.stream(ERole.values()).forEach(eRole -> {
            if (roleRepository.findByName(eRole).isEmpty()) {
                roleRepository.save(new Role(eRole));
                System.out.println(">>> SEEDED ROLE: " + eRole);
            }
        });
    }
}