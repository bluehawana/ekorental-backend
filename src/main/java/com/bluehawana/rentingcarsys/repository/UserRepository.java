package com.bluehawana.rentingcarsys.repository;

import com.bluehawana.rentingcarsys.model.AuthProvider;
import com.bluehawana.rentingcarsys.model.ProviderType;
import com.bluehawana.rentingcarsys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndProvider(String email, String provider);
    Optional<User> findByEmailAndProviderId(String email, String providerId);
    boolean existsByEmail(String email);
    long countByProvider(AuthProvider provider);

 }