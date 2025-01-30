package com.bluehawana.rentingcarsys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "file:${user.home}/.env", ignoreResourceNotFound = true)
})
public class PropertySourceConfig {
}
