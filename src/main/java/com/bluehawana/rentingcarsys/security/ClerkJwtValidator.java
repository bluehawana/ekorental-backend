package com.bluehawana.rentingcarsys.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ClerkJwtValidator {

    private static final Logger logger = Logger.getLogger(ClerkJwtValidator.class.getName());
    private JWTVerifier verifier;

    @Value("${clerk.jwt.publicKey}")
    private String publicKeyUrl;

    @PostConstruct
    public void init() {
        try {
            RSAPublicKey rsaPublicKey = getPublicKeyFromUrl(publicKeyUrl);
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, null);
            this.verifier = JWT.require(algorithm).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize ClerkJwtValidator", e);
            // Don't throw an exception here, as it will prevent the bean from being created
        }
    }

    private RSAPublicKey getPublicKeyFromUrl(String publicKeyUrl) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        URL url = new URL(publicKeyUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (InputStream inputStream = connection.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(inputStream);

            JsonNode keysNode = jsonNode.get("keys");
            if (keysNode == null || !keysNode.isArray() || keysNode.size() == 0) {
                logger.severe("No 'keys' found in JWKS response");
                throw new IllegalArgumentException("Invalid JWKS format: 'keys' field is missing or empty");
            }

            // Get the first key
            JsonNode firstKeyNode = keysNode.get(0);
            String modulusBase64 = firstKeyNode.get("n").asText(); // "n" field
            String exponentBase64 = firstKeyNode.get("e").asText(); // "e" field

            // Decode the modulus and exponent from Base64
            byte[] modulusBytes = Base64.getUrlDecoder().decode(modulusBase64);
            byte[] exponentBytes = Base64.getUrlDecoder().decode(exponentBase64);

            // Create the RSA public key specification
            BigInteger modulus = new BigInteger(1, modulusBytes);
            BigInteger exponent = new BigInteger(1, exponentBytes);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }
    }

    public DecodedJWT validateToken(String token) {
        if (verifier == null) {
            logger.severe("JWT verifier is not initialized. Token validation is not possible.");
            throw new IllegalStateException("JWT verifier is not initialized");
        }
        return verifier.verify(token);
    }

    public List<String> extractRoles(DecodedJWT jwt) {
        List<String> roles = jwt.getClaim("roles").asList(String.class);
        return roles != null ? roles : Collections.emptyList();
    }
}
