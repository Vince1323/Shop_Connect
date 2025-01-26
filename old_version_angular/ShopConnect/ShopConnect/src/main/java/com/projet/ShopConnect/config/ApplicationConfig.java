package com.projet.ShopConnect.config;

import com.projet.ShopConnect.model.Enum;
import com.projet.ShopConnect.model.Role;
import com.projet.ShopConnect.repository.RoleRepository;
import com.projet.ShopConnect.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;
    private final RoleRepository roleRepository;


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(" A REVOIR"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @Transactional
    public CommandLineRunner initializeRoles() {
        return args -> {
            String adminRoleName = Enum.UserRole.ADMIN.toString();
            if(roleRepository.findRoleByName(adminRoleName).isEmpty()) {
                Role adminRole = Role.builder()
                        .name(adminRoleName)
                        .deleted(false)
                        .build();
                roleRepository.save(adminRole);
            }
            String userRoleName = Enum.UserRole.USER.toString();
            if(roleRepository.findRoleByName(userRoleName).isEmpty()) {
                Role userRole = Role.builder()
                        .name(userRoleName)
                        .deleted(false)
                        .build();
                roleRepository.save(userRole);
            }
        };
    }

}
