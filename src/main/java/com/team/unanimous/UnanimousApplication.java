package com.team.unanimous;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UnanimousApplication {

//public static final String APPLICATION_LOCATIONS = "spring.config.location="
//        + "classpath:application.yaml";
//    public static final String APPLICATION_LOCATIONS2 = "spring.config.location="
//            + "classpath:application.properties";
//
    public static void main(String[] args) {
//        new SpringApplicationBuilder(UnanimousApplication.class)
//                .properties(APPLICATION_LOCATIONS)
//                .properties(APPLICATION_LOCATIONS2)
//                .run(args);
//
        SpringApplication.run(UnanimousApplication.class, args);
    }

}
