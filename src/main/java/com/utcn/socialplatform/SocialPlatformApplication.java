package com.utcn.socialplatform;

import com.utcn.socialplatform.model.Role;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocialPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialPlatformApplication.class, args);
    }

}