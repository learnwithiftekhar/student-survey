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
            User user = new User("admin", passwordEncoder.encode("admin123"), "ROLE_USER");
            User mily = new User("mily", passwordEncoder.encode("mily123"), "ROLE_USER");
            User noman = new User("noman", passwordEncoder.encode("noman123"), "ROLE_USER");
            User rashid = new User("rashid", passwordEncoder.encode("rashid123"), "ROLE_USER");

            Optional<User> userfromDb = userRepository.findByUsername("admin");
            Optional<User> milyfromDb = userRepository.findByUsername("mily");
            Optional<User> nomanfromDb = userRepository.findByUsername("noman");
            Optional<User> rashidfromDb = userRepository.findByUsername("rashid");

            if (userfromDb.isEmpty()) {
                userRepository.save(user);
            }


            if (milyfromDb.isEmpty()) {
                userRepository.save(mily);
            }

            if (nomanfromDb.isEmpty()) {
                userRepository.save(noman);
            }

            if (rashidfromDb.isEmpty()) {
                userRepository.save(rashid);
            }
        };
    }
}
