package com.bluehawana.rentingcarsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.bluehawana.rentingcarsys.model")
@EnableJpaRepositories(basePackages = "com.bluehawana.rentingcarsys.repository")
public class RentingcarsysApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentingcarsysApplication.class, args);
    }
}