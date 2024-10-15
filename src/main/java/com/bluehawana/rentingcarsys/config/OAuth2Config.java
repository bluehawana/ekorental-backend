package com.bluehawana.rentingcarsys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuth2Config {

    @Value("${spring.security.oauth2.client.registration.github.client-id:}")
    private String githubClientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret:}")
    private String githubClientSecret;

    @Value("${spring.security.oauth2.client.registration.github.scope:read:user,user:email}")
    private String[] githubScopes;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri:http://localhost:8080/login/oauth2/code/github}")
    private String githubRedirectUri;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration githubRegistration = ClientRegistration.withRegistrationId("github")
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(githubRedirectUri)
                .scope(githubScopes)
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .clientName("GitHub")
                .build();

        return new InMemoryClientRegistrationRepository(githubRegistration);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}