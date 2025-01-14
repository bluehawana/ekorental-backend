package com.bluehawana.rentingcarsys.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.bluehawana.rentingcarsys.model")
@EnableJpaRepositories(basePackages = "com.bluehawana.rentingcarsys.repository")
public class JpaConfig {
    // Configuration is handled by annotations
}