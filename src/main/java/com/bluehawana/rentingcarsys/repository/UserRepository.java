package com.bluehawana.rentingcarsys.repository;


import com.bluehawana.rentingcarsys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhonenumber(String phone);
}
