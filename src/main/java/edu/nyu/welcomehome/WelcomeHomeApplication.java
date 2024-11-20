package edu.nyu.welcomehome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WelcomeHomeApplication {
    
    public static void main(String[] args) {
        System.out.println("Starting the server");
        SpringApplication.run(WelcomeHomeApplication.class, args);
    }

}
