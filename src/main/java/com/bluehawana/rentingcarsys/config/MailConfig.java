package com.bluehawana.rentingcarsys.config;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    @Bean
    public MailjetClient mailjetClient() {
        return new MailjetClient(apiKey, apiSecret);
    }
}