package com.spring.home_solver.config;

import com.spring.home_solver.entity.User;
import com.spring.home_solver.enumulation.Role;
import com.spring.home_solver.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner runner() {
        return runner -> {
            User user = new User("user1", "user1@gmail.com",
                    passwordEncoder.encode("12345678"));
            User user2 = new User("user2", "user2@gmail.com", passwordEncoder.encode("12345678"));
            User admin = new User("adminserver", "admin@gmail.com",
                    passwordEncoder.encode("12345678")).setRole(Role.admin);
            User existUser1 = userRepository.findByUsername(user.getUsername()).orElse(null);
            User existUser2 = userRepository.findByUsername(user2.getUsername()).orElse(null);
            User existAdmin = userRepository.findByUsername(admin.getUsername()).orElse(null);
            if(existUser1 == null) userRepository.save(user);
            if(existUser2 == null) userRepository.save(user2);
            if(existAdmin == null) userRepository.save(admin);
        };
    }
}
