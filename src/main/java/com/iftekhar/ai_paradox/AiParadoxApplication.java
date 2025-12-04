package com.iftekhar.ai_paradox;

import com.iftekhar.ai_paradox.model.User;
import com.iftekhar.ai_paradox.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class AiParadoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiParadoxApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User user = new User("iftekhar", passwordEncoder.encode("1234"), "ROLE_USER");
            Optional<User> userfromDb = userRepository.findByUsername("iftekhar");

            if (userfromDb.isEmpty()) {
                userRepository.save(user);
            }
        };
    }
}
