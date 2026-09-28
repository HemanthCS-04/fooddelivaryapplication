package com.hungerbyte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Entry Point for the HungerByte Full-Stack Food Delivery Application.
 * 
 * When running in Eclipse:
 * Right-click this file -> Run As -> Spring Boot App (or Java Application)
 * 
 * The embedded Tomcat server starts on http://localhost:8080/
 */
@SpringBootApplication
public class HungerByteApplication {

    public static void main(String[] args) {
        SpringApplication.run(HungerByteApplication.class, args);
        System.out.println("=================================================");
        System.out.println(" HungerByte Food Delivery App Started Successfully!");
        System.out.println(" Local Web App: http://localhost:8080/");
        System.out.println(" Database: hungerbyte_db (MySQL on port 3306)");
        System.out.println("=================================================");
    }
}
