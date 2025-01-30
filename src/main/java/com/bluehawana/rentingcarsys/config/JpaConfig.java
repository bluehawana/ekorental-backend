package com.bluehawana.rentingcarsys.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.bluehawana.rentingcarsys.repository")
@EntityScan(basePackages = "com.bluehawana.rentingcarsys.model")
public class JpaConfig {
    // Any additional JPA configuration can go here
}